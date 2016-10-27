package io.reneses.tela.core.cache;

import java.util.AbstractMap;
import java.util.Date;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * On-memory cache implementation
 * Although this implementation takes the configured TTL into account, its performance is not optimal. Therefore, this
 * implementation is only recommended for developing and testing purposes, and not for production environments.
 */
class MapCacheManager extends AbstractCacheManager {

    private static final ConcurrentMap<String, AbstractMap.SimpleEntry<String, Date>> cache = new ConcurrentHashMap<>();

    /**
     * Cache Manager constructor
     *
     * @param ttl TTL (time to live) in seconds for each entry
     */
    MapCacheManager(int ttl) {
        super(ttl);
    }

    private String getKey(String module, String key) {
        return module + ":" + key;
    }

    private void expireIfNeeded(String key) {
        AbstractMap.SimpleEntry<String, Date> entry = cache.get(key);
        if (entry == null)
            return;
        long currentTime = new Date().getTime();
        double diff = (currentTime - entry.getValue().getTime())/1000;
        boolean expired = diff > (double) ttl;
        if (expired)
            cache.remove(key);
    }

    /** {@inheritDoc} */
    @Override
    public void put(String module, String key, String value) {
        if (value == null)
            cache.remove(getKey(module, key));
        else
            cache.put(getKey(module, key), new AbstractMap.SimpleEntry<>(value, new Date()));
        new Thread(() -> new HashSet<>(cache.keySet()).forEach(this::expireIfNeeded));
    }

    /** {@inheritDoc} */
    @Override
    public String get(String module, String key) {
        expireIfNeeded(getKey(module, key));
        AbstractMap.SimpleEntry<String, Date> value = cache.get(getKey(module, key));
        return value == null? null : value.getKey();
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        cache.clear();
    }

}
