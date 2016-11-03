import {Injectable} from "@angular/core";

/**
 * Configuration component, which read the options from the environmental variables
 * Env variables are exposed via webpack
 */
@Injectable()
export class Config {

    public readonly tela: {
        server: string;
    };

    public readonly instagram: {
        redirectHost: string;
        clientId: string;
    };

    constructor() {
        this.tela = {
            server: process.env.TELA_SERVER
        };
        this.instagram = {
            redirectHost: process.env.INSTAGRAM_REDIRECT_HOST,
            clientId: process.env.INSTAGRAM_CLIENT_ID
        };
    }

}