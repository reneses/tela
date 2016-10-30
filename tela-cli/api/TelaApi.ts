import {IAction} from "./IAction";
import request = require("request");

export class TelaApi {

    constructor(private host: string, private port: number, private accessToken?: string) {
    }

    private getTelaUrl(endpoint: string): string {
        let url = this.host + ":" + this.port;
        if (url.indexOf("http") < 0) {
            url = "http://" + url + endpoint;
        }
        return url;
    };

    /**
     * Create a session in the Tela Server
     *
     * @param callback Callback to be executed, the access token retrieved will be passed as parameter
     */
    public createSession = function (callback: (accessToken: string) => void) {
        request.post(this.getTelaUrl("/auth"), {form: {}}, (error: any, response: any, body: string) => {
            if (error || response.statusCode !== 200) {
                callback(null);
            }
            callback(body);
        });
    };

    /**
     * Obtain help from Tela
     *
     * @param module
     * @param callback
     */
    public help(module: string, callback: (help: IAction[]) => void) {
        let url = this.getTelaUrl("/help/" + (module || ""));
        request(url, (error: any, response: any, body: string) => {
            callback(JSON.parse(body));
        });
    }

    /**
     * Add a module token
     *
     * @param module
     * @param token
     * @param callback
     */
    public addModuleToken(module: string, token: string, callback: (err: any) => void) {
        request.post(
            this.getTelaUrl(`/auth/${module}?`),
            {
                auth: {bearer: this.accessToken},
                form: {token: token},
            },
            (error: any, response: any, body: string) => callback(error || response.statusCode !== 200));
    }

    /**
     * Delete a module token
     *
     * @param module
     * @param callback
     */
    public deleteModuleToken(module: string, callback: (err: any) => void) {
        request.del(
            this.getTelaUrl(`/auth/${module}`),
            {
                auth: {bearer: this.accessToken},
            },
            (error: any, response: any, body: string) => callback(error || response.statusCode !== 200));
    };

    /**
     * Execute an action
     *
     * @param module
     * @param action
     * @param params
     * @param callback
     */
    public executeAction(module: string, action: string,
                         params: {[param: string]: string},
                         callback: (code: number, result: any) => void) {

        let url = this.getTelaUrl(`/action/${module}/${action}?`);
        Object.keys(params).forEach((key) => url += key + "=" + params[key] + "&");

        request(url, {auth: {bearer: this.accessToken}},
            (err: any, response: any, body: string) => {
                let result = !err && response.statusCode === 200 ? JSON.parse(body) : body;
                callback(response.statusCode, result);
            });
    };

}
