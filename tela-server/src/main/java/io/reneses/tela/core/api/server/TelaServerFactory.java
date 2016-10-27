package io.reneses.tela.core.api.server;

/**
 * TelaServerFactory class.
 */
public class TelaServerFactory {

    /**
     * Create an instance of an implementation of the Server
     *
     * @param port a int.
     * @return a TelaServer instance
     */
    public static TelaServer create(int port) {
        return new JettyTelaServer(port);
    }

}
