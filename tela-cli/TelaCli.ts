import {ConnectionManager} from "./connection/ConnectionManager";
import {TelaApi} from "./api/TelaApi";
import {IModule} from "./modules/IModule";
import {InstagramModule} from "./modules/InstagramModule";
import {IAction} from "./api/IAction";

export class TelaCli {

    private static readonly DEFAULT_PORT = 80;
    private modules: { [name: string]: IModule } = {};
    private connectionManager = new ConnectionManager();
    private telaApi: TelaApi;

    constructor() {
        this.addModule(new InstagramModule());
        if (this.connectionManager.isConnected()) {
            let connection = this.connectionManager.get();
            this.telaApi = new TelaApi(connection.host, connection.port, connection.accessToken);
        }
    }

    private addModule(module: IModule) {
        this.modules[module.name] = module;
    }

    private getApi(): TelaApi {
        if (!this.telaApi) {
            console.error("A Tela connection is required. Connect with: connect [url] [port]");
            process.exit();
        }
        return this.telaApi;
    }

    /**
     * Print the available commands
     */
    public printCommands() {
        console.log("Usage:\n" +
            "tela-cli connect <host> [<port>]\n" +
            "tela-cli link <module>\n" +
            "tela-cli unlink <module>\n" +
            "tela-cli disconnect\n" +
            "tela-cli configure <module> <property> <value>\n" +
            "tela-cli execute <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]\n" +
            "tela-cli help [<module>]\n" +
            "tela-cli schedule <delay> <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]\n" +
            "tela-cli scheduled\n" +
            "tela-cli cancel (<scheduled_action_id> | all)");
    }

    /**
     * Connect to the server
     *
     * @param host
     * @param port
     */
    public connect(host?: string, port = TelaCli.DEFAULT_PORT) {
        if (!host) {
            console.error("Incorrect parameters, usage: tela-cli connect <url> [port]");
            return;
        }
        console.log(`Establishing connection with Tela at ${host}:${port}...`);
        new TelaApi(host, port).createSession((accessToken: string) => {
            if (!accessToken) {
                console.error("The connection could not established.");
                return;
            }
            this.connectionManager.create(host, port, accessToken);
            this.telaApi = new TelaApi(host, port, accessToken);
            console.log(`Connection established with token '${accessToken}'`);
        });
    }

    /**
     * Disconnect from the server
     */
    public disconnect() {
        this.getApi().deleteSession((err) => {
            if (err) {
                console.error(`Error destroying the connection: ${err}`);
                return;
            }
            this.connectionManager.destroy();
            console.log("Connection destroyed");
        });
    }

    /**
     * Obtain help from Tela. It retrieves all the help from Tela. However, some modules might not be supported by
     * this CLI tool.
     *
     * @param module
     */
    public help(module: string) {
        this.getApi().help(module, (help) => {
            help
                .forEach((action: IAction) => {
                    console.log(`${action.module}/${action.name}`);
                    if (action.description) {
                        console.log(` - Description:  ${action.description}`);
                    }
                    if (action.params.length) {
                        console.log(` - Parameters: ${action.params.join(", ")}`);
                    }
                });
        });
    }

    /**
     * Configure a module property
     *
     * @param module
     * @param property
     * @param value
     */
    public configure(module: string, property: string, value: any) {
        if (!module || !property || !value) {
            console.error("Incorrect parameters, usage: tela-cli configure <module> <property> <value>");
            return;
        }
        this.connectionManager.setProperty(module, property, value);
    };

    /**
     * Link a module, this is, connect to it and store its token in Tela
     *
     * @param moduleName
     */
    public linkModule(moduleName: string) {

        // Obtain the module
        let module = this.modules[moduleName];
        if (!module) {
            console.log("The module '" + moduleName + "' is not currently supported in the CLI");
            return;
        }

        // Link it
        module.connect((moduleAccessToken: string) => {
            this.getApi().addModuleToken(moduleName, moduleAccessToken, (err: any) => {
                if (err) {
                    console.error("The Instagram could not be linked due to an internal error", err);
                } else {
                    console.log("Module linked to Tela");
                }
            });
        });

    }

    /**
     * Unlink a module (remove its token from Tela)
     * @param moduleName
     */
    public unlinkModule(moduleName: string) {
        this.getApi().deleteModuleToken(moduleName, (err) => {
            if (err) {
                console.error(`The module '${moduleName}' was not linked before`);
            } else {
                console.log(`The module '${moduleName}' was successfully unlinked`);
            }
        });
    }

    private filterInvalidParams(params: string[]): string[] {
        return params.filter((param) => {
            return !/^[a-zA-Z][a-zA-Z0-9]*=[^=]*$/g.test(param);
        });
    }

    /**
     * Execute an action
     *
     * @param module
     * @param action
     * @param params
     */
    public execute(module: string, action: string, params: string[]) {
        // Valdiate module and action
        if (!module || !action) {
            console.error("Incorrect parameters, usage: " +
                "tela-cli execute <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]");
            return;
        }
        // Validate the params
        let invalidParams = this.filterInvalidParams(params);
        if (invalidParams.length) {
            console.error("The following parameters are not valid: ", invalidParams);
            return;
        }
        // Execute the action
        this.getApi().execute(module, action, params, (code, result) => {
            switch (code) {
                case 200:
                    console.log(result);
                    break;
                case 403:
                    let message403 = result ? result :
                        `The action '${module}/${action}' requires a token. Please, link the module first.`;
                    console.error(message403);
                    break;
                case 404:
                    let message404 = result ? result :
                        `The action '${module}/${action}' was not found with the supplied parameters.`;
                    console.error(message404);
                    break;
                default:
                    let message = result ? result :
                        `The action '${module}/${action}' could not be executed (Error ${code}).`;
                    console.error(message);
            }
        });
    }

    /**
     * Schedule an action
     *
     * @param delay
     * @param module
     * @param action
     * @param params
     */
    public schedule(delay: number, module: string, action: string, params: string[]) {
        // Validate delay, module and action
        if (!delay || !module || !action) {
            console.error("Incorrect parameters, usage:  " +
                "tela-cli schedule <delay> <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]");
            return;
        }
        // Validate the params
        let invalidParams = this.filterInvalidParams(params);
        if (invalidParams.length) {
            console.error("The following parameters are not valid: ", invalidParams);
            return;
        }
        // Schedule
        this.getApi().schedule(delay, module, action, params, (code, result) => {
            switch (code) {
                case 200:
                    console.log(result);
                    break;
                case 403:
                    let message403 = result ? result :
                        `The action '${module}/${action}' requires a token. Please, link the module first.`;
                    console.error(message403);
                    break;
                case 404:
                    let message404 = result ? result :
                        `The action '${module}/${action}' was not found with the supplied parameters.`;
                    console.error(message404);
                    break;
                default:
                    let message = result ? result :
                        `The action '${module}/${action}' could not be executed (Error ${code}).`;
                    console.error(message);
            }
        });
    }

    /**
     * Get the scheduled actions
     */
    public getScheduled() {
        this.getApi().getScheduled((code, result) => {
            switch (code) {
                case 200:
                    console.log(result);
                    break;
                default:
                    console.error(`Error ${code} obtaining the scheduled actions: ${result}`);
            }
        });
    }

    /**
     * Get the scheduled actions
     */
    public cancelScheduled(action: string) {
        if (action === "all") {
            this.getApi().cancelAllScheduled((err) => {
                if (err) {
                    console.error(`Error canceling the all the scheduled actions: ${err}`);
                    return;
                }
                console.log("All the scheduled actions have been cancelled");
            });
            return;
        }
        let actionId = +action;
        if (isNaN(actionId)) {
            console.error("Incorrect parameters, usage: tela-cli cancel (<scheduled_action_id> | all)");
            return;
        }
        this.getApi().cancelScheduled(actionId, (err) => {
            if (err) {
                console.error(`Error canceling the scheduled action ${actionId}: ${err}`);
                return;
            }
            console.log("Scheduled action has been cancelled");
        });
    }

}
