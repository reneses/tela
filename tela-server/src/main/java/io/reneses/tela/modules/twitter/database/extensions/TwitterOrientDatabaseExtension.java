package io.reneses.tela.modules.twitter.database.extensions;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;

import java.util.HashMap;
import java.util.Map;

/**
 * Twitter extension for the Orient Database
 */
public class TwitterOrientDatabaseExtension extends OrientDatabaseExtension {

    public static class User {
        public static final String
                CLASS = "TwitterUser",      // Vertex class
                FOLLOWS = "TwitterFollows"; // Edge class
        public static final String
                ID = "user_id", NAME = "name", SCREEN_NAME = "screen_name", PICTURE = "picture", URL = "url",
                NUMBER_OF_FOLLOWERS = "n_followers", NUMBER_OF_FOLLOWING = "n_following";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isInitiated(OrientGraphWrapper telaGraph) {
        OrientBaseGraph graph = telaGraph.getNoTxGraph();
        boolean isInitiated = telaGraph.existsClass(graph, User.CLASS) && telaGraph.existsClass(graph, User.FOLLOWS);
        graph.shutdown();
        return isInitiated;
    }

    /** {@inheritDoc} */
    @Override
    public void init(OrientGraphWrapper telaGraph) {

        OrientBaseGraph graph = telaGraph.getNoTxGraph();

        // Create user vertex
        Map<String, OType> properties = new HashMap<>();
        properties.put(User.ID, OType.LONG);
        properties.put(User.NAME, OType.STRING);
        properties.put(User.SCREEN_NAME, OType.STRING);
        properties.put(User.PICTURE, OType.STRING);
        properties.put(User.URL, OType.STRING);
        properties.put(User.NUMBER_OF_FOLLOWERS, OType.INTEGER);
        properties.put(User.NUMBER_OF_FOLLOWING, OType.INTEGER);
        telaGraph.createVertexClass(graph, User.CLASS, properties);

        // Create user indexes
        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(User.ID, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        indexes.put(User.SCREEN_NAME, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        telaGraph.createIndexes(graph, User.CLASS, indexes);

        // EDGE
        telaGraph.createEdgeClass(graph, User.FOLLOWS, properties);

        graph.shutdown();

    }

}
