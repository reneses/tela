package io.reneses.tela.core.api.server;

/**
 * TelaServer interface.
 */
public interface TelaServer {

    /**
     * Default the sever will be executed at
     */
    int DEFAULT_PORT = 80;

    /**
     * Get the port the server is running at
     *
     * @return Port number
     */
    int getPort();

    /**
     * Get local IP
     *
     * @return Local IP
     */
    String getLocalIp();

    /**
     * Start the server server
     *
     * @return the object itself
     */
    TelaServer start();

    /**
     * Stop the server
     *
     * @return true if stopped, false if it could not been stopped
     */
    boolean stop();

    /**
     * Check if the server is running
     *
     * @return true if running, false if not
     */
    boolean isRunning();

}
