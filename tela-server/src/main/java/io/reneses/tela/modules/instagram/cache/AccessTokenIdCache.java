package io.reneses.tela.modules.instagram.cache;

/**
 * Cache to remember the ID corresponding to an access token
 */
public interface AccessTokenIdCache {

    /**
     * Store the user ID related to an access token
     *
     * @param accessToken AccessToken
     * @param userId UserId
     */
    void put(String accessToken, long userId);

    /**
     * Get the ID of the given access token
     *
     * @param accessToken Access token
     * @return User ID of the given access token, or null if not found
     */
    Long getId(String accessToken);

}
