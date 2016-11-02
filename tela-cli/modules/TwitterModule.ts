import {Module} from "./Module";
import {ConnectionManager} from "../connection/ConnectionManager";
import {Url} from "url";

export class TwitterModule extends Module {

    public readonly name = "twitter";
    private connectionManager = new ConnectionManager();

    public isConfigured() {
        return this.connectionManager.existsProperty(this.name, "apiKey")
            && this.connectionManager.existsProperty(this.name, "apiSecret");
    };

    public connect(callback: (moduleToken: string) => void) {

        if (!this.isConfigured()) {
            console.error("The clientId and the clientSecret must be configured first:\n" +
                "tela-cli configure " + this.name + " apiKey <apiKey>\n" +
                "tela-cli configure " + this.name + " apiSecret <apiSecret>");
            return;
        }

        let apiKey = this.connectionManager.getProperty(this.name, "apiKey");
        let apiSecret = this.connectionManager.getProperty(this.name, "apiSecret");

        let port = 8082;
        let twitter = require("node-twitter-api")({
            callback: "http://172.0.0.1:" + port,
            consumerKey: apiKey,
            consumerSecret: apiSecret,
        });
        twitter.getRequestToken((error: any, requestToken: string, requestTokenSecret: string) => {
            if (error) {
                console.log("Error getting Twitter token", error);
                callback(null);
            }
            this.startAuthServer(port, (url: Url) => {
                let oauthVerifier = url.query.oauth_verifier;
                twitter.getAccessToken(requestToken, requestTokenSecret, oauthVerifier,
                    (err: any, accessToken: string, accessTokenSecret: string, result: { screen_name: string}) => {
                        if (error) {
                            console.log("Error getting Twitter token", err);
                            callback(null);
                        }
                        console.log("Logged into Twitter account @" + result.screen_name);
                        let moduleToken = `${apiKey}:${apiSecret}:${accessToken}:${accessTokenSecret}`;
                        callback(moduleToken);
                    });
            });
            this.openBrowser(twitter.getAuthUrl(requestToken));
        });
    }

}
