package io.reneses.tela.core.history;

import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.core.history.models.HistoryEntry;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class HistoryImplTest {

    private History history;

    private void initHistory(int ttl) {
        CacheManagerFactory.setTtl(ttl);
        history = HistoryFactory.create();
    }

    @Before
    public void setUp() throws Exception {
        CacheManagerFactory.setMemoryMode();
    }

    @After
    public void tearDown() throws Exception {
        CacheManagerFactory.create().clear();
    }

    @Test
    public void add() throws Exception {
        initHistory(1);
        HistoryEntry entry = new HistoryEntry("test", "test");
        history.add(entry);
        assertTrue(history.isPresent(entry));
    }

    @Test
    public void addExpires() throws Exception {
        HistoryEntry entry = new HistoryEntry("test", "test");
        initHistory(1);
        history.add(entry);
        Thread.sleep(3000);
        assertFalse(history.isPresent(entry));
    }

    @Test
    public void addOverwriteTtl() throws Exception {
        HistoryEntry entry = new HistoryEntry("test", "test");
        initHistory(2);
        history.add(entry);
        Thread.sleep(1200);
        history.add(entry);
        Thread.sleep(1200);
        assertTrue(history.isPresent(entry));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNull() throws Exception {
        initHistory(3);
        history.add(null);
    }

    @Test
    public void addDifferentParameters() throws Exception {
        HistoryEntry entry1 = new HistoryEntry("test", "test", 1);
        HistoryEntry entry2 = new HistoryEntry("test", "test", "hola");
        initHistory(3);
        history.add(entry1);
        history.add(entry2);
        assertTrue(history.isPresent(entry1));
        assertTrue(history.isPresent(entry2));
    }

    @Test
    public void isPresentNull() throws Exception {
        initHistory(3);
        assertFalse(history.isPresent(null));
    }

}