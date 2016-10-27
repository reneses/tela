package io.reneses.tela.core.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Factory which can be configured at runtime in order to use the desired cacheManager implementation.
 */
public class CacheManagerFactory {

    private enum Mode {
        REDIS, MEMORY, DEFAULT
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheManagerFactory.class);
    private static Mode mode = Mode.DEFAULT;
    private static int ttl = -1;

    private CacheManagerFactory() {}

    /**
     * Set the TTL (time to live) for each entry
     *
     * @param cacheTtl TTL, in seconds
     */
    public static void setTtl(int cacheTtl) {
        if (cacheTtl <= 0)
            throw new IllegalArgumentException("The TTL cannot must be a positive value");
        ttl = cacheTtl;
        LOGGER.info("[Cache] Cache TTL set to {}s", ttl);
    }

    /**
     * Configure the on-memory cacheManager mode
     */
    public static void setMemoryMode() {
        mode = Mode.MEMORY;
        LOGGER.info("[Cache] CacheManager mode set to Memory");
    }

    /**
     * Configure the redis cacheManager mode
     */
    public static void setRedisMode() {
        mode = Mode.REDIS;
        LOGGER.info("[Cache] CacheManager mode set to Redis");
    }

    /**
     * Create a cacheManager manager of the configured implementation
     *
     * @return CacheManager manager
     */
    public static CacheManager create() {
        int ttlToUse = ttl <= 0? CacheManager.DEFAULT_TTL : ttl;
        switch (mode) {
            case REDIS:
                return new RedisCacheManager(ttlToUse);
            case MEMORY:
                return new MapCacheManager(ttlToUse);
            case DEFAULT:
            default:
                throw new RuntimeException("No cache mode has been configured");
        }
    }

}
