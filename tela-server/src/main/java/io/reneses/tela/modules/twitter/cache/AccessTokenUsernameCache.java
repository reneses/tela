package io.reneses.tela.modules.twitter.cache;


/**
 * CacheManager storing the username corresponding to each access token (this is, to the logged user)
 */
public interface AccessTokenUsernameCache {

    /**
     * Store into the cacheManager the access token and its related username
     *
     * @param accessToken Access token
     * @param username Username corresponding to the access token
     */
    void put(String accessToken, String username);

    /**
     * Retrieve the username related to an access token
     *
     * @param accessToken Access token
     * @return a Username of the access token
     */
    String getUsername(String accessToken);

    /**
     * Clear all the cache entries
     */
    void clear();

}
