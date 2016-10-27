package io.reneses.tela.core.api.server;

import java.io.IOException;
import java.net.InetAddress;

/**
 * Common functionality for a Tela Server
 */
abstract class AbstractTelaServer implements TelaServer {

    private int port;
    private String localIp;

    /**
     * Constructor admitting a port
     *
     * @param port Port to use
     */
    AbstractTelaServer(int port) {
        if (port < 0)
            throw new IllegalArgumentException("There port specified in the configuration is not valid");
        this.port = port;
    }

    /**
     * Get the port the server is running at
     *
     * @return Port number
     */
    public int getPort() {
        return port;
    }

    /**
     * Get local IP
     *
     * @return Local IP
     */
    public String getLocalIp() {
        if (localIp == null) {
            try {
                localIp = InetAddress.getLocalHost().getHostAddress();
            }
            catch (IOException ignored) {}
        }
        return localIp;
    }

}
