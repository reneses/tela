package io.reneses.tela.core.sessions.databases.extensions;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * OrientDB extension for the scheduler
 */
public class SessionOrientDatabaseExtension extends OrientDatabaseExtension {

    public static final String
            SESSION_CLASS = "Session", SESSION_ID = "session_id", SESSION_ACCESS_TOKEN = "access_token",
            TOKEN_CLASS = "Token", TOKEN_TOKEN = "token", TOKEN_MODULE = "module",
            SESSION_HAS_TOKEN = "HasToken";

    /** {@inheritDoc} */
    @Override
    public boolean isInitiated(OrientGraphWrapper telaGraph) {
        OrientBaseGraph graph = telaGraph.getNoTxGraph();
        boolean isInitiated = telaGraph.existsClass(graph, SESSION_CLASS);
        graph.shutdown();
        return isInitiated;
    }

    /** {@inheritDoc} */
    @Override
    public void init(OrientGraphWrapper telaGraph) {

        OrientBaseGraph graph = telaGraph.getNoTxGraph();

        // Create session vertex
        Map<String, OType> properties = new HashMap<>();
        properties.put(SESSION_ID, OType.STRING);
        properties.put(SESSION_ACCESS_TOKEN, OType.STRING);
        telaGraph.createVertexClass(graph, SESSION_CLASS, properties);

        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(SESSION_ID, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        indexes.put(SESSION_ACCESS_TOKEN, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        telaGraph.createIndexes(graph, SESSION_CLASS, indexes);

        // Create token vertex
        Map<String, OType> paramsProperties = new HashMap<>();
        paramsProperties.put(TOKEN_TOKEN, OType.STRING);
        paramsProperties.put(TOKEN_MODULE, OType.STRING);
        telaGraph.createVertexClass(graph, TOKEN_CLASS, paramsProperties);

        Map<String, OClass.INDEX_TYPE> tokenIndexes = new HashMap<>();
        tokenIndexes.put(TOKEN_TOKEN, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        telaGraph.createIndexes(graph, TOKEN_CLASS, tokenIndexes);

        // Edge
        telaGraph.createEdgeClass(graph, SESSION_HAS_TOKEN);

        graph.shutdown();
    }

}
