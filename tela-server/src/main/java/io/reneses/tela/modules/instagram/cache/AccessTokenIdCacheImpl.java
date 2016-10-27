package io.reneses.tela.modules.instagram.cache;

import io.reneses.tela.core.cache.CacheManager;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.modules.instagram.InstagramTelaModule;

/**
 * Implementation of the Access Token
 */
public class AccessTokenIdCacheImpl implements AccessTokenIdCache {

    private static final String PREFIX = "token-id";
    private CacheManager cache = CacheManagerFactory.create();

    private String getKey(String accessToken) {
        return PREFIX + ":" + accessToken;
    }

    /** {@inheritDoc} */
    @Override
    public void put(String accessToken, long id) {
        cache.put(InstagramTelaModule.NAME, getKey(accessToken), Long.toString(id));
    }

    /** {@inheritDoc} */
    @Override
    public Long getId(String accessToken) {
        String longString = cache.get(InstagramTelaModule.NAME, getKey(accessToken));
        return longString == null? null : Long.valueOf(longString);
    }

}
