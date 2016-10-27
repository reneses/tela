package io.reneses.tela.core.configuration;

/**
 * Configuration Factory to retrieve an instance of a configuration implementation
 */
public class ConfigurationFactory {

    private ConfigurationFactory() {
    }

    /**
     * Retrieve a Configuration instance
     *
     * @return Configuration instance
     */
    public static Configuration create() {
        return new ConfigurationImpl();
    }

}
