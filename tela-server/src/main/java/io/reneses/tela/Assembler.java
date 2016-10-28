package io.reneses.tela;

import io.reneses.tela.core.configuration.Configuration;
import io.reneses.tela.core.configuration.ConfigurationFactory;
import io.reneses.tela.modules.TelaModule;
import io.reneses.tela.core.api.controllers.TelaController;
import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.api.server.TelaServerFactory;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.redis.JedisFactory;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.dispatcher.ActionDispatcherFactory;
import io.reneses.tela.core.scheduler.Scheduler;
import io.reneses.tela.core.scheduler.SchedulerFactory;
import io.reneses.tela.core.scheduler.databases.extensions.SchedulerOrientDatabaseExtension;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.SessionManagerFactory;
import io.reneses.tela.core.sessions.databases.extensions.SessionOrientDatabaseExtension;

/**
 * The purpose of the Assembler is to read the configuration, createOrUpdate and configure the desired components and
 * bind everything together, returning a TelaServer isntance
 */
public class Assembler {

    private Assembler() {
    }

    // Configure Redis (if configured with 'tempMode')
    private static void configureRedis(Configuration config) {
        String tempMode = config.getStringProperty(Configuration.Property.CACHE_MODE);
        if (!tempMode.equalsIgnoreCase(Configuration.TempMode.REDIS))
            return;
        JedisFactory.connect(
                config.getStringProperty(Configuration.Property.REDIS_HOST),
                config.getIntegerProperty(Configuration.Property.REDIS_PORT),
                config.getStringProperty(Configuration.Property.REDIS_USER),
                config.getStringProperty(Configuration.Property.REDIS_PASSWORD)
        );
    }

    // Configure the cache
    private static void configureCache(Configuration config) {
        int ttl = config.getIntegerProperty(Configuration.Property.CACHE_TTL);
        CacheManagerFactory.setTtl(ttl);
        String tempMode = config.getStringProperty(Configuration.Property.CACHE_MODE);
        switch (tempMode) {
            case Configuration.TempMode.REDIS:
                CacheManagerFactory.setRedisMode();
                break;
            case Configuration.TempMode.MEMORY:
            default:
                CacheManagerFactory.setMemoryMode();
        }
    }

    // Configure the action dispatcher registering the modules
    private static ActionDispatcher configureActionDispatcher(TelaModule... modules) {
        for (TelaModule module : modules) {
            ActionDispatcherFactory.addPackagesToScan(module.getClass().getPackage());
        }
        return ActionDispatcherFactory.create();
    }

    // Configure OrientDB
    private static void configureOrientDb(Configuration config, TelaModule... modules) {

        // Register core extensions
        OrientGraphWrapperFactory.registerExtensions(
                new SchedulerOrientDatabaseExtension(),
                new SessionOrientDatabaseExtension()
        );

        // Register modules extensions
        for (TelaModule module : modules) {
            OrientGraphWrapperFactory.registerExtensions(module.getExtensions());
        }

        // Configure mode
        String orientDbMode = config.getStringProperty(Configuration.Property.ORIENTDB_MODE);
        switch (orientDbMode) {
            case Configuration.OrientDbMode.LOCAL:
                String local = config.getStringProperty(Configuration.Property.ORIENTDB_LOCAL);
                OrientGraphWrapperFactory.connectLocal(local);
                break;
            case Configuration.OrientDbMode.MEMORY:
                String memory = config.getStringProperty(Configuration.Property.ORIENTDB_DATABASE);
                OrientGraphWrapperFactory.connectMemory(memory);
                break;
            case Configuration.OrientDbMode.REMOTE:
                String database = config.getStringProperty(Configuration.Property.ORIENTDB_DATABASE);
                String host = config.getStringProperty(Configuration.Property.ORIENTDB_HOST);
                int port = config.getIntegerProperty(Configuration.Property.PORT);
                String user = config.getStringProperty(Configuration.Property.ORIENTDB_USER);
                String password = config.getStringProperty(Configuration.Property.ORIENTDB_PASSWORD);
                OrientGraphWrapperFactory.connectRemote(database, host, port, user, password);
                break;
        }

    }

    // Configure the session manager
    private static SessionManager configureSessionManager() {
        return SessionManagerFactory.create();
    }

    // Configure the scheduler
    private static Scheduler configureScheduler(
            Configuration config,
            ActionDispatcher dispatcher,
            SessionManager sessionManager) {

        int schedulerDelay = config.getIntegerProperty(Configuration.Property.SCHEDULER_DELAY);
        SchedulerFactory.setDelay(schedulerDelay);
        return SchedulerFactory.create(dispatcher, sessionManager);
    }

    /**
     * Create, configure and bind the required components
     *
     * @param config Tela configuration
     * @param modules Tela modules
     * @return Tela Server configured instance
     */
    public static TelaServer build(Configuration config, TelaModule... modules) {

        configureRedis(config);
        configureCache(config);
        configureOrientDb(config, modules);

        ActionDispatcher dispatcher = configureActionDispatcher(modules);
        SessionManager sessionManager = configureSessionManager();
        Scheduler scheduler = configureScheduler(config, dispatcher, sessionManager);
        TelaController.configureComponents(sessionManager, dispatcher, scheduler);

        int port = config.getIntegerProperty(Configuration.Property.PORT);
        return TelaServerFactory.create(port);

    }

    /**
     * Create, configure and bind the required components
     *
     * @param modules Tela modules
     * @return Tela Server configured instance
     */
    public static TelaServer build(TelaModule... modules) {
        Configuration config = ConfigurationFactory.create();
        return build(config, modules);
    }

}
