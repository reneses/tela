package io.reneses.tela.core.cache;

import io.reneses.tela.core.databases.redis.JedisFactory;
import redis.clients.jedis.Jedis;

import static redis.clients.jedis.Protocol.Command.TTL;

/**
 * Redis cache manager implementation
 */
class RedisCacheManager extends AbstractCacheManager {


    /**
     * Cache Manager constructor
     *
     * @param ttl TTL (time to live) in seconds for each entry
     */
    RedisCacheManager(int ttl) {
        super(ttl);
    }

    private String getKey(String module, String key) {
        return module + ":" + key;
    }

    /** {@inheritDoc} */
    @Override
    public void put(String module, String key, String value) {
        Jedis jedis = JedisFactory.get();
        key = getKey(module, key);
        if (value == null) {
            jedis.del(key);
        } else {
            jedis.set(key, value);
            jedis.expire(key, ttl);
        }
        jedis.close();
    }

    /** {@inheritDoc} */
    @Override
    public String get(String module, String key) {
        Jedis jedis = JedisFactory.get();
        String value = jedis.get(getKey(module, key));
        jedis.close();
        return value;
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        Jedis jedis = JedisFactory.get();
        jedis.flushAll();
        jedis.close();
    }

}
