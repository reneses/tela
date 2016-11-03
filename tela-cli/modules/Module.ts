import {Url} from "url";
import url = require("url");
import http = require("http");
import open = require("open");

export abstract class Module {

    public abstract readonly name: string;

    public abstract isConfigured(): boolean;
    public abstract connect(callback: (moduleToken: string) => void): void;

    protected startAuthServer(port: number, callback: (url: Url) => void) {
        let authServer = http.createServer((req: any, res: any) => {
            req.connection.ref();
            let html = "<html>" +
                "<head><script>close();</script></head>" +
                "<body>Login successful! You can now close this window</body>" +
                "</html>";
            res.end(html, () => {
                req.connection.unref();
                authServer.close();
            });
            callback(url.parse(req.url, true));
        });
        authServer.on("connection", (socket: any) => socket.unref()).listen(port);
    }

    protected openBrowser(url: string) {
        open(url);
    }

}
