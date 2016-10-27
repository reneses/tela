package io.reneses.tela.core.configuration;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;

public class ConfigurationImplTest {

    private ConfigurationImpl config;

    private void setPortPropertyEnv(int port) {
        ConfigurationProperty property = config.properties.get(Configuration.Property.PORT);
        property = Mockito.spy(property);
        config.properties.put(Configuration.Property.PORT, property);
        Mockito.when(property.getEnvValue()).thenReturn(port);
    }
    private void setTempModePropertyEnv(String tempMode) {
        ConfigurationProperty property = config.properties.get(Configuration.Property.CACHE_MODE);
        property = Mockito.spy(property);
        config.properties.put(Configuration.Property.CACHE_MODE, property);
        Mockito.when(property.getEnvValue()).thenReturn(tempMode);
    }

    @Before
    public void setUp() throws Exception {
        config = new ConfigurationImpl();
    }

    @Test
    public void getStringPropertyFile() throws Exception {
        assertEquals("memory", config.getStringProperty(Configuration.Property.CACHE_MODE));
    }

    @Test
    public void getIntPropertyFile() throws Exception {
        assertEquals(8080, config.getIntegerProperty(Configuration.Property.PORT));
    }

    @Test
    public void getIntegerPropertyEnv() throws Exception {
        setPortPropertyEnv(1234);
        assertEquals(1234, config.getIntegerProperty(Configuration.Property.PORT));
    }

    @Test
    public void getStringPropertyEnv() throws Exception {
        setTempModePropertyEnv("hello");
        assertEquals("hello", config.getStringProperty(Configuration.Property.CACHE_MODE));
    }

    @Test
    public void getIntegerPropertyProgrammatically() throws Exception {
        setPortPropertyEnv(1234);
        config.setProperty(Configuration.Property.PORT, 5678);
        assertEquals(5678, config.getIntegerProperty(Configuration.Property.PORT));
    }

    @Test
    public void getStringPropertyProgrammatically() throws Exception {
        setTempModePropertyEnv("world");
        assertEquals("world", config.getStringProperty(Configuration.Property.CACHE_MODE));
    }

}