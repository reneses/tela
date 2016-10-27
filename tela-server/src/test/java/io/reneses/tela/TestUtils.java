package io.reneses.tela;

import io.reneses.tela.core.api.controllers.TelaController;
import io.reneses.tela.core.api.server.TelaServer;
import io.reneses.tela.core.api.server.TelaServerFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.redis.JedisFactory;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.dispatcher.ActionDispatcherFactory;
import io.reneses.tela.core.scheduler.Scheduler;
import io.reneses.tela.core.scheduler.SchedulerFactory;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.SessionManagerFactory;
import io.reneses.tela.core.sessions.databases.extensions.SessionOrientDatabaseExtension;
import redis.embedded.RedisServer;

import java.io.IOException;

import static org.mockito.Mockito.mock;


public class TestUtils {

    private static RedisServer redisServer;

    public static void configureComponents(SessionManager sessionManager,
                                           ActionDispatcher actionDispatcher,
                                           Scheduler scheduler) {

        if (sessionManager == null)
            sessionManager = mock(SessionManager.class);
        if (actionDispatcher == null)
            actionDispatcher = mock(ActionDispatcher.class);
        if (scheduler == null)
            scheduler = mock(Scheduler.class);
        TelaController.configureComponents(sessionManager, actionDispatcher, scheduler);
    }

    public static SessionManager configureSessionManager() {
        OrientGraphWrapperFactory.registerExtensions(new SessionOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("Test");
        return SessionManagerFactory.create();
    }

    public static ActionDispatcher configureActionDispatcher(Class<?> classes) {
        ActionDispatcherFactory.addClassesToScan(classes);
        return ActionDispatcherFactory.create();
    }

    public static void destroyDatabase() {
                OrientGraphWrapperFactory.dropAndDestroyInstance();
    }

    public static void startTestRedis() throws IOException {
        if (redisServer == null) {
            redisServer = new RedisServer(6380);
        }
        redisServer.start();
        JedisFactory.connect("localhost", 6380);
    }
    public static void stopTestRedis() {
        redisServer.stop();
    }

}
