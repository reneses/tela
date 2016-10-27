package io.reneses.tela.core.api.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.HttpTestClient;
import io.reneses.tela.TestActions;
import io.reneses.tela.TestUtils;
import io.reneses.tela.core.api.responses.ScheduledActionResponse;
import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.api.server.TelaServerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.scheduler.Scheduler;
import io.reneses.tela.core.scheduler.SchedulerFactory;
import io.reneses.tela.core.scheduler.databases.extensions.SchedulerOrientDatabaseExtension;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.databases.extensions.SessionOrientDatabaseExtension;
import io.reneses.tela.core.sessions.models.Session;
import org.eclipse.jetty.client.api.ContentResponse;
import org.junit.*;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

public class SchedulerControllerTest {

    private static HttpTestClient httpClient;
    private static TelaServer server;
    private static ActionDispatcher dispatcher;
    private static SessionManager sessionManager;

    private Scheduler scheduler;

    private Session session1, session2;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        sessionManager = TestUtils.configureSessionManager();
        dispatcher = TestUtils.configureActionDispatcher(TestActions.class);
        SchedulerFactory.setDelay(5);
        TestUtils.configureComponents(sessionManager, dispatcher, SchedulerFactory.create(dispatcher, sessionManager));

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
        OrientGraphWrapperFactory.registerExtensions(new SchedulerOrientDatabaseExtension(), new SessionOrientDatabaseExtension());
        sessionManager = TestUtils.configureSessionManager();
        scheduler = SchedulerFactory.create(dispatcher, sessionManager);
        session1 = sessionManager.create();
        session2 = sessionManager.create();
    }

    @After
    public void tearDown() throws Exception {
        scheduler.close();
        TestUtils.destroyDatabase();
    }

    @Test
    public void schedule() throws Exception {

        LocalDateTime t1 = LocalDateTime.now();
        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello?delay=10");
        LocalDateTime t2 = LocalDateTime.now();

        assertEquals(200, httpResponse.getStatus());

        ScheduledActionResponse response = new ObjectMapper().readValue(httpResponse.getContentAsString(), ScheduledActionResponse.class);

        assertEquals("world", response.getResult());
        assertEquals(10, response.getAction().getDelay());
        assertEquals(session1.getAccessToken(), response.getAction().getAccessToken());
        assertEquals("test", response.getAction().getModuleName());
        assertEquals("hello", response.getAction().getActionName());
        assertEquals(session1.getAccessToken(), response.getAction().getAccessToken());
        assertTrue(response.getAction().getParams().isEmpty());
        assertNotNull(response.getAction().getId());

        assertTrue(response.getAction().getCreatedAt().isAfter(t1));
        assertTrue(response.getAction().getCreatedAt().isBefore(t2));
        assertEquals(response.getAction().getCreatedAt().plusSeconds(10), response.getAction().getNextExecution());

    }

    @Test
    public void scheduleWithoutDelay() throws Exception {
        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello/");
        assertEquals(200, httpResponse.getStatus());
        ScheduledActionResponse response = new ObjectMapper().readValue(httpResponse.getContentAsString(), ScheduledActionResponse.class);
        assertEquals(response.getAction().getDelay(), 1); // Default delay for hello action
    }

    @Test
    public void scheduleWithParams() throws Exception {
        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/negate?delay=10&n=5");
        assertEquals(200, httpResponse.getStatus());
        ScheduledActionResponse response = new ObjectMapper().readValue(httpResponse.getContentAsString(), ScheduledActionResponse.class);
        assertEquals(-5, response.getResult());
    }

    @Test
    public void scheduleWithoutRequiredParam() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(new Session(), "/schedule/test/negate/?delay=10");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void scheduleWithInvalidParamType() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session1, "/schedule/test/error/?delay=10&n=5");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void scheduleWithInvalidDelay() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello/?delay=aaa");
        assertEquals(422, response.getStatus());
    }

    @Test
    public void scheduleWithoutSession() throws Exception {
        ContentResponse response = httpClient.getRequest("/schedule/test/hello/?delay=10");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void scheduleWithInvalidSession() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(new Session(), "/schedule/test/hello/?delay=10");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void scheduleWithActionNotDefined() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session1, "/schedule/test/error/?delay=10");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void scheduleWithActionNotSchedulable() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(session1, "/schedule/test/bye/?delay=10");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void scheduleRepeated() throws Exception {

        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello?delay=10");
        assertEquals(200, httpResponse.getStatus());
        httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        List<ScheduledAction> actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertEquals(1, actions.size());

        httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello/?delay=10");
        assertEquals(409, httpResponse.getStatus());
        httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertEquals(1, actions.size());


    }

    @Test
    public void getScheduled() throws Exception {

        httpClient.getAuthorizedRequest(session1, "/schedule/test/hello?delay=10");

        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        assertEquals(200, httpResponse.getStatus());
        List<ScheduledAction> actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertFalse(actions.isEmpty());
        assertEquals(1, actions.size());

        assertEquals(10, actions.get(0).getDelay());
        assertEquals(session1.getAccessToken(), actions.get(0).getAccessToken());
        assertEquals("test", actions.get(0).getModuleName());
        assertEquals("hello", actions.get(0).getActionName());
        assertEquals(session1.getAccessToken(), actions.get(0).getAccessToken());
        assertTrue(actions.get(0).getParams().isEmpty());
        assertNotNull(actions.get(0).getId());

    }
    @Test
    public void getScheduledEmpty() throws Exception {

        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        assertEquals(200, httpResponse.getStatus());
        List<ScheduledAction> actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertTrue(actions.isEmpty());

    }

    @Test
    public void getScheduledWithoutSession() throws Exception {
        ContentResponse response = httpClient.getRequest("/schedule/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void getScheduledWithInvalidSession() throws Exception {
        ContentResponse response = httpClient.getAuthorizedRequest(new Session(), "/schedule/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void cancel() throws Exception {

        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello/?delay=10");
        ScheduledActionResponse response = new ObjectMapper().readValue(httpResponse.getContentAsString(), ScheduledActionResponse.class);
        ScheduledAction scheduledAction = response.getAction();

        httpResponse = httpClient.deleteAuthorizedRequest(session1, "/schedule/" + scheduledAction.getId());
        assertEquals(200, httpResponse.getStatus());

        httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        List<ScheduledAction> actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertTrue(actions.isEmpty());

    }

    @Test
    public void cancelWithoutSession() throws Exception {
        ContentResponse response = httpClient.deleteRequest("/schedule/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void cancelWithInvalidSession() throws Exception {
        ContentResponse response = httpClient.deleteAuthorizedRequest(new Session(), "/schedule/2/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void cancelWithInvalidAction() throws Exception {
        ContentResponse response = httpClient.deleteAuthorizedRequest(session1, "/schedule/1");
        assertEquals(404, response.getStatus());
    }

    @Test
    public void cancelForbidden() throws Exception {

        ContentResponse httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/test/hello/?delay=10");
        ScheduledActionResponse response = new ObjectMapper().readValue(httpResponse.getContentAsString(), ScheduledActionResponse.class);
        ScheduledAction scheduledAction = response.getAction();

        httpResponse = httpClient.deleteAuthorizedRequest(session2, "/schedule/" + scheduledAction.getId());
        assertEquals(403, httpResponse.getStatus());

        httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        List<ScheduledAction> actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertFalse(actions.isEmpty());
        assertEquals(scheduledAction.getId(), actions.get(0).getId());

    }

    @Test
    public void cancelAll() throws Exception {

        httpClient.getAuthorizedRequest(session1, "/schedule/test/hello/?delay=10");
        httpClient.getAuthorizedRequest(session2, "/schedule/test/hello/?delay=10");

        ContentResponse httpResponse = httpClient.deleteAuthorizedRequest(session1, "/schedule/");
        assertEquals(200, httpResponse.getStatus());

        httpResponse = httpClient.getAuthorizedRequest(session1, "/schedule/");
        List<ScheduledAction> actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertTrue(actions.isEmpty());

        httpResponse = httpClient.getAuthorizedRequest(session2, "/schedule/");
        actions = new ObjectMapper().readValue(httpResponse.getContentAsString(), new TypeReference<List<ScheduledAction>>() {
        });
        assertFalse(actions.isEmpty());

    }

    @Test
    public void cancelAllWithoutSession() throws Exception {
        ContentResponse response = httpClient.deleteRequest("/schedule/");
        assertEquals(403, response.getStatus());
    }

    @Test
    public void cancelAllWithInvalidSession() throws Exception {
        ContentResponse response = httpClient.deleteAuthorizedRequest(new Session(), "/schedule/");
        assertEquals(403, response.getStatus());
    }

}