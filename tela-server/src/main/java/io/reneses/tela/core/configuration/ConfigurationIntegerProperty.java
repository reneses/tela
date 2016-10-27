package io.reneses.tela.core.configuration;


import java.util.Properties;

/**
 * Integer ConfigurationProperty
 */
class ConfigurationIntegerProperty extends ConfigurationProperty<Integer> {

    ConfigurationIntegerProperty(Properties properties, String name, String envKey, String fileKey, Integer defaultValue) {
        super(properties, name, envKey, fileKey, defaultValue);
    }

    /** {@inheritDoc} */
    @Override
    protected Integer getEnvValue() {
        try {
            return Integer.valueOf(getStringEnvValue());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Integer getFileValue() {
        try {
            return Integer.valueOf(getStringFileValue());
        } catch (NumberFormatException e) {
            return null;
        }
    }


}
