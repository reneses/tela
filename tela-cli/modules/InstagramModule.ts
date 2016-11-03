import {Module} from "./Module";
import {ConnectionManager} from "../connection/ConnectionManager";
import request = require("request");
import {Url} from "url";

export class InstagramModule extends Module {

    public readonly name = "instagram";
    private connectionManager = new ConnectionManager();

    public isConfigured() {
        return this.connectionManager.existsProperty(this.name, "clientId")
            && this.connectionManager.existsProperty(this.name, "clientSecret");
    };

    public connect(callback: (moduleToken: string) => void) {

        if (!this.isConfigured()) {
            console.error("The clientId and the clientSecret must be configured first:\n" +
                "tela-cli configure " + this.name + " clientId <client_id>\n" +
                "tela-cli configure " + this.name + " clientSecret <client_secret>");
            return;
        }

        let clientId = this.connectionManager.getProperty(this.name, "clientId");
        let clientSecret = this.connectionManager.getProperty(this.name, "clientSecret");

        let port = 8082;
        let redirectUri = "http://127.0.0.1:" + port + "/";
        let instagramUrl = "https://api.instagram.com/oauth/authorize/?"
            + "client_id=" + clientId
            + "&redirect_uri=" + encodeURI(redirectUri)
            + "&scope=public_content&follower_list"
            + "&response_type=code";

        this.startAuthServer(port, (url: Url) => {
            let authCode = url.query.code;
            request.post(
                {
                    form: {
                        client_id: clientId,
                        client_secret: clientSecret,
                        code: authCode,
                        grant_type: "authorization_code",
                        redirect_uri: redirectUri,
                    },
                    url: "https://api.instagram.com/oauth/access_token",
                },
                (err: any, httpResponse: any, body: string) => {
                    let obj = JSON.parse(body);
                    console.log("Logged into Instagram account @" + obj.user.username);
                    callback(obj.access_token);
                });
        });
        this.openBrowser(instagramUrl);
    }

}
