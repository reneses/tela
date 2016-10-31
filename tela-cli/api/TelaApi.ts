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
     * Delete a session from the Tela Server
     *
     * @param callback Callback to be executed
     */
    public deleteSession = function (callback: (errorMessage: string) => void) {
        request.del(
            this.getTelaUrl(`/auth`),
            {auth: {bearer: this.accessToken}},
            (err: any, response: any, body: string) => {
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
    public execute(module: string, action: string,
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

    /**
     * Schedule an action
     *
     * @param delay
     * @param module
     * @param action
     * @param params
     * @param callback
     */
    public schedule(delay: number,
                    module: string, action: string,
                    params: {[param: string]: string},
                    callback: (code: number, result: any) => void) {

        let url = this.getTelaUrl(`/schedule/${module}/${action}?delay=${delay}`);
        Object.keys(params).forEach((key) => url += "&" + key + "=" + params[key]);

        request(url, {auth: {bearer: this.accessToken}},
            (err: any, response: any, body: string) => {
                let result = !err && response.statusCode === 200 ? JSON.parse(body) : body;
                callback(response.statusCode, result);
            });
    };

    /**
     * Get the scheduled actions
     *
     * @param callback
     */
    public getScheduled(callback: (code: number, result: any) => void) {
        let url = this.getTelaUrl(`/schedule/`);
        request(url, {auth: {bearer: this.accessToken}},
            (err: any, response: any, body: string) => {
                let result = !err && response.statusCode === 200 ? JSON.parse(body) : body;
                callback(response.statusCode, result);
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
            {auth: {bearer: this.accessToken}},
            (err: any, response: any, body: string) => {
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
            {auth: {bearer: this.accessToken}},
            (err: any, response: any, body: string) => {
                let errorMessage = err || response.statusCode !== 200 ? body : null;
                callback(errorMessage);
            });
    };

}
