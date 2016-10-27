package io.reneses.tela.core.configuration;

/**
 * Configuration, component in charge of reading the configurable options, as well as changing them programmatically
 * The priority of configuration for each option is as following:
 * 1. First, if an option has been set programmatically, it value will be taken
 * 2. If not, Tela tries to read the corresponding environmental variable.
 * 3. If it is not present, it tries to read it from the `tela.properties` file.
 * 4. If it is not present neither, it uses the default option.
 */
public interface Configuration {

    enum Property {
        PORT, SCHEDULER_DELAY, CACHE_TTL, CACHE_MODE,
        REDIS_HOST, REDIS_PORT, REDIS_USER, REDIS_PASSWORD,
        ORIENTDB_MODE, ORIENTDB_LOCAL, ORIENTDB_HOST, ORIENTDB_PORT, ORIENTDB_DATABASE, ORIENTDB_USER, ORIENTDB_PASSWORD
    }

    interface TempMode {
        String REDIS = "redis", MEMORY = "memory";
    }

    interface OrientDbMode {
        String REMOTE = "remote", MEMORY = "memory", LOCAL = "local";
    }

    /**
     * Set a property programmatically
     *
     * @param property Property to set
     * @param value    New value
     */
    void setProperty(Property property, int value);

    /**
     * Set a property programmatically
     *
     * @param property Property to set
     * @param value    New value
     */
    void setProperty(Property property, String value);

    /**
     * Get an integer property value
     *
     * @param property Property to retrieve
     * @return Property value
     */
    String getStringProperty(Property property);

    /**
     * Get an integer property value
     *
     * @param property Property to retrieve
     * @return Property value
     */
    int getIntegerProperty(Property property);

}
