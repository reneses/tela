/**
 * Connection model
 */
export interface Connection {

    host?: string;
    port?: number;
    accessToken?: string;
    configuration?: { [key: string]: any; };

}
