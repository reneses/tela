import {Module} from "./Module";
import {ConnectionManager} from "../ConnectionManager";
var request = require('request'), fs = require('fs');

export class InstagramModule implements Module {

    readonly name = 'instagram';
    private connectionManager = new ConnectionManager();

    public isConfigured() {
        return this.connectionManager.existsProperty(this.name, 'clientId')
            && this.connectionManager.existsProperty(this.name, 'clientSecret');
    };

    private exchangeCodeForToken(clientId: string, clientSecret: string, redirectUri: string, code: string,
                                 callback: (accessToken: string) => void) {

        request.post(
            {
                url: 'https://api.instagram.com/oauth/access_token',
                form: {
                    client_id: clientId,
                    client_secret: clientSecret,
                    grant_type: 'authorization_code',
                    redirect_uri: redirectUri,
                    code: code
                }
            }, (err: any, httpResponse: any, body: string) => {
                let obj = JSON.parse(body);
                console.log('Logged into Instagram account @' + obj.user.username);
                callback(obj.access_token);
            });
    }

    private startAuthServer(clientId: string, clientSecret: string, redirectUri: string,
                            callback: (accessToken: string) => void) {

        let authServer = require('http')
            .createServer((req: any, res: any) => {
                req.connection.ref();
                var url = require('url').parse(req.url, true);
                var authCode = url.query.code;
                this.exchangeCodeForToken(clientId, clientSecret, redirectUri, authCode, callback);
                res.end('Login successful! You can now close this window', () => {
                    req.connection.unref();
                    authServer.close();
                });
            })
            .on('connection', (socket: any) => {
                socket.unref();
            })
            .listen(8082);
    }

    private static openBrowser(url: string) {
        require('open')(url);
    }

    public connect(callback: (accessToken: string) => void) {

        if (!this.isConfigured()) {
            console.error('The clientId and the clientSecret must be configured first:\n' +
                'tela-cli configure ' + this.name + ' clientId <client_id>\n' +
                'tela-cli configure ' + this.name + ' clientSecret <client_secret>');
            return;
        }

        var clientId = this.connectionManager.getProperty(this.name, 'clientId');
        var clientSecret = this.connectionManager.getProperty(this.name, 'clientSecret');

        var redirectUri = 'http://127.0.0.1:8082/';
        var instagramUrl = 'https://api.instagram.com/oauth/authorize/?'
            + 'client_id=' + clientId
            + '&redirect_uri=' + encodeURI(redirectUri)
            + '&scope=public_content&follower_list'
            + '&response_type=code';

        this.startAuthServer(clientId, clientSecret, redirectUri, callback);
        InstagramModule.openBrowser(instagramUrl);
    }

}