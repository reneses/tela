package io.reneses.tela.core.api.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class TelaServerFactoryTest {
    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {
        TelaServer server = TelaServerFactory.create(8888);
        assertNotNull(server);
    }

    @Test
    public void createCorrectPort() throws Exception {
        TelaServer server = TelaServerFactory.create(8888);
        assertEquals(8888, server.getPort());
    }

}