package io.reneses.tela.core.databases.redis;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.net.URI;
import java.net.URISyntaxException;


/**
 * Factory returning Jedis instances, which can be used to interact with the configured Redis server
 */
public class JedisFactory {

    /** Constant <code>DEFAULT_HOST="localhost"</code> */
    public static final String DEFAULT_HOST = "localhost";

    /** Constant <code>DEFAULT_USER="null"</code> */
    public static final String DEFAULT_USER = null;

    /** Constant <code>DEFAULT_PASSWORD="null"</code> */
    public static final String DEFAULT_PASSWORD = null;

    /** Constant <code>DEFAULT_PORT=6379</code> */
    public static final int DEFAULT_PORT = 6379;

    private static final Logger LOGGER = LoggerFactory.getLogger(JedisFactory.class);
    private static JedisPool pool;

    private JedisFactory(){}

    private static URI generateRedisUrl(String host, int port, String user, String password) {
        String url = "redis://";
        if (user != null) {
            url += user;
            if (password != null)
                url += ':' + password;
            url += '@';
        }
        url += host + ":" + port;
        URI uri = null;
        try {
            uri = new URI(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return uri;
    }

    /**
     * Connect to a Redis server
     *
     * @param host Redis host
     * @param port Redis port
     */
    public static void connect(String host, int port) {
        connect(host, port, null, null);
    }

    /**
     * Connect to a Redis server with an user and password
     *
     * @param host Redis host
     * @param port Redis port
     * @param user Redis user
     * @param password Redis password
     */
    public static void connect(String host, int port, String user, String password) {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxIdle(5);
        poolConfig.setMinIdle(1);
        poolConfig.setMaxTotal(10);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        pool = new JedisPool(poolConfig, generateRedisUrl(host, port, user, password));
        LOGGER.info("Redis connection at {}:{} established", host, port);
    }

    /**
     * Get a Jedis instance
     *
     * @return Jedis instance
     */
    public static Jedis get() {
        if (pool == null) {
            throw new IllegalStateException("Trying to getId a Redis instance without being connecting");
        }
        return pool.getResource();
    }

}
