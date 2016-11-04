package io.reneses.tela.core.scheduler;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.dispatcher.ActionDispatcher;
import io.reneses.tela.core.dispatcher.ActionDispatcherFactory;
import io.reneses.tela.core.dispatcher.exceptions.ActionNotDefinedException;
import io.reneses.tela.core.dispatcher.exceptions.InvalidParameterTypeException;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import io.reneses.tela.TestActions;
import io.reneses.tela.core.scheduler.exceptions.ActionNotSchedulableException;
import io.reneses.tela.core.sessions.SessionManager;
import io.reneses.tela.core.sessions.models.Session;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * It's important to remark that the SchedulerImpl executes the action in the moment of scheduling.
 * Therefore, an action each second will be executed 4 times in 3 seconds
 */
public class SchedulerImplTest {

    private static ActionDispatcher actionDispatcher;

    private SessionManager sessionManager;
    private Scheduler scheduler;
    private Session session;

    @BeforeClass
    public static void setUpBeforeClass() {
        ActionDispatcherFactory.addClassesToScan(TestActions.class);
        actionDispatcher = ActionDispatcherFactory.create();
    }

    @Before
    public void setUp() throws Exception {
        sessionManager = TestUtils.configureSessionManager();
        TestActions.resetCount();
        SchedulerFactory.setDelay(1);

        scheduler = SchedulerFactory.create(actionDispatcher, sessionManager);
        session = sessionManager.create();
    }

    @After
    public void tearDown() throws Exception {
        scheduler.close();
        TestUtils.destroyDatabase();
    }

    @Test
    public void schedule() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "count");
        scheduler.schedule(task);
        Thread.sleep(11000);
        assertEquals(4, TestActions.getCount());
    }

    @Test
    public void scheduleBelowMinimumDelay() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 10, "test", "slowCount");
        assertEquals(10, task.getDelay());
        scheduler.schedule(task);

        assertEquals(60, task.getDelay());
        ScheduledAction retrieved = scheduler.getScheduledAction(task.getId());
        assertEquals(60, retrieved.getDelay());
    }

    @Test(expected = IllegalArgumentException.class)
    public void scheduleNull() throws Exception {
        scheduler.schedule(null);
    }

    @Test(expected = ActionNotSchedulableException.class)
    public void scheduleNotSchedulable() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "bye");
        scheduler.schedule(task);
    }

    @Test(expected = SessionNotFoundException.class)
    public void scheduleWithBadSession() throws Exception {
        ScheduledAction task = new ScheduledAction("", 3, "test", "hello");
        scheduler.schedule(task);
    }

    @Test(expected = ActionNotDefinedException.class)
    public void scheduleActionNotDefined() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "error");
        scheduler.schedule(task);
    }

    @Test(expected = ActionNotDefinedException.class)
    public void scheduleWithoutRequiredParameters() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "negate");
        scheduler.schedule(task);
    }

    @Test(expected = InvalidParameterTypeException.class)
    public void scheduleWithInvalidParameterType() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("n", new String[] {"hello"});
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "negate", params);
        scheduler.schedule(task);
    }

    @Test
    public void scheduleWithParameters() throws Exception {
        Map<String, String[]> params = new HashMap<>();
        params.put("n", new String[] {"1"});
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 10, "test", "negate", params);
        scheduler.schedule(task);
    }

    @Test
    public void scheduleDifferentTimes() throws Exception {
        ScheduledAction t1 = new ScheduledAction(session.getAccessToken(), 3, "test", "count");
        ScheduledAction t2 = new ScheduledAction(session.getAccessToken(), 7, "test", "count");
        scheduler.schedule(t1);
        scheduler.schedule(t2);
        Thread.sleep(10000);
        assertEquals(6, TestActions.getCount());
    }

    @Test
    public void testRemoveSession() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "count");
        scheduler.schedule(task);
        Thread.sleep(5000);
        sessionManager.delete(session);
        Thread.sleep(2000);
        assertEquals(2, TestActions.getCount());
        assertTrue(scheduler.getScheduledActions().isEmpty());
    }

    @Test
    public void cancel() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "count");
        scheduler.schedule(task);
        scheduler.cancel(session, task);
        Thread.sleep(4000);
        assertEquals(1, TestActions.getCount());
    }

    @Test
    public void close() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "count");
        scheduler.schedule(task);
        scheduler.close();
        Thread.sleep(4000);
        assertEquals(1, TestActions.getCount());
    }

    @Test
    public void closeActionsPersists() throws Exception {
        ScheduledAction task = new ScheduledAction(session.getAccessToken(), 3, "test", "count");
        scheduler.schedule(task);
        scheduler.getScheduledActions();
        scheduler.close();
        scheduler.getScheduledActions();
        assertTrue(scheduler.contains(task));
    }
}