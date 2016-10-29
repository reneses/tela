import {ConnectionManager} from "./ConnectionManager";
import {TelaApi} from "./TelaApi";
import {Module} from "./modules/Module";
import {InstagramModule} from "./modules/InstagramModule";
import {Action} from "./models/Action";

export class TelaCli {

    private static readonly DEFAULT_PORT = 80;
    private connectionManager = new ConnectionManager();
    private modules: { [name: string]: Module } = {};

    constructor() {
        this.addModule(new InstagramModule());
    }

    private addModule(module: Module) {
        this.modules[module.name] = module;
    }

    /**
     * Print the available commands
     */
    public static printCommands() {
        console.log(`Usage:
            tela-cli connect <host> [<port>]
            tela-cli link <module>
            tela-cli unlink <module>
            tela-cli disconnect
            tela-cli configure <module> <property> <value>
            tela-cli execute <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]
            tela-cli help [<module>]`)
    }

    /**
     * Connect to the server
     *
     * @param host
     * @param port
     */
    public connect(host?: string, port = TelaCli.DEFAULT_PORT) {
        if (!host) {
            console.error('Incorrect parameters, usage:  connect <url> [port]');
            return;
        }
        console.log('Establishing connection with Tela at ' + host + ':' + port + "...");
        new TelaApi(host, port).createSession((accessToken: string) => {
            if (!accessToken) {
                console.error('The connection could not be saved! Please, try again.');
                return;
            }
            this.connectionManager.create(host, port, accessToken);
            console.log('Connection established with token \'' + accessToken + '\'');
        });
    }

    /**
     * Disconnect from the server
     */
    public disconnect() {
        this.connectionManager.destroy();
        console.log('Connection destroyed');
    }

    /**
     * Obtain help from Tela. It retrieves all the help from Tela. However, some modules might not be supported by
     * this CLI tool.
     *
     * @param module
     */
    public help(module: string) {
        let connection = this.connectionManager.load();
        new TelaApi(connection.host, connection.port).help(module, (help) => {
            help
                .forEach((action: Action) => {
                    console.log(action.module + '/' + action.name);
                    if (action.description)
                        console.log(' - Description: ' + action.description);
                    if (action.params.length)
                        console.log(' - Parameters: ' + action.params.join(', '));
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
            console.error('Incorrect parameters, usage:  configure <module> <property> <value>');
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
        let connection = this.connectionManager.load();
        module.connect((moduleAccessToken: string) => {
            let tela = new TelaApi(connection.host, connection.port);
            tela.addModuleToken(connection.accessToken, moduleName, moduleAccessToken, (err: any) => {
                if (err)
                    console.error('The Instagram could not be linked due to an internal error', err);
                else
                    console.log('Module linked to Tela');
            });
        });

    }

    /**
     * Unlink a module (remove its token from Tela)
     * @param moduleName
     */
    public unlinkModule(moduleName: string) {
        let connection = this.connectionManager.load();
        let tela = new TelaApi(connection.host, connection.port);
        tela.deleteModuleToken(connection.accessToken, moduleName, (err) => {
            if (err)
                console.error(`The module '${moduleName}' was not linked before`);
            else
                console.log(`The module '${moduleName}' was successfully unlinked`);
        });
    }

    /**
     * Execute an action
     *
     * @param module
     * @param action
     * @param params
     */
    public executeAction(module: string, action: string, params: string[]) {
        if (!module || !action) {
            console.error('Incorrect parameters, usage:  execute <module> <action> [<param1>=<value1> [<param2>=<value2> ...]]');
            return;
        }
        var paramsObj: { [param: string]: string} = {};
        params.forEach((param) => {
            let p = param.split('=');
            paramsObj[p[0]] = p[1];
        });
        let connection = this.connectionManager.load();
        let tela = new TelaApi(connection.host, connection.port);
        tela.executeAction(connection.accessToken, module, action, paramsObj, (code, result) => {
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

}