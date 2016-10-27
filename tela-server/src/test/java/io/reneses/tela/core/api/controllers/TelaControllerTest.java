package io.reneses.tela.core.api.controllers;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.api.exceptions.AuthorizationApiException;
import io.reneses.tela.core.api.exceptions.ParameterException;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import io.reneses.tela.core.sessions.models.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TelaControllerTest {

    private static Session session;
    private static TelaController telaController;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        SessionManager sessionManager = TestUtils.configureSessionManager();
        session = sessionManager.create();
        TestUtils.configureComponents(sessionManager, null, null);
        telaController = new GeneralController();
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        TestUtils.destroyDatabase();
    }

    @Test
    public void extractSession() throws Exception {
        String authHeader = "Bearer " + session.getAccessToken();
        Session extracted = telaController.extractSession(authHeader);
        assertEquals(session, extracted);
    }

    @Test(expected = AuthorizationApiException.class)
    public void extractSessionInvalidMethod() throws Exception {
        String authHeader = "Simple 123456789";
        telaController.extractSession(authHeader);
    }

    @Test(expected = AuthorizationApiException.class)
    public void extractEmptyHeader() throws Exception {
        String authHeader = "";
        telaController.extractSession(authHeader);
    }

    @Test(expected = SessionNotFoundException.class)
    public void extractSessionNotFound() throws Exception {
        String authHeader = "Bearer a";
        telaController.extractSession(authHeader);
    }

    @Test
    public void getParameter() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"a"});
        String param = TelaController.getParameter(params, "p1", null);
        assertNotNull(param);
        assertEquals("a", param);
    }

    @Test
    public void getParameterFirstOfArray() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"a", "b"});
        String param = TelaController.getParameter(params, "p1", null);
        assertNotNull(param);
        assertEquals("a", param);
    }

    @Test
    public void getParameterNotExists() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{});
        String param = TelaController.getParameter(params, "p1", null);
        assertNull(param);
    }

    @Test
    public void getParameterEmpty() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{""});
        String param = TelaController.getParameter(params, "p1", null);
        assertNull(param);
    }

    @Test
    public void getIntParameter() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"3"});
        int param = TelaController.getIntParameter(params, "p1", null);
        assertNotNull(param);
        assertEquals(3, param);
    }

    @Test
    public void getIntParameterFirstOfArray() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"3", "4"});
        int param = TelaController.getIntParameter(params, "p1", null);
        assertNotNull(param);
        assertEquals(3, param);
    }

    @Test
    public void getIntParameterNotExists() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{});
        Integer param = TelaController.getIntParameter(params, "p1", null);
        assertNull(param);
    }

    @Test
    public void getIntParameterEmpty() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{""});
        Integer param = TelaController.getIntParameter(params, "p1", null);
        assertNull(param);
    }

    @Test(expected = ParameterException.class)
    public void getIntParameterNoInteger() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"a"});
        TelaController.getIntParameter(params, "p1", null);
    }

}