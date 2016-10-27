package io.reneses.tela.core.scheduler.databases.extensions;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.OrientBaseGraph;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;

import java.util.HashMap;
import java.util.Map;

/**
 * OrientDB extension for the scheduler
 */
public class SchedulerOrientDatabaseExtension extends OrientDatabaseExtension {

    public static final String TASK_CLASS = "Task", TASK_DELAY = "delay", TASK_ACCESS_TOKEN = "access_token",
            TASK_MODULE = "module", TASK_NAME = "name", TASK_ID = "task_id",
            TASK_CREATED_AT = "created_at", TASK_NEXT_EXECUTION = "next_execution",
            PARAMS_EDGE = "HasParam", PARAMS_CLASS = "Parameter", PARAMS_NAME = "name", PARAMS_VALUES = "values";

    /** {@inheritDoc} */
    @Override
    public boolean isInitiated(OrientGraphWrapper telaGraph) {
        OrientBaseGraph graph = telaGraph.getNoTxGraph();
        boolean isInitiated = telaGraph.existsClass(graph, TASK_CLASS);
        graph.shutdown();
        return isInitiated;
    }

    /** {@inheritDoc} */
    @Override
    public void init(OrientGraphWrapper telaGraph) {

        OrientBaseGraph graph = telaGraph.getNoTxGraph();

        // Create vertex
        Map<String, OType> properties = new HashMap<>();
        properties.put(TASK_ID, OType.INTEGER);
        properties.put(TASK_DELAY, OType.INTEGER);
        properties.put(TASK_MODULE, OType.STRING);
        properties.put(TASK_NAME, OType.STRING);
        properties.put(TASK_ACCESS_TOKEN, OType.STRING);
        properties.put(TASK_CREATED_AT, OType.DATETIME);
        properties.put(TASK_NEXT_EXECUTION, OType.DATETIME);
        telaGraph.createVertexClass(graph, TASK_CLASS, properties);

        // Create params vertex
        Map<String, OType> paramsProperties = new HashMap<>();
        paramsProperties.put(PARAMS_NAME, OType.STRING);
        paramsProperties.put(PARAMS_VALUES, OType.EMBEDDEDLIST);
        telaGraph.createVertexClass(graph, PARAMS_CLASS, paramsProperties);

        // Create indexes
        Map<String, OClass.INDEX_TYPE> indexes = new HashMap<>();
        indexes.put(TASK_ID, OClass.INDEX_TYPE.UNIQUE_HASH_INDEX);
        indexes.put(TASK_ACCESS_TOKEN, OClass.INDEX_TYPE.NOTUNIQUE_HASH_INDEX);
        telaGraph.createIndexes(graph, TASK_CLASS, indexes);

        graph.shutdown();

    }

}
