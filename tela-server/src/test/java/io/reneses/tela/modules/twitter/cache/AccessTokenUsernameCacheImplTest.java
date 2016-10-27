package io.reneses.tela.modules.twitter.cache;

import io.reneses.tela.core.cache.CacheManagerFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertEquals;


public class AccessTokenUsernameCacheImplTest {

    private AccessTokenUsernameCache cache;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        CacheManagerFactory.setMemoryMode();
    }

    @Before
    public void setUp() throws Exception {
        cache = new AccessTokenUsernameCacheImpl();
    }

    @After
    public void tearDown() throws Exception {
        cache.clear();
    }

    @Test
    public void put() throws Exception {
        cache.put("1", "test");
        assertEquals("test", cache.getUsername("1"));
    }

    @Test
    public void putSeveral() throws Exception {
        for (int i=0; i < 10000; i++)
            cache.put(String.valueOf(i), "test" + i);
        for (int i=0; i < 10000; i++)
            assertEquals("test"+i, cache.getUsername(String.valueOf(i)));
    }

}