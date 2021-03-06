package io.reneses.tela.core.cache;

import io.reneses.tela.TestUtils;
import io.reneses.tela.core.databases.redis.JedisFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.embedded.RedisServer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class RedisCacheManagerTest {

    private CacheManager cache;

    @Before
    public void setUp() throws Exception {
        TestUtils.startTestRedis();
        cache = new RedisCacheManager(1);
    }

    @After
    public void tearDown() throws Exception {
        TestUtils.stopTestRedis();
    }

    @Test
    public void put() throws Exception {
        cache.put("test", "test", "1");
        assertEquals("1", cache.get("test", "test"));
    }

    @Test
    public void putSeveral() throws Exception {
        for (int i = 0; i < 1000; i++)
            cache.put("test", "entry" + i, String.valueOf(i));
        for (int i = 0; i < 1000; i++)
            assertEquals(String.valueOf(i), cache.get("test", "entry" + i));
    }

    @Test
    public void putExpires() throws Exception {
        cache.put("test", "test", "test");
        Thread.sleep(2000);
        assertNull(cache.get("test", "test"));
    }

    @Test
    public void putOverwriteTtl() throws Exception {
        cache.put("test", "test", "test");
        Thread.sleep(700);
        cache.put("test", "test", "test");
        Thread.sleep(700);
        assertNotNull(cache.get("test", "test"));
    }

    @Test
    public void putNull() throws Exception {
        cache.put("test", "test", null);
        assertNull(cache.get("test", "test"));
    }

    @Test
    public void putOverwrite() throws Exception {
        cache.put("test", "test", "1");
        cache.put("test", "test", "2");
        assertEquals("2", cache.get("test", "test"));
    }

    @Test
    public void putNullOverwrites() throws Exception {
        cache.put("test", "test", "1");
        cache.put("test", "test", null);
        assertNull(cache.get("test", "test"));
    }

    @Test
    public void getNotExisting() throws Exception {
        assertNull(cache.get("test", "test"));
    }

    @Test
    public void putModuleCollision() throws Exception {
        cache.put("test1", "test", "1");
        cache.put("test2", "test", "2");
        assertEquals("1", cache.get("test1", "test"));
        assertEquals("2", cache.get("test2", "test"));
    }

    @Test
    public void clear() throws Exception {
        cache.put("test", "1", "1");
        cache.put("test", "2", "2");
        cache.clear();
        assertNull(cache.get("test", "1"));
        assertNull(cache.get("test", "2"));
    }

}