package io.reneses.tela.core.sessions.repositories;


import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.TestUtils;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.sessions.databases.extensions.SessionOrientDatabaseExtension;
import io.reneses.tela.core.sessions.models.Session;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

public class OrientSessionManagerRepositoryTest {

    private OrientSessionManagerRepository repository;
    private OrientGraphWrapper telaGraph;
    private OrientBaseGraph graph;

    @Before
    public void setUp() throws Exception {
        TestUtils.configureSessionManager();
        repository = new OrientSessionManagerRepository();
        telaGraph = OrientGraphWrapperFactory.get();
        graph = OrientGraphWrapperFactory.get().getNoTxGraph();
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.destroyDatabase();
    }

    @Test
    public void createWithoutModules() throws Exception {

        Session session = new Session();

        repository.create(session);

        List<OrientVertex> vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.SESSION_CLASS);
        assertEquals(1, vertices.size());
        assertEquals(session.getId(), vertices.get(0).getProperty(SessionOrientDatabaseExtension.SESSION_ID));
        assertEquals(session.getAccessToken(), vertices.get(0).getProperty(SessionOrientDatabaseExtension.SESSION_ACCESS_TOKEN));

        vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.TOKEN_CLASS);
        assertTrue(vertices.isEmpty());

    }

    @Test
    public void createWithModules() throws Exception {

        Session session = new Session();
        repository.create(session);
        repository.addModuleToken(session, "t1", "1");
        repository.addModuleToken(session, "t2", "2");

        List<OrientVertex> vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.SESSION_CLASS);
        assertEquals(1, vertices.size());
        assertEquals(session.getId(), vertices.get(0).getProperty(SessionOrientDatabaseExtension.SESSION_ID));
        assertEquals(session.getAccessToken(), vertices.get(0).getProperty(SessionOrientDatabaseExtension.SESSION_ACCESS_TOKEN));

        vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.TOKEN_CLASS);
        assertEquals(2, vertices.size());

    }

    @Test
    public void delete() throws Exception {
        Session session = new Session();
        repository.create(session);
        assertTrue(repository.delete(session));

        List<OrientVertex> vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.SESSION_CLASS);
        assertTrue(vertices.isEmpty());

        vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.TOKEN_CLASS);
        assertTrue(vertices.isEmpty());
    }

    @Test
    public void deleteWithTokens() throws Exception {

        Session session = new Session();
        repository.create(session);
        repository.addModuleToken(session, "t1", "1");
        repository.addModuleToken(session, "t2", "2");
        assertTrue(repository.delete(session));

        List<OrientVertex> vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.SESSION_CLASS);
        assertTrue(vertices.isEmpty());

        vertices = telaGraph.getVertices(graph, SessionOrientDatabaseExtension.TOKEN_CLASS);
        assertTrue(vertices.isEmpty());
    }

    @Test
    public void deleteNotExisting() throws Exception {
        Session session = new Session();
        assertFalse(repository.delete(session));
    }

    @Test
    public void findByAccessToken() throws Exception {
        Session session = new Session();
        repository.create(session);

        Session retrieved = repository.findByAccessToken(session.getAccessToken());
        assertNotNull(retrieved);
        assertEquals(session.getId(), retrieved.getId());

    }

    @Test
    public void findByAccessTokenWithTokens() throws Exception {
        Session session = new Session();
        repository.create(session);
        repository.addModuleToken(session, "t", "1");

        Session retrieved = repository.findByAccessToken(session.getAccessToken());
        assertNotNull(retrieved);
        assertEquals(session.getId(), retrieved.getId());
        assertEquals("1", retrieved.getToken("t"));

    }

    @Test
    public void findByAccessTokenNotExisting() throws Exception {
        Session session = new Session();
        assertNull(repository.findByAccessToken(session.getAccessToken()));
    }

    @Test
    public void existsByAccessToken() throws Exception {
        Session session = new Session();
        repository.create(session);
        assertTrue(repository.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void existsByAccessTokenNotExisting() throws Exception {
        Session session = new Session();
        assertFalse(repository.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void addModuleToken() throws Exception {
        Session session = new Session();
        repository.create(session);
        assertTrue(repository.addModuleToken(session, "t", "1"));
        assertNotNull(repository.findByModuleToken("t", "1"));
    }

    @Test
    public void addModuleTokenMultipleTokens() throws Exception {
        Session session = new Session();
        repository.create(session);
        assertTrue(repository.addModuleToken(session, "t", "1"));
        assertTrue(repository.addModuleToken(session, "s", "2"));
        assertNotNull(repository.findByModuleToken("t", "1"));
        assertNotNull(repository.findByModuleToken("s", "2"));
    }

    @Test
    public void addModuleTokenToNotExistingSession() throws Exception {
        Session session = new Session();
        assertFalse(repository.addModuleToken(session, "t1", "1"));
    }

    @Test
    public void findByModuleToken() throws Exception {
        Session session = new Session();
        repository.create(session);
        repository.addModuleToken(session, "t", "1");
        Session retrieved = repository.findByModuleToken("t", "1");
        assertNotNull(retrieved);
        assertEquals(session.getId(), retrieved.getId());
    }

    @Test
    public void findByModuleTokenNotExisting() throws Exception {
        Session retrieved = repository.findByModuleToken("t", "1");
        assertNull(retrieved);
    }

    @Test
    public void deleteModuleToken() throws Exception {
        Session session = new Session();
        repository.create(session);
        repository.addModuleToken(session, "t", "1");
        assertTrue(repository.deleteModuleToken(session, "t"));
        assertTrue(repository.existsByAccessToken(session.getAccessToken()));
    }

    @Test
    public void deleteModuleTokenNotExistingSession() throws Exception {
        Session session = new Session();
        assertFalse(repository.deleteModuleToken(session, "t"));
    }

    @Test
    public void deleteModuleTokenNotExistingToken() throws Exception {
        Session session = new Session();
        repository.create(session);
        assertFalse(repository.deleteModuleToken(session, "t"));
    }


}
