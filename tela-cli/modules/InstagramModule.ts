import {IModule} from "./IModule";
import {ConnectionManager} from "../connection/ConnectionManager";
import request = require("request");
import open = require("open");

export class InstagramModule implements IModule {

    public readonly name = "instagram";
    private connectionManager = new ConnectionManager();

    public isConfigured() {
        return this.connectionManager.existsProperty(this.name, "clientId")
            && this.connectionManager.existsProperty(this.name, "clientSecret");
    };

    private exchangeCodeForToken(clientId: string, clientSecret: string, redirectUri: string, code: string,
                                 callback: (accessToken: string) => void) {

        request.post(
            {
                form: {
                    client_id: clientId,
                    client_secret: clientSecret,
                    code: code,
                    grant_type: "authorization_code",
                    redirect_uri: redirectUri,
                },
                url: "https://api.instagram.com/oauth/access_token",
            }, (err: any, httpResponse: any, body: string) => {
                let obj = JSON.parse(body);
                console.log("Logged into Instagram account @" + obj.user.username);
                callback(obj.access_token);
            });
    }

    private startAuthServer(clientId: string, clientSecret: string, redirectUri: string,
                            callback: (accessToken: string) => void) {

        let authServer = require("http")
            .createServer((req: any, res: any) => {
                req.connection.ref();
                let url = require("url").parse(req.url, true);
                let authCode = url.query.code;
                this.exchangeCodeForToken(clientId, clientSecret, redirectUri, authCode, callback);
                let html = "<html>" +
                                "<head><script>close();</script></head>" +
                                 "<body>Login successful! You can now close this window</body>" +
                            "</html>" ;
                res.end(html, () => {
                    req.connection.unref();
                    authServer.close();
                });
            })
            .on("connection", (socket: any) => {
                socket.unref();
            })
            .listen(8082);
    }

    private static openBrowser(url: string) {
        open(url);
    }

    public connect(callback: (accessToken: string) => void) {

        if (!this.isConfigured()) {
            console.error("The clientId and the clientSecret must be configured first:\n" +
                "tela-cli configure " + this.name + " clientId <client_id>\n" +
                "tela-cli configure " + this.name + " clientSecret <client_secret>");
            return;
        }

        let clientId = this.connectionManager.getProperty(this.name, "clientId");
        let clientSecret = this.connectionManager.getProperty(this.name, "clientSecret");

        let redirectUri = "http://127.0.0.1:8082/";
        let instagramUrl = "https://api.instagram.com/oauth/authorize/?"
            + "client_id=" + clientId
            + "&redirect_uri=" + encodeURI(redirectUri)
            + "&scope=public_content&follower_list"
            + "&response_type=code";

        this.startAuthServer(clientId, clientSecret, redirectUri, callback);
        InstagramModule.openBrowser(instagramUrl);
    }

}
