package io.reneses.tela.core.scheduler.repositories;

import com.orientechnologies.orient.core.storage.ORecordDuplicatedException;
import com.tinkerpop.blueprints.Direction;
import com.tinkerpop.blueprints.Element;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import io.reneses.tela.core.scheduler.databases.extensions.SchedulerOrientDatabaseExtension;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * AbstractSchedulerRepository implementation using OrientDB
 */
public class OrientSchedulerRepository implements SchedulerRepository {

    private OrientGraphWrapper telaGraph;

    /**
     * Constructor for OrientSchedulerRepository.
     */
    public OrientSchedulerRepository() {
        telaGraph = OrientGraphWrapperFactory.get();
    }

    /**
     * Convert a LocalDateTime object to Date
     *
     * @param date Date object
     * @return LocalDateTime object
     */
    private static LocalDateTime dateToLdt(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    /**
     * Convert a Date object to LocalDateTime
     *
     * @param date LocalDateTime object
     * @return Date Object
     */
    private static Date ldtToDate(LocalDateTime date) {
        return Date.from(date.atZone(ZoneId.systemDefault()).toInstant());
    }

    /**
     * Map a vertex to a ScheduledAction
     *
     * @param vertex Vertex
     * @return ScheduledAction
     */
    ScheduledAction mapVertex(Vertex vertex) {

        String prefix = vertex.getProperty(SchedulerOrientDatabaseExtension.TASK_MODULE);
        String name = vertex.getProperty(SchedulerOrientDatabaseExtension.TASK_NAME);
        int delay = vertex.getProperty(SchedulerOrientDatabaseExtension.TASK_DELAY);
        String accessToken = vertex.getProperty(SchedulerOrientDatabaseExtension.TASK_ACCESS_TOKEN);
        LocalDateTime createdAt = dateToLdt(vertex.getProperty(SchedulerOrientDatabaseExtension.TASK_CREATED_AT));
        LocalDateTime nextExecution = dateToLdt(vertex.getProperty(SchedulerOrientDatabaseExtension.TASK_NEXT_EXECUTION));

        Map<String, String[]> params = new HashMap<>();
        vertex.getVertices(Direction.OUT).forEach(v -> {
            String paramName = v.getProperty(SchedulerOrientDatabaseExtension.PARAMS_NAME);
            List<String> paramValues = v.getProperty(SchedulerOrientDatabaseExtension.PARAMS_VALUES);
            params.put(paramName, paramValues.toArray(new String[paramValues.size()]));
        });

        return new ScheduledAction(accessToken, delay, prefix, name, params, createdAt, nextExecution);
    }

    //----------------------------------- REPOSITORY FUNCTIONS -----------------------------------//

    /** {@inheritDoc} */
    @Override
    public boolean create(ScheduledAction task) {

        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        boolean success = false;
        try {
            // Task vertex
            Map<String, Object> properties = new HashMap<>();
            properties.put(SchedulerOrientDatabaseExtension.TASK_ACCESS_TOKEN, task.getAccessToken());
            properties.put(SchedulerOrientDatabaseExtension.TASK_ID, task.getId());
            properties.put(SchedulerOrientDatabaseExtension.TASK_NAME, task.getActionName());
            properties.put(SchedulerOrientDatabaseExtension.TASK_MODULE, task.getModuleName());
            properties.put(SchedulerOrientDatabaseExtension.TASK_DELAY, task.getDelay());
            properties.put(SchedulerOrientDatabaseExtension.TASK_CREATED_AT, ldtToDate(task.getCreatedAt()));
            properties.put(SchedulerOrientDatabaseExtension.TASK_NEXT_EXECUTION, ldtToDate(task.getNextExecution()));
            OrientVertex taskVertex = telaGraph.addVertex(graph, SchedulerOrientDatabaseExtension.TASK_CLASS, properties);

            // Parameters
            task.getParams().entrySet()
                    .stream()
                    .forEach(param -> {
                        Map<String, Object> paramProperties = new HashMap<>();
                        paramProperties.put(SchedulerOrientDatabaseExtension.PARAMS_NAME, param.getKey());
                        paramProperties.put(SchedulerOrientDatabaseExtension.PARAMS_VALUES, param.getValue());
                        OrientVertex paramVertex = telaGraph.addVertex(graph, SchedulerOrientDatabaseExtension.PARAMS_CLASS, paramProperties);
                        telaGraph.addEdge(graph, SchedulerOrientDatabaseExtension.PARAMS_EDGE, taskVertex, paramVertex);
                    });

            success = true;
        } catch (ORecordDuplicatedException ignored) {
        }

        graph.shutdown();
        return success;

    }

    /** {@inheritDoc} */
    @Override
    public void updateNextExecution(ScheduledAction task) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, SchedulerOrientDatabaseExtension.TASK_CLASS, SchedulerOrientDatabaseExtension.TASK_ID, task.getId());
        vertex.setProperty(SchedulerOrientDatabaseExtension.TASK_NEXT_EXECUTION, ldtToDate(task.getNextExecution()));
        vertex.save();
        graph.shutdown();
    }

    /** {@inheritDoc} */
    @Override
    public boolean contains(int taskId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, SchedulerOrientDatabaseExtension.TASK_CLASS, SchedulerOrientDatabaseExtension.TASK_ID, taskId);
        boolean result = vertex != null;
        graph.shutdown();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public ScheduledAction find(int taskId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, SchedulerOrientDatabaseExtension.TASK_CLASS, SchedulerOrientDatabaseExtension.TASK_ID, taskId);
        ScheduledAction task = (vertex == null) ? null : mapVertex(vertex);
        graph.shutdown();
        return task;
    }

    /** {@inheritDoc} */
    @Override
    public List<ScheduledAction> findReadyToExecute() {
        List<ScheduledAction> result = new ArrayList<>();
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        Iterable<Object> iterable = telaGraph.executeAndFetch(graph, String.format("SELECT FROM %s WHERE %s <= sysdate()",
                SchedulerOrientDatabaseExtension.TASK_CLASS, SchedulerOrientDatabaseExtension.TASK_NEXT_EXECUTION));
        iterable.forEach(v -> result.add(mapVertex((Vertex) v)));
        graph.shutdown();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<ScheduledAction> findByAccessToken(String accessToken) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<ScheduledAction> tasks = telaGraph
                .getVertices(graph, SchedulerOrientDatabaseExtension.TASK_CLASS, SchedulerOrientDatabaseExtension.TASK_ACCESS_TOKEN, accessToken)
                .stream()
                .map(this::mapVertex)
                .collect(Collectors.toList());
        graph.shutdown();
        return tasks;
    }

    /** {@inheritDoc} */
    @Override
    public List<ScheduledAction> findAll() {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        List<ScheduledAction> tasks = telaGraph
                .getVertices(graph, SchedulerOrientDatabaseExtension.TASK_CLASS)
                .stream()
                .map(this::mapVertex)
                .collect(Collectors.toList());
        graph.shutdown();
        return tasks;
    }

    /** {@inheritDoc} */
    @Override
    public boolean delete(int taskId) {
        OrientGraphNoTx graph = telaGraph.getNoTxGraph();
        OrientVertex vertex = telaGraph.getVertex(graph, SchedulerOrientDatabaseExtension.TASK_CLASS, SchedulerOrientDatabaseExtension.TASK_ID, taskId);
        if (vertex == null)
            return false;
        vertex.getVertices(Direction.OUT).forEach(Element::remove);
        vertex.remove();
        graph.shutdown();
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public int deleteByAccessToken(String accessToken) {
        List<ScheduledAction> tasks = findByAccessToken(accessToken);
        tasks.forEach(t -> delete(t.getId()));
        return tasks.size();
    }

}
