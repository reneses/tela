package io.reneses.tela.core.sessions.repositories;

import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.sessions.databases.extensions.SessionOrientDatabaseExtension;
import io.reneses.tela.core.sessions.models.Session;

import java.util.*;

/**
 * AbstractSchedulerRepository implementation using OrientDB
 */
public class OrientSessionManagerRepository implements AbstractSessionManagerRepository {

    private OrientGraphWrapper telaGraph;

    /**
     * Constructor for OrientSessionManagerRepository.
     */
    public OrientSessionManagerRepository() {
        telaGraph = OrientGraphWrapperFactory.get();
    }

    Session mapVertex(Vertex sessionVertex) {
        String sessionId = sessionVertex.getProperty(SessionOrientDatabaseExtension.SESSION_ID);
        String accessToken = sessionVertex.getProperty(SessionOrientDatabaseExtension.SESSION_ACCESS_TOKEN);
        Map<String, String> tokens = new HashMap<>();
        sessionVertex.getVertices(Direction.OUT, SessionOrientDatabaseExtension.SESSION_HAS_TOKEN).forEach(v -> {
            String module = v.getProperty(SessionOrientDatabaseExtension.TOKEN_MODULE);
            String token = v.getProperty(SessionOrientDatabaseExtension.TOKEN_TOKEN);
            tokens.put(module, token);
        });
        return new Session(sessionId, accessToken, tokens);
    }

    private void addModuleTokenVertex(OrientGraphWrapper telaGraph, OrientBaseGraph graph, OrientVertex sessionVertex, String module, String token) {
        Map<String, Object> tokenProperties = new HashMap<>();
        tokenProperties.put(SessionOrientDatabaseExtension.TOKEN_MODULE, module);
        tokenProperties.put(SessionOrientDatabaseExtension.TOKEN_TOKEN, token);
        OrientVertex tokenVertex = telaGraph.addVertex(graph, SessionOrientDatabaseExtension.TOKEN_CLASS, tokenProperties);
        telaGraph.addEdge(graph, SessionOrientDatabaseExtension.SESSION_HAS_TOKEN, sessionVertex, tokenVertex);
    }


    //----------------------------------- REPOSITORY FUNCTIONS -----------------------------------//

    /** {@inheritDoc} */
    @Override
    public void create(Session session) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        try {
            Map<String, Object> properties = new HashMap<>();
            properties.put(SessionOrientDatabaseExtension.SESSION_ID, session.getId());
            properties.put(SessionOrientDatabaseExtension.SESSION_ACCESS_TOKEN, session.getAccessToken());
            OrientVertex sessionVertex = telaGraph.addVertex(graph, SessionOrientDatabaseExtension.SESSION_CLASS, properties);
            session.getTokens().forEach((module, token) ->
                    addModuleTokenVertex(telaGraph, graph, sessionVertex, module, token));
        } catch (ORecordDuplicatedException ignored) {
        } finally {
            graph.shutdown();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean delete(Session session) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, SessionOrientDatabaseExtension.SESSION_CLASS, SessionOrientDatabaseExtension.SESSION_ID, session.getId());
        if (vertex == null)
            return false;
        vertex.getVertices(Direction.OUT).forEach(Element::remove);
        vertex.remove();
        graph.shutdown();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public Session findByAccessToken(String accessToken) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        Vertex sessionVertex = telaGraph.getVertex(graph, SessionOrientDatabaseExtension.SESSION_CLASS, SessionOrientDatabaseExtension.SESSION_ACCESS_TOKEN, accessToken);
        Session session = sessionVertex == null ? null : mapVertex(sessionVertex);
        graph.shutdown();
        return session;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByAccessToken(String accessToken) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        boolean exists = telaGraph.getVertex(graph,
                SessionOrientDatabaseExtension.SESSION_CLASS, SessionOrientDatabaseExtension.SESSION_ACCESS_TOKEN, accessToken) != null;
        graph.shutdown();
        return exists;
    }

    /** {@inheritDoc} */
    @Override
    public Session findByModuleToken(String module, String token) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex tokenVertex = telaGraph.getVertex(graph, SessionOrientDatabaseExtension.TOKEN_CLASS, SessionOrientDatabaseExtension.TOKEN_TOKEN, token);
        if (tokenVertex == null)
            return null;

        Iterator<Vertex> iterator = tokenVertex.getVertices(Direction.IN, SessionOrientDatabaseExtension.SESSION_HAS_TOKEN).iterator();

        // Check there is a session associated, otherwise the integrity has been corrupted
        if (!iterator.hasNext()) {
            tokenVertex.remove();
            return null;
        }

        Session session = mapVertex(iterator.next());
        graph.shutdown();
        return session;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteModuleToken(Session session, String module) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        Vertex sessionVertex = telaGraph.getVertex(graph, SessionOrientDatabaseExtension.SESSION_CLASS, SessionOrientDatabaseExtension.SESSION_ID, session.getId());
        if (sessionVertex == null)
            return false;
        boolean removed = false;
        for (Vertex v : sessionVertex.getVertices(Direction.OUT, SessionOrientDatabaseExtension.SESSION_HAS_TOKEN)) {
            String m = v.getProperty(SessionOrientDatabaseExtension.TOKEN_MODULE);
            if (m.equals(module)) {
                v.remove();
                removed = true;
                break;
            }
        }
        graph.shutdown();
        return removed;
    }

    /** {@inheritDoc} */
    @Override
    public boolean addModuleToken(Session session, String module, String token) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, SessionOrientDatabaseExtension.SESSION_CLASS, SessionOrientDatabaseExtension.SESSION_ID, session.getId());
        if (vertex == null)
            return false;
        addModuleTokenVertex(telaGraph, graph, vertex, module, token);
        graph.shutdown();
        return true;
    }

}
