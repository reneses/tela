package io.reneses.tela.modules.twitter.api;


import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;

/**
 * Factory in charge of creating a Twitter API Wrapper
 */
public class TwitterApiWrapperFactory {

    private static AbstractTwitterApiWrapper nonCachedSingleton, cachedSingleton;
    private static final boolean DEFAULT_CACHED = true;

    /**
     * Get an instance of an Twitter API Wrapper
     *
     * @param cached If a cached-enable instance should be created or not
     * @return Twitter API Wrapper
     */
    public static AbstractTwitterApiWrapper create(boolean cached) {
        if (!cached && nonCachedSingleton == null || cached && cachedSingleton == null) {
            TwitterApi twitterApi = new TwitterApiImpl();
            History history = HistoryFactory.create();
            if (cached)
                cachedSingleton = new CachedTwitterApiWrapper(twitterApi, history);
            else
                nonCachedSingleton = new TwitterApiWrapper(twitterApi, history);
        }
        return cached? cachedSingleton : nonCachedSingleton;
    }

    /**
     * Get an instance of an Twitter API Wrapper with the default configuration regarding caching
     *
     * @return Twitter API Wrapper
     */
    public static AbstractTwitterApiWrapper create() {
        return create(DEFAULT_CACHED);
    }

}
