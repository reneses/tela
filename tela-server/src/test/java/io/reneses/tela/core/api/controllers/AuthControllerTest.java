package io.reneses.tela.core.api.controllers;

import io.reneses.tela.HttpTestClient;
import io.reneses.tela.TestActions;
import io.reneses.tela.TestUtils;
import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.api.server.TelaServerFactory;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.models.Session;
import io.reneses.tela.core.sessions.exceptions.ModuleTokenNotFoundException;
import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.*;

import static org.junit.Assert.*;


public class AuthControllerTest {

    private static HttpTestClient httpClient;
    private static TelaServer server;
    private SessionManager sessionManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        SessionManager sessionManager = TestUtils.configureSessionManager();
        ActionDispatcher dispatcher = TestUtils.configureActionDispatcher(TestActions.class);
        TestUtils.configureComponents(sessionManager, dispatcher, null);

        server = TelaServerFactory.create(8082).start();
        httpClient = new HttpTestClient(server.getPort()).start();

    }

    @AfterClass
    public static void tearDowAfterClass() throws Exception {
        httpClient.stop();
        server.stop();
    }

    @Before
    public void setUp() throws Exception {
        sessionManager = TestUtils.configureSessionManager();
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.destroyDatabase();
    }

    //------------------------------------------ CREATE ------------------------------------------//

    @Test
    public void testCreate() throws Exception {

        ContentResponse httpResponse = httpClient.postRequest("/auth");
        assertEquals(200, httpResponse.getStatus());

        String accessToken = httpResponse.getContentAsString();
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
        assertTrue(sessionManager.existsByAccessToken(accessToken));
        try {
            sessionManager.getModuleToken(sessionManager.findByAccessToken(accessToken), "test");
            Assert.fail();
        } catch (ModuleTokenNotFoundException ignored) {
        }

    }

    @Test
    public void testCreateWithModuleToken() throws Exception {

        ContentResponse httpResponse = httpClient.postRequest("/auth", "module", "test", "token", "1");
        assertEquals(200, httpResponse.getStatus());

        String accessToken = httpResponse.getContentAsString();
        assertNotNull(accessToken);
        assertFalse(accessToken.isEmpty());
        assertTrue(sessionManager.existsByAccessToken(accessToken));

        String token = sessionManager.getModuleToken(sessionManager.findByAccessToken(accessToken), "test");
        assertEquals("1", token);

    }

    @Test
    public void testCreateWithoutModule() throws Exception {
        ContentResponse httpResponse = httpClient.postRequest("/auth", "token", "1");
        assertEquals(422, httpResponse.getStatus());
    }

    @Test
    public void testCreateWithEmptyModule() throws Exception {
        ContentResponse httpResponse = httpClient.postRequest("/auth", "module", "", "token", "1");
        assertEquals(422, httpResponse.getStatus());
    }

    @Test
    public void testCreateWithNotExistingModule() throws Exception {
        ContentResponse httpResponse = httpClient.postRequest("/auth", "module", "not", "token", "1");
        assertEquals(404, httpResponse.getStatus());
    }

    @Test
    public void testCreateWithoutToken() throws Exception {
        ContentResponse httpResponse = httpClient.postRequest("/auth", "module", "not");
        assertEquals(422, httpResponse.getStatus());
    }

    @Test
    public void testCreateWithEmptyToken() throws Exception {
        ContentResponse httpResponse = httpClient.postRequest("/auth", "module", "not", "token", "");
        assertEquals(422, httpResponse.getStatus());
    }

    @Test
    public void testAddModuleToken() throws Exception {

        Session session = sessionManager.create();
        ContentResponse httpResponse = httpClient.postAuthorizedRequest(session, "/auth/test", "token", "1");

        assertEquals(200, httpResponse.getStatus());
        assertEquals("1", sessionManager.getModuleToken(session, "test"));

    }

    @Test
    public void testAddModuleTokenWithoutToken() throws Exception {
        Session session = sessionManager.create();
        ContentResponse httpResponse = httpClient.postAuthorizedRequest(session, "/auth/test");
        assertEquals(422, httpResponse.getStatus());
    }

    @Test
    public void testAddModuleTokenWithEmptyToken() throws Exception {
        Session session = sessionManager.create();
        ContentResponse httpResponse = httpClient.postAuthorizedRequest(session, "/auth/test", "token", "");
        assertEquals(422, httpResponse.getStatus());
    }

    //------------------------------------------ DELETE ------------------------------------------//

    @Test
    public void testDeleteNotExisting() throws Exception {
        ContentResponse response = httpClient.deleteAuthorizedRequest(new Session(), "/auth/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testDelete() throws Exception {
        Session session = sessionManager.create();
        ContentResponse response = httpClient.deleteAuthorizedRequest(session, "/auth/");
        assertEquals(200, response.getStatus());
        assertFalse(sessionManager.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void testDeleteTokenBadSession() throws Exception {
        ContentResponse response = httpClient.deleteAuthorizedRequest(new Session(), "/auth/test");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void testDeleteTokenNotExisting() throws Exception {
        Session session = sessionManager.create();
        ContentResponse response = httpClient.deleteRequest("/auth/" + session + "/test");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void testDeleteToken() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        ContentResponse response = httpClient.deleteAuthorizedRequest(session, "/auth/test");
        assertEquals(200, response.getStatus());
        assertTrue(sessionManager.existsByAccessToken(session.getAccessToken()));
        assertNull(sessionManager.findByModuleToken("test", "1"));
    }

}