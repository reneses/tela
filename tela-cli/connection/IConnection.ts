/**
 * IConnection model
 */
export interface IConnection {

    host?: string;
    port?: number;
    accessToken?: string;
    configuration?: { [key: string]: any; };

}
