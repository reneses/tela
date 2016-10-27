package io.reneses.tela.modules.instagram.cache;

import io.reneses.tela.core.cache.CacheManager;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.modules.instagram.InstagramTelaModule;

/**
 * Implementation of the UsernameIdCache
 */
public class UsernameIdCacheImpl implements UsernameIdCache {

    private static final String PREFIX = "username-id";
    private CacheManager cache = CacheManagerFactory.create();

    private String getKey(String username) {
        return PREFIX + ":" + username;
    }

    /** {@inheritDoc} */
    @Override
    public void put(String username, long id) {
        cache.put(InstagramTelaModule.NAME, getKey(username), Long.toString(id));
    }

    /** {@inheritDoc} */
    @Override
    public Long getId(String username) {
        String longString = cache.get(InstagramTelaModule.NAME, getKey(username));
        return longString == null? null : Long.valueOf(longString);
    }

}
