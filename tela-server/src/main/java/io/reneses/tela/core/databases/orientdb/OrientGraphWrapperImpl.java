package io.reneses.tela.core.databases.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.orientechnologies.orient.core.sql.OCommandSQL;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.orient.*;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Wrapper for an OrientDB database, with utility functions
 */
class OrientGraphWrapperImpl implements OrientGraphWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrientGraphWrapperImpl.class);

    private OrientGraphFactory factory;
    private String url;
    private long threadId;

    OrientGraphWrapperImpl(Iterable<OrientDatabaseExtension> extensions, String url) {
        this.url = url;
        this.threadId = Thread.currentThread().getId();
        factory = new OrientGraphFactory(url, true);
        init(extensions);
    }

    OrientGraphWrapperImpl(Iterable<OrientDatabaseExtension> extensions, String url, String user, String password) {
        this.url = url;
        this.threadId = Thread.currentThread().getId();
        factory = new OrientGraphFactory(url, user, password, true);
        init(extensions);
    }

    /**
     * Init the database (this is, the registered extensions)
     */
    private void init(Iterable<OrientDatabaseExtension> extensions) {
        if (extensions == null || !extensions.iterator().hasNext()) {
            LOGGER.warn("[OrientDB] OrientDB is being initialize without any database extension");
            return;
        }
        for (OrientDatabaseExtension extension : extensions) {
            if (!extension.isInitiated(this)) {
                LOGGER.debug("[OrientDB] Initiating graph extension '{}'", extension.getClass().getSimpleName());
                extension.init(this);
            }
        }
    }

    /** {@inheritDoc} */
    @Override
    public OrientGraphNoTx getNoTxGraph() {
        if (Thread.currentThread().getId() == threadId)
            return factory.getNoTx();
        return new OrientGraphNoTx(url);
    }

    /** {@inheritDoc} */
    @Override
    public OrientGraph getTxGraph() {
        if (Thread.currentThread().getId() == threadId)
            return factory.getTx();
        return new OrientGraph(url);
    }

    /** {@inheritDoc} */
    @Override
    public OrientVertex addVertex(OrientBaseGraph graph, String vertexClass, Map<String, Object> properties) {
        return graph.addVertex("class:" + vertexClass, properties);
    }

    /** {@inheritDoc} */
    @Override
    public OrientVertex getVertex(OrientBaseGraph graph, String vertexClass, String key, Object value) {
        Iterator vertexIterator = executeAndFetch(graph, String.format("SELECT FROM %s WHERE %s = ?", vertexClass, key), value).iterator();
        if (!vertexIterator.hasNext())
            return null;
        return (OrientVertex) vertexIterator.next();
    }

    /** {@inheritDoc} */
    @Override
    public List<OrientVertex> getVertices(OrientBaseGraph graph, String vertexClass) {
        List<OrientVertex> vertices = new ArrayList<>();
        for (Vertex v : graph.getVerticesOfClass(vertexClass))
            vertices.add((OrientVertex) v);
        return vertices;
    }

    /** {@inheritDoc} */
    @Override
    public List<OrientVertex> getVertices(OrientBaseGraph graph, String vertexClass, String key, Object value) {
        Iterable vertexIterable = executeAndFetch(graph, String.format("SELECT FROM %s WHERE %s = ?", vertexClass, key), value);
        List<OrientVertex> vertices = new ArrayList<>();
        for (Object v : vertexIterable)
            vertices.add((OrientVertex) v);
        return vertices;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeVertices(OrientBaseGraph graph, String vertexClass, String key, Object value) {
        List<OrientVertex> vertices = getVertices(graph, vertexClass, key, value);
        if (vertices.isEmpty())
            return false;
        vertices.forEach(graph::removeVertex);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean removeVertices(OrientBaseGraph graph, String vertexClass) {
        List<OrientVertex> vertices = getVertices(graph, vertexClass);
        if (vertices.isEmpty())
            return false;
        vertices.forEach(graph::removeVertex);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsClass(OrientBaseGraph graph, String name) {
        return graph.getRawGraph().getMetadata().getSchema().existsClass(name);
    }

    /** {@inheritDoc} */
    @Override
    public void createVertexClass(OrientBaseGraph graph, String name, Map<String, OType> properties) {
            if (existsClass(graph, name)) {
                graph.dropVertexType(name);
            }
            OrientVertexType vertex = graph.createVertexType(name);
            for (Map.Entry<String, OType> p : properties.entrySet()) {
                vertex.createProperty(p.getKey(), p.getValue());
            }
    }

    /** {@inheritDoc} */
    @Override
    public void createVertexClass(OrientBaseGraph graph, String name) {
        createVertexClass(graph, name, new HashMap<>());
    }

    /** {@inheritDoc} */
    @Override
    public void createEdgeClass(OrientBaseGraph graph, String name, Map<String, OType> properties) {
            if (existsClass(graph, name)) {
                graph.dropEdgeType(name);
            }
            OrientEdgeType edge = graph.createEdgeType(name);
            for (Map.Entry<String, OType> p : properties.entrySet()) {
                edge.createProperty(p.getKey(), p.getValue());
            }
    }

    /** {@inheritDoc} */
    @Override
    public void createEdgeClass(OrientBaseGraph graph, String name) {
        createEdgeClass(graph, name, new HashMap<>());
    }

    /** {@inheritDoc} */
    @Override
    public void createIndexes(OrientBaseGraph graph, String className, Map<String, OClass.INDEX_TYPE> indexes) {
        indexes.forEach((indexProperty, indexType) -> {
            String indexName = className + "_" + indexProperty;
            graph.getRawGraph().getMetadata().getSchema().getClass(className).createIndex(
                    indexName,
                    indexType,
                    indexProperty
            );
        });
    }

    /** {@inheritDoc} */
    @Override
    public void addEdge(OrientBaseGraph graph, String edgeClass, OrientVertex out, OrientVertex in, Map<String, Object> fields) {
        out.addEdge(null, in, edgeClass, null, fields);
    }

    /** {@inheritDoc} */
    @Override
    public void addEdge(OrientBaseGraph graph, String edgeClass, OrientVertex out, OrientVertex in) {
        addEdge(graph, edgeClass, out, in, new HashMap<>());
    }


    /** {@inheritDoc} */
    @Override
    public Iterable<Object> executeAndFetch(OrientBaseGraph graph, String command, Object... bindings) {
        return graph.command(new OCommandSQL(command)).execute(bindings);
    }

    /** {@inheritDoc} */
    @Override
    public void execute(OrientBaseGraph graph, String command, Object... bindings) {
        graph.command(new OCommandSQL(command)).execute(bindings);
    }

    /** {@inheritDoc} */
    @Override
    public void drop() {
        factory.drop();
        LOGGER.info("[OrientDB] Database dropped");
    }

    /** {@inheritDoc} */
    @Override
    public void close() {
        factory.close();
        LOGGER.info("[OrientDB] Connection closed");
    }

}
