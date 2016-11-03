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

    private checkConnection(response: any) {
        if (!response) {
            console.log("It was not possible to connect to the server (Is Tela Running?)");
            process.exit(-1);
        }
    }

    /**
     * Create a session in the Tela Server
     *
     * @param callback Callback to be executed, the access token retrieved will be passed as parameter
     */
    public createSession = function (callback: (accessToken: string) => void) {
        let url = this.getTelaUrl("/auth");
        request.post(url, {form: {}}, (error: any, response: any, body: string) => {
            this.checkConnection(response);
            if (error || response.statusCode !== 200) {
                callback(null);
                return;
            }
            callback(body);
        });
    };

    /**
     * Delete a session from the Tela Server
     *
     * @param callback Callback to be executed
     */
    public deleteSession = function (callback: (errorMessage: string) => void) {
        let url = this.getTelaUrl(`/auth`);
        request.del(url, {auth: {bearer: this.accessToken}}, (err: any, response: any, body: string) => {
            this.checkConnection(response);
            let errorMessage = err || response.statusCode !== 200 ? body : null;
            callback(errorMessage);
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
            this.checkConnection(response);
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
        let url = this.getTelaUrl(`/auth/${module}?`);
        request.post(url, {auth: {bearer: this.accessToken}, form: {token: token}},
            (error: any, response: any) => {
                this.checkConnection(response);
                callback(error || response.statusCode !== 200);
            });
    }

    /**
     * Delete a module token
     *
     * @param module
     * @param callback
     */
    public deleteModuleToken(module: string, callback: (err: any) => void) {
        let url = this.getTelaUrl(`/auth/${module}`);
        request.del(url, {auth: {bearer: this.accessToken}}, (error: any, response: any) => {
            this.checkConnection(response);
            callback(error || response.statusCode !== 200);
        });
    };

    /**
     * Execute an action
     *
     * @param module
     * @param action
     * @param params
     * @param callback
     */
    public execute(module: string, action: string, params: string[],
                   callback: (code: number, result: string) => void) {

        let url = this.getTelaUrl(`/action/${module}/${action}?`) + params.join("&");
        request(url, {auth: {bearer: this.accessToken}}, (err: any, response: any, body: string) => {
            this.checkConnection(response);
            callback(response.statusCode, body);
        });
    };

    /**
     * Schedule an action
     *
     * @param delay
     * @param module
     * @param action
     * @param params
     * @param callback
     */
    public schedule(delay: number, module: string, action: string, params: string[],
                    callback: (code: number, result: string) => void) {

        let url = this.getTelaUrl(`/schedule/${module}/${action}?delay=${delay}&`) + params.join("&");
        request(url, {auth: {bearer: this.accessToken}}, (err: any, response: any, body: string) => {
            this.checkConnection(response);
            callback(response.statusCode, body);
        });
    };

    /**
     * Get the scheduled actions
     *
     * @param callback
     */
    public getScheduled(callback: (code: number, result: string) => void) {
        let url = this.getTelaUrl(`/schedule/`);
        request(url, {auth: {bearer: this.accessToken}}, (err: any, response: any, body: string) => {
            this.checkConnection(response);
            callback(response.statusCode, body);
        });
    };

    /**
     * Cancel a scheduled actions
     *
     * @param scheduledActionId
     * @param callback
     */
    public cancelScheduled(scheduledActionId: number, callback: (errorMessage: string) => void) {
        request.del(
            this.getTelaUrl(`/schedule/${scheduledActionId}`),
            {auth: {bearer: this.accessToken}}, (err: any, response: any, body: string) => {
                this.checkConnection(response);
                let errorMessage = err || response.statusCode !== 200 ? body : null;
                callback(errorMessage);
            });
    };

    /**
     * Cancel all the scheduled actions
     *
     * @param callback
     */
    public cancelAllScheduled(callback: (errorMessage: string) => void) {
        request.del(
            this.getTelaUrl(`/schedule`),
            {auth: {bearer: this.accessToken}}, (err: any, response: any, body: string) => {
                this.checkConnection(response);
                let errorMessage = err || response.statusCode !== 200 ? body : null;
                callback(errorMessage);
            });
    };

}
