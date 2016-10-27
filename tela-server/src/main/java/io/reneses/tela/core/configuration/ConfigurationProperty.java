package io.reneses.tela.core.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Configurable property
 *
 * @param <T> Value type
 */
abstract class ConfigurationProperty<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationProperty.class);

    private Properties properties;
    private String name, envKey, fileKey;
    private T value, defaultValue;

    ConfigurationProperty(Properties properties, String name, String envKey, String fileKey, T defaultValue) {
        this.properties = properties;
        this.name = name;
        this.envKey = envKey;
        this.fileKey = fileKey;
        this.defaultValue = defaultValue;
    }

    final String getStringEnvValue() {
        return System.getenv(envKey);
    }

    final String getStringFileValue() {
        return properties == null ? null : properties.getProperty(fileKey);
    }

    /**
     * getEnvValue.
     *
     * @return a T object.
     */
    protected abstract T getEnvValue();

    /**
     * getFileValue.
     *
     * @return a T object.
     */
    protected abstract T getFileValue();

    /**
     * Set the value of the property
     *
     * @param value Property value
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * The priority of configuration for each property is as following:
     * 1. First, if an option has been set programmatically, it value will be taken
     * 2. If not, Tela tries to read the corresponding environmental variable.
     * 3. If it is not present, it tries to read it from the `tela.properties` file.
     * 4. If it is not present neither, it uses the default option.
     *
     * @return Property value
     */
    public T getValue() {
        T out = value;
        if (out != null) {
            LOGGER.info("[Configuration] Property '{}' set programmatically", name);
            return out;
        }
        out = getEnvValue();
        if (out != null) {
            LOGGER.info("[Configuration] Property '{}' read from the environmental variables", name);
            return out;
        }
        out = getFileValue();
        if (out != null) {
            LOGGER.info("[Configuration] Property '{}' read from the properties file", name);
            return out;
        }
        LOGGER.info("[Configuration] Property '{}' not set, using default value", name);
        return defaultValue;
    }

}
