package io.reneses.tela.core.cache;

/**
 * Abstract implementation of the Cache Manager, offering a common constructor
 */
abstract class AbstractCacheManager implements CacheManager {

    // Time (in seconds) the cache entries should be persistent
    int ttl;

    /**
     * Cache Manager constructor
     *
     * @param ttl TTL (time to live) in seconds for each entry
     */
    AbstractCacheManager(int ttl) {
        this.ttl = ttl;
    }

}
