import {Connection} from "./Connection";
import fs = require("fs");
import path = require("path");

/**
 * Connection utility
 *
 * @param filename
 * @returns {{save: save, destroy: destroy, load: get}}
 * @constructor
 */
export class ConnectionManager {

    private static readonly DEFAULT_FILENAME = ".tela";
    private static connection: Connection;
    private filename: string;

    constructor(filename?: string) {
        this.filename = path.basename(filename || ConnectionManager.DEFAULT_FILENAME);
    }

    /**
     * Save a new connection
     *
     * @param connection
     */
    private save(connection: Connection) {
        fs.writeFileSync(this.filename, JSON.stringify(connection));
        ConnectionManager.connection = connection;
    };

    /**
     * Creatio a session
     *
     * @param host
     * @param port
     * @param accessToken
     */
    public create(host: string, port: number, accessToken: string) {
        let connection = this.get();
        connection.host = host;
        connection.port = port;
        connection.accessToken = accessToken;
        this.save(connection);
    }

    /**
     * Destroy the existing connection
     */
    public destroy () {
        ConnectionManager.connection = null;
        if (fs.existsSync(this.filename)) {
            fs.unlinkSync(this.filename);
        }
    }

    /**
     * Load a connection
     *
     * @returns connection
     */
    public get(): Connection {
        if (!ConnectionManager.connection) {
            if (!fs.existsSync(this.filename)) {
                return {};
            }
            let connection: Connection = JSON.parse(fs.readFileSync(this.filename).toString());
            if (!connection.configuration) {
                connection.configuration = {};
            }
            ConnectionManager.connection = connection;
        }
        return ConnectionManager.connection;
    };

    /**
     * Get a property
     *
     * @param module
     * @param property
     * @returns value or {null}
     */
    public getProperty(module: string, property: string): any {
        let connection = this.get();
        return connection.configuration[module] ? connection.configuration[module][property] : null;
    };

    /**
     * Set a property
     *
     * @param module
     * @param property
     * @param value
     */
    public setProperty(module: string, property: string, value: any) {
        let connection = this.get();
        if (!connection.configuration[module]) {
            connection.configuration[module] = {};
        }
        connection.configuration[module][property] = value;
        this.save(connection);
    }

    /**
     * Check if a property exists
     *
     * @param module
     * @param property
     * @returns {boolean}
     */
    public existsProperty(module: string, property: string) {
        return !!this.getProperty(module, property);
    };

    /**
     * Check if connected
     */
    public isConnected(): boolean {
        let connection = this.get();
        return !!connection.host && !!connection.port && !!connection.accessToken;
    }

}
