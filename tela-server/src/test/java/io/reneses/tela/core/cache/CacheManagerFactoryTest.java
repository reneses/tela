package io.reneses.tela.core.cache;

import io.reneses.tela.TestUtils;
import org.junit.Test;

import static org.junit.Assert.*;

public class CacheManagerFactoryTest {

    @Test
    public void setMemoryMode() throws Exception {
        CacheManagerFactory.setMemoryMode();
        CacheManager cacheManager = CacheManagerFactory.create();
        assertTrue(cacheManager instanceof MapCacheManager);
    }

    @Test
    public void setRedisMode() throws Exception {
        TestUtils.startTestRedis();
        try {
            CacheManagerFactory.setRedisMode();
            CacheManager cacheManager = CacheManagerFactory.create();
            assertTrue(cacheManager instanceof RedisCacheManager);
        }
        catch (Exception e) {
            TestUtils.stopTestRedis();
            throw e;
        }
        TestUtils.stopTestRedis();
    }

    @Test
    public void setTtl() throws Exception {
        CacheManagerFactory.setTtl(2000);
        CacheManagerFactory.setMemoryMode();
        AbstractCacheManager cacheManager = (AbstractCacheManager) CacheManagerFactory.create();
        assertEquals(2000, cacheManager.ttl);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTtlNegative() throws Exception {
        CacheManagerFactory.setTtl(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setTtlZero() throws Exception {
        CacheManagerFactory.setTtl(0);
    }

}