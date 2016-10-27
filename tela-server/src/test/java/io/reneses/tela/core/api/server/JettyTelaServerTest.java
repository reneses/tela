package io.reneses.tela.core.api.server;

import io.reneses.tela.HttpTestClient;
import io.reneses.tela.TestUtils;
import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.*;

import static org.junit.Assert.*;


public class JettyTelaServerTest {

    static HttpTestClient httpClient;
    TelaServer server;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        httpClient = new HttpTestClient(8082).start();
        TestUtils.configureComponents(null, null, null);
    }

    @Before
    public void setUp() throws Exception {
        server = new JettyTelaServer(8082).start();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        httpClient.stop();
    }

    @Test
    public void start() throws Exception {
        assertTrue(server.isRunning());
        ContentResponse response = httpClient.getRequest("test");
        assertEquals(200, response.getStatus());
        assertEquals("OK", response.getContentAsString());
    }

    @Test
    public void error404() throws Exception {
        ContentResponse response = httpClient.getRequest("endpoint/not/found");
        assertEquals(404, response.getStatus());
        assertEquals("Not Found", response.getContentAsString());
    }
}