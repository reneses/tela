package io.reneses.tela.core.databases.orientdb;

import com.orientechnologies.orient.client.remote.OServerAdmin;
import com.orientechnologies.orient.core.exception.OSecurityAccessException;
import io.reneses.tela.core.databases.extensions.DatabaseExtension;
import io.reneses.tela.core.databases.extensions.OrientDatabaseExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Factory which can be configured at runtime in order to use the desired OrientDB connection
 * It mains and returns a singleton object of the configured implementation.
 */
public class OrientGraphWrapperFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrientGraphWrapperFactory.class);
    private static OrientGraphWrapper singleton;
    private static List<OrientDatabaseExtension> extensions = new ArrayList<>();

    private OrientGraphWrapperFactory() {
    }

    /**
     * Register the database extensions
     * The list of extensions will be filtered, and only the OrientDatabaseExtension instances will be initiated
     *
     * @param graphExtensions Orient database extensions
     */
    public static void registerExtensions(Iterable<DatabaseExtension> graphExtensions) {
        for (DatabaseExtension extension : graphExtensions) {
            if (extension instanceof OrientDatabaseExtension) {
                LOGGER.info("[OrientDB] Graph extension registered: {}", extension.getClass().getSimpleName());
                extensions.add((OrientDatabaseExtension) extension);
            }
        }
    }

    /**
     * Register the database extensions
     * The list of extensions will be filtered, and only the OrientDatabaseExtension instances will be initiated
     *
     * @param extensions Orient database extensions
     */
    public static void registerExtensions(DatabaseExtension... extensions) {
        registerExtensions(Arrays.asList(extensions));
    }

    /**
     * Connect to a local OrientDB database
     *
     * @param path Path to the database
     */
    public static void connectLocal(String path) {
        LOGGER.info("[OrientDB] Initiating local database in {}", path);
        singleton = new OrientGraphWrapperImpl(extensions, "plocal:" + path);
    }

    private static void createRemote(String database, String host, int port, String user, String password) {
        OServerAdmin serverAdmin = null;
        try {
            serverAdmin = new OServerAdmin("remote:" + host + ":" + port).connect(user, password);
            if (!serverAdmin.existsDatabase(database, "plocal"))
                serverAdmin.createDatabase(database, "graph", "plocal");
            serverAdmin.close();
        } catch (OSecurityAccessException e) {
            LOGGER.error("The connection to OrientDB server {}@{}:{} could not be established", user, host, port);
            throw new RuntimeException("The connection to OrientDB server could not be established");
        } catch (Exception e) {
            LOGGER.error("Exception checking/creating remote database " + database, e);
        } finally {
            if (serverAdmin != null)
                serverAdmin.close();
        }
    }

    /**
     * Connect to a remote OrientDB database
     *
     * @param database Database name
     * @param host     Database host
     * @param port     Database port
     * @param user     Database user
     * @param password Database password
     */
    public static void connectRemote(String database, String host, int port, String user, String password) {
        LOGGER.info("[OrientDB] Initiating remote database at {}@{}:{}/{}", user, host, port, database);
        createRemote(database, host, port, user, password);
        singleton = new OrientGraphWrapperImpl(extensions, "REMOTE:" + host + "/" + database, user, password);
    }

    /**
     * Connect to a in-memory OrientDB database
     *
     * @param database Database name
     */
    public static void connectMemory(String database) {
        LOGGER.info("[OrientDB] Initiating in-memory database '{}'", database);
        singleton = new OrientGraphWrapperImpl(extensions, "memory:" + database);
    }

    /**
     * Get a singleton instance of the configured OrientDB instance
     *
     * @return OrientDB instance
     */
    public static OrientGraphWrapper get() {
        if (singleton == null)
            throw new RuntimeException("There is no OrientDB connection defined");
        return singleton;
    }

    /**
     * Drop the database and destroy the instance
     * WARNING: This will erase all data. Use only for testing purposes.
     */
    public static void dropAndDestroyInstance() {
        singleton.drop();
        singleton.close();
        singleton = null;
    }

}
