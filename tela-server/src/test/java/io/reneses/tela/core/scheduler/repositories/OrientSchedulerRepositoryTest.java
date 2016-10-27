package io.reneses.tela.core.scheduler.repositories;

import com.tinkerpop.blueprints.impls.orient.OrientGraphNoTx;
import com.tinkerpop.blueprints.impls.orient.OrientVertex;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapperFactory;
import io.reneses.tela.core.databases.orientdb.OrientGraphWrapper;
import io.reneses.tela.core.scheduler.databases.extensions.SchedulerOrientDatabaseExtension;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


public class OrientSchedulerRepositoryTest {

    private OrientSchedulerRepository repository;

    @Before
    public void setUp() throws Exception {
        OrientGraphWrapperFactory.registerExtensions(new SchedulerOrientDatabaseExtension());
        OrientGraphWrapperFactory.connectMemory("Test");
        repository = new OrientSchedulerRepository();
    }

    @After
    public void tearDown() throws Exception {
        OrientGraphWrapperFactory.dropAndDestroyInstance();
    }

    @Test
    public void createWithoutParams() throws Exception {

        String session = "1";
        ScheduledAction task = new ScheduledAction(session, 3, "test", "count");

        repository.create(task);

        System.out.println(task.getId());
        assertNotNull(task.getId());
        ScheduledAction retrieved = repository.find(task.getId());

        assertEquals(task.getModuleName(), retrieved.getModuleName());
        assertEquals(task.getActionName(), retrieved.getActionName());
        assertEquals(task.getDelay(), retrieved.getDelay());
        assertTrue(retrieved.getParams().isEmpty());
        assertEquals(task.getCreatedAt(), retrieved.getCreatedAt());
        assertEquals(task.getNextExecution(), retrieved.getNextExecution());


    }

    @Test
    public void createWithParams() throws Exception {

        String session = "1";
        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"1"});
        params.put("p2", new String[]{"2", "3"});
        ScheduledAction task = new ScheduledAction(session, 3, "test", "count", params);

        repository.create(task);

        ScheduledAction retrieved = repository.find(task.getId());
        assertNotNull(retrieved.getParams());
        assertEquals(2, retrieved.getParams().size());
        assertArrayEquals(task.getParams().get(0), retrieved.getParams().get(0));
        assertArrayEquals(task.getParams().get(1), retrieved.getParams().get(1));

    }

    @Test
    public void contains() throws Exception {
        ScheduledAction t1 = new ScheduledAction("1", 1000, "test", "t1");
        ScheduledAction t2 = new ScheduledAction("1", 1000, "test", "t2");
        repository.create(t1);
        repository.create(t2);
        assertTrue(repository.contains(t1.getId()));
        assertTrue(repository.contains(t2.getId()));
    }
    @Test
    public void containsNotExisting() throws Exception {
        ScheduledAction t1 = new ScheduledAction("1", 1000, "test", "t1");
        assertFalse(repository.contains(t1.getId()));
    }

    @Test
    public void findReadyToExecute() throws Exception {
        ScheduledAction t1 = new ScheduledAction("1", 1, "test", "t1");
        ScheduledAction t2 = new ScheduledAction("2", 10000, "test", "t2");
        repository.create(t1);
        repository.create(t2);
        Thread.sleep(1000);
        List<ScheduledAction> tasks = repository.findReadyToExecute();
        assertEquals(1, tasks.size());
        assertEquals(t1.getId(), tasks.get(0).getId());
    }

    @Test
    public void findAll() throws Exception {
        ScheduledAction t1 = new ScheduledAction("1", 1000, "test", "t1");
        ScheduledAction t2 = new ScheduledAction("1", 1000, "test", "t2");
        ScheduledAction t3 = new ScheduledAction("1", 1000, "test", "t3");
        ScheduledAction t4 = new ScheduledAction("2", 1000, "test", "t1");
        ScheduledAction t5 = new ScheduledAction("2", 1000, "test", "t2");
        repository.create(t1);
        repository.create(t2);
        repository.create(t3);
        repository.create(t4);
        repository.create(t5);
        assertEquals(5, new HashSet<>(repository.findAll()).size());
        assertEquals(5, repository.findAll().size());
    }

    @Test
    public void findAllEmpty() throws Exception {
        assertEquals(0, repository.findAll().size());
    }

    @Test
    public void findBySession() throws Exception {
        ScheduledAction t1 = new ScheduledAction("1", 1000, "test", "t1");
        ScheduledAction t2 = new ScheduledAction("1", 1000, "test", "t2");
        ScheduledAction t3 = new ScheduledAction("1", 1000, "test", "t3");
        ScheduledAction t4 = new ScheduledAction("2", 1000, "test", "t1");
        ScheduledAction t5 = new ScheduledAction("2", 1000, "test", "t2");
        repository.create(t1);
        repository.create(t2);
        repository.create(t3);
        repository.create(t4);
        repository.create(t5);
        assertEquals(3, repository.findByAccessToken("1").size());
        assertEquals(2, repository.findByAccessToken("2").size());
        assertEquals(0, repository.findByAccessToken("3").size());
    }

    @Test
    public void findBySessionNotExisting() throws Exception {
        assertEquals(0, repository.findByAccessToken("3").size());
    }

    @Test
    public void delete() throws Exception {

        ScheduledAction t1 = new ScheduledAction("1", 1000, "test", "count");
        ScheduledAction t2 = new ScheduledAction("2", 1000, "test", "count");

        assertFalse(repository.delete(t1.getId()));

        repository.create(t1);
        repository.create(t2);

        assertTrue(repository.delete(t1.getId()));
        assertEquals(0, repository.findByAccessToken("1").size());
        assertEquals(1, repository.findAll().size());

        assertFalse(repository.delete(t1.getId()));

    }

    @Test
    public void deleteParamsAreDeleted() throws Exception {

        OrientGraphWrapper telaGraph = OrientGraphWrapperFactory.get();

        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{"1"});
        params.put("p2", new String[]{"2", "3"});
        ScheduledAction task = new ScheduledAction("1", 3, "test", "count", params);

        repository.create(task);
        repository.delete(task.getId());

        OrientGraphNoTx graph = telaGraph.getNoTxGraph();

        List<OrientVertex> paramVertices = telaGraph.getVertices(graph, SchedulerOrientDatabaseExtension.PARAMS_CLASS);
        assertTrue(paramVertices.isEmpty());

        graph.shutdown();

    }

    @Test
    public void deleteByAccessToken() throws Exception {

        ScheduledAction t1 = new ScheduledAction("1", 1000, "test", "count");
        ScheduledAction t2 = new ScheduledAction("2", 1000, "test", "count");
        repository.create(t1);
        repository.create(t2);

        assertEquals(1, repository.deleteByAccessToken("1"));
        assertEquals(0, repository.findByAccessToken("1").size());
        assertEquals(1, repository.findAll().size());

    }

    @Test
    public void deleteByAccessTokenNotExisting() throws Exception {
        assertEquals(0, repository.deleteByAccessToken("1"));
    }

}