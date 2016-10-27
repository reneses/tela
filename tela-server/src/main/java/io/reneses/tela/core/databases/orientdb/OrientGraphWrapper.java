package io.reneses.tela.core.databases.orientdb;

import com.orientechnologies.orient.core.metadata.schema.OClass;
import com.orientechnologies.orient.core.metadata.schema.OType;
import com.tinkerpop.blueprints.impls.orient.*;

import java.util.*;

/**
 * Wrapper for an OrientDB database, with utility functions
 */
public interface OrientGraphWrapper {

    /**
     * Constant <code>DEFAULT_DIRECTORY="./data"</code>
     */
    String DEFAULT_DIRECTORY = "./data";

    /**
     * Constant <code>DEFAULT_DATABASE="Tela"</code>
     */
    String DEFAULT_DATABASE = "Tela";

    /**
     * Constant <code>DEFAULT_HOST="localhost"</code>
     */
    String DEFAULT_HOST = "localhost";

    /**
     * Constant <code>DEFAULT_USER="admin"</code>
     */
    String DEFAULT_USER = "admin";

    /**
     * Constant <code>DEFAULT_PASSWORD="admin"</code>
     */
    String DEFAULT_PASSWORD = "admin";

    /**
     * Constant <code>DEFAULT_PORT=2424</code>
     */
    int DEFAULT_PORT = 2424;

    /**
     * Get a not transactional graph
     *
     * @return Not transaction graph
     */
    OrientGraphNoTx getNoTxGraph();

    /**
     * Get a transactional graph
     *
     * @return Transactional graph
     */
    OrientGraph getTxGraph();

    /**
     * Add a vertex to the graph
     *
     * @param graph       Graph
     * @param vertexClass Vertex class
     * @param properties  Properties
     * @return Created vertex
     */
    OrientVertex addVertex(OrientBaseGraph graph, String vertexClass, Map<String, Object> properties);

    /**
     * Get a vertex from the graph
     *
     * @param graph       Graph
     * @param vertexClass Vertex class
     * @param key         Property to search for
     * @param value       Value to search for
     * @return Vertex if found, null otherwise
     */
    OrientVertex getVertex(OrientBaseGraph graph, String vertexClass, String key, Object value);

    /**
     * Get all the vertices from a specific class
     *
     * @param graph       Graph
     * @param vertexClass Vertex class
     * @return Vertices of the given class
     */
    List<OrientVertex> getVertices(OrientBaseGraph graph, String vertexClass);

    /**
     * Get all the vertices from a specific class with a specific property
     *
     * @param graph       Graph
     * @param vertexClass Vertex class
     * @param key         Property to search for
     * @param value       Value to search for
     * @return Vertices of the given class
     */
    List<OrientVertex> getVertices(OrientBaseGraph graph, String vertexClass, String key, Object value);

    /**
     * Remove all the vertices from a specific class with a specific property
     *
     * @param graph       Graph
     * @param vertexClass Vertex class
     * @param key         Property to search for
     * @param value       Value to search for
     * @return True if at least one vertex was removed, false if any
     */
    boolean removeVertices(OrientBaseGraph graph, String vertexClass, String key, Object value);

    /**
     * Remove all the vertices from a specific class
     *
     * @param graph       Graph
     * @param vertexClass Vertex class
     * @return True if at least one vertex was removed, false if any
     */
    boolean removeVertices(OrientBaseGraph graph, String vertexClass);

    /**
     * Check whether a class exist or not
     *
     * @param graph Graph
     * @param name  Class name
     * @return True if exists, false otherwise
     */
    boolean existsClass(OrientBaseGraph graph, String name);

    /**
     * Create a vertex class
     *
     * @param graph      Graph
     * @param name       Class name
     * @param properties Class properties
     */
    void createVertexClass(OrientBaseGraph graph, String name, Map<String, OType> properties);

    /**
     * Create a vertex class
     *
     * @param graph Graph
     * @param name  Class name
     */
    void createVertexClass(OrientBaseGraph graph, String name);

    /**
     * Create an edge class
     *
     * @param graph      Graph
     * @param name       Class name
     * @param properties Class properties
     */
    void createEdgeClass(OrientBaseGraph graph, String name, Map<String, OType> properties);

    /**
     * Create a vertex class
     *
     * @param graph Graph
     * @param name  Class name
     */
    void createEdgeClass(OrientBaseGraph graph, String name);

    /**
     * Create indexes for a class
     *
     * @param graph     Graph
     * @param className Class name
     * @param indexes   Indexes to createOrUpdate
     */
    void createIndexes(OrientBaseGraph graph, String className, Map<String, OClass.INDEX_TYPE> indexes);

    /**
     * Create an edge
     *
     * @param graph     Graph
     * @param edgeClass Edge's class
     * @param out       Out vertex
     * @param in        In vertex
     * @param fields    Edge's properties
     */
    void addEdge(OrientBaseGraph graph, String edgeClass, OrientVertex out, OrientVertex in, Map<String, Object> fields);

    /**
     * Create an edge
     *
     * @param graph     Graph
     * @param edgeClass Edge's class
     * @param out       Out vertex
     * @param in        In vertex
     */
    void addEdge(OrientBaseGraph graph, String edgeClass, OrientVertex out, OrientVertex in);


    /**
     * Execute a SQL command which will be fetched
     *
     * @param graph    Graph
     * @param command  SQL
     * @param bindings Bindings
     * @return Iterable result
     */
    Iterable<Object> executeAndFetch(OrientBaseGraph graph, String command, Object... bindings);

    /**
     * Execute a SQL command
     *
     * @param graph    Graph
     * @param command  SQL
     * @param bindings Bindings
     */
    void execute(OrientBaseGraph graph, String command, Object... bindings);

    /**
     * Drop all the data
     */
    void drop();

    /**
     * Close the connection
     */
    void close();

}
