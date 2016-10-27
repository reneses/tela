package io.reneses.tela.core.cache;

/**
 * Generic cache manager
 *
 * The cache manager is supposed to be global, and different modules will use it.
 */
public interface CacheManager {

    /**
     * Default time (in seconds) the cache entries should be stored
     */
    int DEFAULT_TTL = 3600;

    /**
     * Store a string value in the cache
     * If the value is null, the entry will be removed
     *
     * @param module Name of the module is implementing a cache
     * @param key Cache key
     * @param value Value to cache
     */
    void put(String module, String key, String value);

    /**
     * Get a string value from the cacheManager
     *
     * @param module Name of the module is implementing a cache
     * @param key Cache key
     * @return Cache value if present, null otherwise
     */
    String get(String module, String key);

    /**
     * Clear all the entries
     */
    void clear();

}
