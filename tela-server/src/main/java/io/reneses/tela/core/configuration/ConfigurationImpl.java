package io.reneses.tela.core.configuration;

import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.cache.CacheManager;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.redis.JedisFactory;
import io.reneses.tela.core.scheduler.Scheduler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Implementation of the configuration
 */
class ConfigurationImpl implements Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurationImpl.class);
    private static final String CONFIGURATION_FILE = "tela.properties";

    private Properties configurationFile;
     Map<Property, ConfigurationProperty> properties = new HashMap<>();

    /**
     * Constructor, initiating all the configurable properties
     */
    ConfigurationImpl() {

        configurationFile = readPropertiesFile();

        addProperty(Property.PORT, "PORT", "port", TelaServer.DEFAULT_PORT);
        addProperty(Property.SCHEDULER_DELAY, "SCHEDULER_DELAY", "scheduler.delay", Scheduler.DEFAULT_DELAY);
        addProperty(Property.CACHE_TTL, "CACHE_TTL", "cache.ttl", CacheManager.DEFAULT_TTL);
        addProperty(Property.CACHE_MODE, "CACHE_MODE", "cache.mode", TempMode.MEMORY);

        addProperty(Property.REDIS_HOST, "REDIS_HOST", "redis.host", JedisFactory.DEFAULT_HOST);
        addProperty(Property.REDIS_PORT, "REDIS_PORT", "redis.port", JedisFactory.DEFAULT_PORT);
        addProperty(Property.REDIS_USER, "REDIS_USER", "redis.user", JedisFactory.DEFAULT_USER);
        addProperty(Property.REDIS_PASSWORD, "REDIS_PASSWORD", "redis.password", JedisFactory.DEFAULT_PASSWORD);

        addProperty(Property.ORIENTDB_MODE, "ORIENTDB_MODE", "orientdb.mode", OrientDbMode.MEMORY);
        addProperty(Property.ORIENTDB_LOCAL, "ORIENTDB_LOCAL", "orientdb.local", OrientGraphWrapper.DEFAULT_DIRECTORY);
        addProperty(Property.ORIENTDB_DATABASE, "ORIENTDB_DATABASE", "orientdb.database", OrientGraphWrapper.DEFAULT_DATABASE);
        addProperty(Property.ORIENTDB_HOST, "ORIENTDB_HOST", "orientdb.host", OrientGraphWrapper.DEFAULT_HOST);
        addProperty(Property.ORIENTDB_PORT, "ORIENTDB_PORT", "orientdb.port", OrientGraphWrapper.DEFAULT_PORT);
        addProperty(Property.ORIENTDB_USER, "ORIENTDB_USER", "orientdb.user", OrientGraphWrapper.DEFAULT_USER);
        addProperty(Property.ORIENTDB_PASSWORD, "ORIENTDB_PASSWORD", "orientdb.password", OrientGraphWrapper.DEFAULT_PASSWORD);

    }

    /**
     * Read the configuration file
     *
     * @return Properties object of the configuration file
     */
    private Properties readPropertiesFile() {

        Properties properties = new Properties();

        // Class execution
        InputStream inputStream = ConfigurationImpl.class.getResourceAsStream("/" + CONFIGURATION_FILE);

        // Jar execution
        if (inputStream == null) {
            try {
                URL path = new URL(ConfigurationImpl.class.getProtectionDomain().getCodeSource().getLocation(), CONFIGURATION_FILE);
                inputStream = new FileInputStream(path.getFile());
            } catch (FileNotFoundException | MalformedURLException e) {
                LOGGER.warn("[Configuration] No configuration file could be found");
                return null;
            }
        }

        // Parse properties
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("[Configuration] The configuration file could not be loaded");
        } finally {
            try {
                inputStream.close();
            } catch (IOException ignored) {
            }
        }

        return properties.isEmpty() ? null : properties;
    }

    private void addProperty(Property name, String envKey, String fileKey, String defaultValue) {
        properties.put(name, new ConfigurationStringProperty(configurationFile, name.name(), envKey, fileKey, defaultValue));
    }

    private void addProperty(Property name, String envKey, String fileKey, Integer defaultValue) {
        properties.put(name, new ConfigurationIntegerProperty(configurationFile, name.name(), envKey, fileKey, defaultValue));
    }


    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void setProperty(Property key, int value) {
        if (!properties.containsKey(key))
            throw new IllegalArgumentException("The property " + key + " does not exist");
        ConfigurationProperty property = properties.get(key);
        if (!(property instanceof ConfigurationIntegerProperty))
            throw new IllegalArgumentException("Trying to assign an Integer value to a non-integer property");
        property.setValue(value);
    }

    /** {@inheritDoc} */
    @Override
    @SuppressWarnings("unchecked")
    public void setProperty(Property key, String value) {
        if (!properties.containsKey(key))
            throw new IllegalArgumentException("The property " + key + " does not exist");
        ConfigurationProperty property = properties.get(key);
        if (!(property instanceof ConfigurationStringProperty))
            throw new IllegalArgumentException("Trying to assign a String value to a non-string property");
        property.setValue(value);
    }

    /** {@inheritDoc} */
    @Override
    public String getStringProperty(Property key) {
        if (!properties.containsKey(key))
            throw new IllegalArgumentException("The property " + key + " does not exist");
        ConfigurationProperty property = properties.get(key);
        if (!(property instanceof ConfigurationStringProperty))
            throw new IllegalArgumentException("Trying to assign a String value to a non-string property");
        return ((ConfigurationStringProperty) property).getValue();
    }

    /** {@inheritDoc} */
    @Override
    public int getIntegerProperty(Property key) {
        if (!properties.containsKey(key))
            throw new IllegalArgumentException("The property " + key + " does not exist");
        ConfigurationProperty property = properties.get(key);
        if (!(property instanceof ConfigurationIntegerProperty))
            throw new IllegalArgumentException("Trying to createOrUpdate an Integer value from a non-integer property");
        return ((ConfigurationIntegerProperty) property).getValue();
    }

}
