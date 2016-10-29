/**
 * Connection model
 */
export class Connection {

    constructor(public host?: string, public port?: number,
                public accessToken?: string,
                public configuration: { [key: string]: any; } = {}) {
    }

    public isConnected(): boolean {
        return !!this.host && !!this.port && !!this.accessToken;
    }

}