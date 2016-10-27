package io.reneses.tela.core.api.controllers;

import io.reneses.tela.HttpTestClient;
import io.reneses.tela.TestActions;
import io.reneses.tela.TestUtils;
import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.api.server.TelaServerFactory;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.models.Session;
import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.*;

import static org.junit.Assert.*;


public class ActionControllerTest {

    private static HttpTestClient httpClient;
    private static TelaServer server;

    private static Session session;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        SessionManager sessionManager = TestUtils.configureSessionManager();
        session = sessionManager.create();
        ActionDispatcher dispatcher = TestUtils.configureActionDispatcher(TestActions.class);
        TestUtils.configureComponents(sessionManager, dispatcher, null);

        server = TelaServerFactory.create(8082).start();
        httpClient = new HttpTestClient(server.getPort()).start();

    }

    @AfterClass
    public static void tearDowAfterClass() throws Exception {
        httpClient.stop();
        server.stop();
        TestUtils.destroyDatabase();
    }

    @Test
    public void execute() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/hello/");
        assertEquals(200, response.getStatus());
        assertEquals("world", response.getContentAsString());
    }

    @Test
    public void executeWithParameters() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/negate?n=5");
        assertEquals(200, response.getStatus());
        assertEquals("-5", response.getContentAsString());
    }

    @Test
    public void executeWithArrayParam() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/implode?word=hola&word=que&word=tal");
        assertEquals(200, response.getStatus());
        assertEquals("holaquetal", response.getContentAsString());
    }

    @Test
    public void executeWithInvalidTypeParams() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/negate?n=hello");
        assertEquals(422, response.getStatus());
    }

    @Test
    public void executeWithMissingParams() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/negate");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void executeWithInvalidParams() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/negate?param=5");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void executeWithoutSession() throws Exception {
        ContentResponse response = httpClient.getRequest("/action/test/hello/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void executeWithNotExistingModule() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test2/hello/");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void executeWithNotExistingAction() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session, "/action/test/not/");
        assertEquals(404, response.getStatus());
    }

}