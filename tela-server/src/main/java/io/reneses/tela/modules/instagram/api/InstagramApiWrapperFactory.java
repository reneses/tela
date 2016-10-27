package io.reneses.tela.modules.instagram.api;


import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.HistoryFactory;

/**
 * Factory in charge of creating a Instagram API Wrapper
 */
public class InstagramApiWrapperFactory {

    private static AbstractInstagramApiWrapper nonCachedSingleton, cachedSingleton;
    private static final boolean DEFAULT_CACHED = true;

    /**
     * Get an instance of an Instagram API Wrapper
     *
     * @param cached If a cached-enable instance should be created or not
     * @return Instagram API Wrapper
     */
    public static AbstractInstagramApiWrapper create(boolean cached) {
        if (!cached && nonCachedSingleton == null || cached && cachedSingleton == null) {
            InstagramApi instagramApi = new InstagramApiImpl();
            History history = HistoryFactory.create();
            if (cached)
                cachedSingleton = new CachedInstagramApiWrapper(instagramApi, history);
            else
                nonCachedSingleton = new InstagramApiWrapper(instagramApi, history);
        }
        return cached? cachedSingleton : nonCachedSingleton;
    }

    /**
     * Get an instance of an Instagram API Wrapper with the default configuration regarding caching
     *
     * @return Instagram API Wrapper
     */
    public static AbstractInstagramApiWrapper create() {
        return create(DEFAULT_CACHED);
    }

}
