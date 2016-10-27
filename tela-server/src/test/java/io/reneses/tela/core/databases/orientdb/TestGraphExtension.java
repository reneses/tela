package io.reneses.tela.core.databases.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;

import java.util.HashMap;
import java.util.Map;

public class TestGraphExtension extends OrientDatabaseExtension {

    public static String CLASS = "People", ID = "person_id", NAME = "name", AGE = "age", CALLS = "calls";

    @Override
    public boolean isInitiated(OrientGraphWrapper telaGraph) {
        OrientBaseGraph graph = telaGraph.getNoTxGraph();
        boolean isInitiated =  telaGraph.existsClass(graph, CLASS);
        graph.shutdown();
        return isInitiated;
    }

    @Override
    public void init(OrientGraphWrapper telaGraph) {

        OrientBaseGraph graph = telaGraph.getNoTxGraph();

        // Create vertex
        Map<String, OType> properties = new HashMap<>();
        properties.put(ID, OType.STRING);
        properties.put(NAME, OType.STRING);
        properties.put(AGE, OType.INTEGER);
        telaGraph.createVertexClass(graph, CLASS, properties);

        // Create indexes
        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(ID, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        telaGraph.createIndexes(graph, CLASS, indexes);

        // Create edge
        telaGraph.createEdgeClass(graph, CALLS);

        graph.shutdown();

    }
}
