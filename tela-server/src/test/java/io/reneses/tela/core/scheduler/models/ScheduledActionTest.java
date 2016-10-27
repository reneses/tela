package io.reneses.tela.core.scheduler.models;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.reneses.tela.core.scheduler.models.ScheduledAction;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static org.junit.Assert.*;


public class ScheduledActionTest {

    @Test
    public void serialization() throws Exception {

        Map<String, String[]> params = new HashMap<>();
        params.put("p1", new String[]{ "1", "2" });
        ScheduledAction action = new ScheduledAction("1", 60, "test", "action", params);

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(action);
        assertNotNull(json);
        assertFalse(json.isEmpty());
        ScheduledAction back = mapper.readValue(json, ScheduledAction.class);

        assertEquals(action, back);
        assertEquals(action.getCreatedAt(), back.getCreatedAt());
        assertEquals(action.getNextExecution(), back.getNextExecution());

    }

    @Test
    public void constructorWithoutDates() throws Exception {
        ScheduledAction task = new ScheduledAction("1", 60, "test", "action");
        assertNotNull(task.getCreatedAt());
        assertNotNull(task.getNextExecution());
        assertEquals(task.getCreatedAt().plusSeconds(task.getDelay()), task.getNextExecution());
    }

    @Test
    public void updateNextExecution() throws Exception {
        ScheduledAction task = new ScheduledAction("1", 60, "test", "action");
        assertEquals(task.getCreatedAt().plusSeconds(task.getDelay()), task.getNextExecution());
        task.updateNextExecution();
        assertEquals(task.getCreatedAt().plusSeconds(task.getDelay() * 2), task.getNextExecution());
        task.updateNextExecution();
        assertEquals(task.getCreatedAt().plusSeconds(task.getDelay() * 3), task.getNextExecution());
        task.updateNextExecution();
        assertEquals(task.getCreatedAt().plusSeconds(task.getDelay() * 4), task.getNextExecution());
    }

    @Test
    public void equalsWithoutParams() throws Exception {

        ScheduledAction t1 = new ScheduledAction("1", 60, "test", "action");
        ScheduledAction t2 = new ScheduledAction("1", 60, "test", "action");
        ScheduledAction t3 = new ScheduledAction("2", 60, "test", "action");
        ScheduledAction t4 = new ScheduledAction("1", 70, "test", "action");
        ScheduledAction t5 = new ScheduledAction("1", 60, "test test", "action");
        ScheduledAction t6 = new ScheduledAction("1", 60, "test", "action action");

        assertEquals(t1, t2);
        assertNotEquals(t2, t3);
        assertNotEquals(t2, t4);
        assertNotEquals(t2, t5);
        assertNotEquals(t2, t6);

        assertEquals(1, new HashSet<>(Arrays.asList(t1, t2)).size());
        assertEquals(5, new HashSet<>(Arrays.asList(t2, t3, t4, t5, t6)).size());
    }

    @Test
    public void equalsWithParams() throws Exception {

        Map<String, String[]> params1 = new HashMap<>();
        params1.put("p1", new String[]{ "1", "2" });
        ScheduledAction t1 = new ScheduledAction("1", 60, "test", "action", params1);

        Map<String, String[]> params2 = new HashMap<>();
        params2.put("p1", new String[]{ "1", "2" });
        ScheduledAction t2 = new ScheduledAction("1", 60, "test", "action", params2);

        Map<String, String[]> params3 = new HashMap<>();
        params3.put("p1", new String[]{ "1", "2" });
        params3.put("p2", new String[]{ "1" });
        ScheduledAction t3 = new ScheduledAction("1", 60, "test", "action", params3);

        Map<String, String[]> params4 = new HashMap<>();
        params4.put("p1", new String[]{ "1" });
        ScheduledAction t4 = new ScheduledAction("1", 60, "test", "action", params4);

        System.out.println(t1.hashCode());
        System.out.println(t2.hashCode());

        assertEquals(t1, t2);
        assertNotEquals(t2, t3);
        assertNotEquals(t2, t4);
        assertEquals(1, new HashSet<>(Arrays.asList(t1, t2)).size());
        assertEquals(3, new HashSet<>(Arrays.asList(t2, t3, t4)).size());
    }

}