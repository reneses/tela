package io.reneses.tela.core.configuration;

import java.util.Properties;

/**
 * String ConfigurationProperty
 */
class ConfigurationStringProperty extends ConfigurationProperty<String> {


    ConfigurationStringProperty(Properties properties, String name, String envKey, String fileKey, String defaultValue) {
        super(properties, name, envKey, fileKey, defaultValue);
    }

    /** {@inheritDoc} */
    @Override
    protected String getEnvValue() {
        return getStringEnvValue();
    }

    /** {@inheritDoc} */
    @Override
    protected String getFileValue() {
        return getStringFileValue();
    }


}
