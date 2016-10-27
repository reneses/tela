package io.reneses.tela.core.sessions;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.sessions.exceptions.ModuleTokenNotFoundException;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import io.reneses.tela.core.sessions.models.Session;
import org.junit.*;

import static org.junit.Assert.*;


public class SessionManagerImplTest {

    SessionManager sessionManager;

    @Before
    public void setUp() throws Exception {
        sessionManager = TestUtils.configureSessionManager();
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.destroyDatabase();
    }

    @Test
    public void create() throws Exception {
        Session session = sessionManager.create();
        assertTrue(sessionManager.existsByAccessToken(session.getAccessToken()));
        assertNotNull(sessionManager.findByAccessToken(session.getAccessToken()));
    }

    @Test
    public void createWithModuleToken() throws Exception {
        sessionManager.createWithModuleToken("test", "1");
        assertNotNull(sessionManager.findByModuleToken("test", "1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithModuleTokenNullToken() throws Exception {
        sessionManager.createWithModuleToken("test", null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void createWithModuleTokenNullModule() throws Exception {
        sessionManager.createWithModuleToken(null, "1");
    }

    @Test
    public void existsByAccessToken() throws Exception {
        assertFalse(sessionManager.existsByAccessToken(new Session().getAccessToken()));
        Session session = sessionManager.create();
        assertTrue(sessionManager.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void existsByAccessTokenWithNull() throws Exception {
        assertFalse(sessionManager.existsByAccessToken(null));
    }

    @Test
    public void delete() throws Exception {
        Session session = sessionManager.create();
        sessionManager.delete(session);
        assertFalse(sessionManager.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void deleteWithToken() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        sessionManager.delete(session);
        assertFalse(sessionManager.existsByAccessToken(session.getAccessToken()));
        assertNull(sessionManager.findByModuleToken("test", "1"));
    }

    @Test
    public void getModuleToken() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        String token = sessionManager.getModuleToken(session, "test");
        assertEquals("1", token);
    }

    @Test(expected = SessionNotFoundException.class)
    public void getModuleTokenNotExistingSession() throws Exception {
        sessionManager.getModuleToken(new Session(), "test");
    }

    @Test(expected = ModuleTokenNotFoundException.class)
    public void getModuleTokenNotExistingToken() throws Exception {
        Session session = sessionManager.create();
        sessionManager.getModuleToken(session, "test");
    }

    @Test(expected = SessionNotFoundException.class)
    public void getModuleTokenAfterDeleteSession() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        sessionManager.delete(session);
        sessionManager.getModuleToken(session, "test");
    }

    @Test
    public void deleteModuleToken() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        assertTrue(sessionManager.deleteModuleToken(session, "test"));
        assertTrue(sessionManager.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void deleteModuleTokenNotSessionFound() throws Exception {
        assertFalse(sessionManager.deleteModuleToken(new Session(), "test"));
    }

    @Test
    public void deleteModuleTokenNotExisting() throws Exception {
        Session session = sessionManager.create();
        assertFalse(sessionManager.deleteModuleToken(session, "test"));
    }

    @Test
    public void deleteModuleTokenWithOtherTokens() throws Exception {
        Session session = sessionManager.create();
        sessionManager.addToken(session, "test", "1");
        sessionManager.addToken(session, "test2", "2");
        sessionManager.deleteModuleToken(session, "test");
        assertTrue(sessionManager.existsByAccessToken(session.getAccessToken()));
        assertEquals("2", sessionManager.getModuleToken(session, "test2"));
    }

    @Test
    public void findByModuleByToken() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        Session retrieved = sessionManager.findByModuleToken("test", "1");
        assertEquals(session.getId(), retrieved.getId());
    }

    @Test
    public void findByModuleByTokenNotExisting() throws Exception {
        assertNull(sessionManager.findByModuleToken("test", "1"));
    }

    @Test
    public void deleteByModuleToken() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        assertTrue(sessionManager.deleteByModuleToken("test", "1"));
        assertFalse(sessionManager.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void deleteByModuleTokenNotExistingToken() throws Exception {
        assertFalse(sessionManager.deleteByModuleToken("test", "1"));
    }

    @Test
    public void deleteByModuleTokenOtherTokens() throws Exception {
        Session session = sessionManager.createWithModuleToken("test", "1");
        sessionManager.addToken(session, "test2", "2");
        assertTrue(sessionManager.deleteByModuleToken("test", "1"));
        assertFalse(sessionManager.existsByAccessToken(session.getAccessToken()));
    }

    @Test(expected = SessionNotFoundException.class)
    public void addTokenSessionNotExisting() throws Exception {
        sessionManager.addToken(new Session(), "test", "2");
    }

    @Test
    public void addTokenSession() throws Exception {
        Session session = sessionManager.create();
        sessionManager.addToken(session, "test", "2");
        assertEquals("2", sessionManager.getModuleToken(session, "test"));
        assertEquals(session, sessionManager.findByModuleToken("test", "2"));
    }

    @Test
    public void addTokenAlreadyExisting() throws Exception {
        Session s1 = sessionManager.createWithModuleToken("test", "1");
        Session s2 = sessionManager.create();
        sessionManager.addToken(s2, "test", "1");
        assertTrue(sessionManager.existsByAccessToken(s1.getAccessToken()));
        try {
            sessionManager.getModuleToken(s1, "test");
            Assert.fail();
        } catch (ModuleTokenNotFoundException ignored) {
        }
        assertEquals("1", sessionManager.getModuleToken(s2, "test"));
        assertEquals(s2, sessionManager.findByModuleToken("test", "1"));
    }

}