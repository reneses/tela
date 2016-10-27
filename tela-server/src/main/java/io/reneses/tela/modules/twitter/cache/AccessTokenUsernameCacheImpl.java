package io.reneses.tela.modules.twitter.cache;


import io.reneses.tela.core.cache.CacheManager;
import io.reneses.tela.core.cache.CacheManagerFactory;
import io.reneses.tela.modules.twitter.TwitterTelaModule;

/**
 * AccessTokenUsernameCacheImpl implementation
 */
public class AccessTokenUsernameCacheImpl implements AccessTokenUsernameCache {

    private static final String PREFIX = "twitter:token-username";
    private CacheManager cacheManager = CacheManagerFactory.create();

    private String getKey(String accessToken) {
        return PREFIX + ":" + accessToken;
    }

    /** {@inheritDoc} */
    @Override
    public void put(String accessToken, String username) {
        cacheManager.put(TwitterTelaModule.NAME, getKey(accessToken), username);
    }

    /** {@inheritDoc} */
    @Override
    public String getUsername(String accessToken) {
        return cacheManager.get(TwitterTelaModule.NAME, getKey(accessToken));
    }

    /** {@inheritDoc} */
    @Override
    public void clear() {
        cacheManager.clear();
    }

}
