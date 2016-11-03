package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.models.User;

import java.util.List;

/**
 * Abstract Twitter API communicating with the Twitter platform
 * This API does not offer the complete functionality of the Twitter API
 *
 * General documentation: https://dev.twitter.com/rest/public
 * Create credentials: https://apps.twitter.com/app/new
 * Auth flow to obtain an access token: https://dev.twitter.com/oauth/application-only
 */
interface TwitterApi {

    /**
     * Retrieve information about the logged user
     * Documentation: https://dev.twitter.com/rest/reference/get/account/verify_credentials
     *
     * @param apiKey Api Key
     * @param apiSecret Api Secret
     * @param token Token
     * @param tokenSecret Token secret
     * @return Logged user
     * @throws TwitterException if any.
     */
    User self(String apiKey, String apiSecret, String token, String tokenSecret) throws TwitterException;

    /**
     * Retrieve information about a user
     * Documentation: https://dev.twitter.com/rest/reference/get/account/verify_credentials
     *
     * @param apiKey Api Key
     * @param apiSecret Api Secret
     * @param token Token
     * @param tokenSecret Token secret
     * @param username Username
     * @return User with the given username
     * @throws TwitterException if any.
     */
    User user(String apiKey, String apiSecret, String token, String tokenSecret, String username) throws TwitterException;

    /**
     * Retrieve the followers of a user
     * Documentation: https://dev.twitter.com/rest/reference/get/followers/list
     *
     * @param apiKey Api Key
     * @param apiSecret Api Secret
     * @param token Token
     * @param tokenSecret Token secret
     * @param username    Username
     * @param limit       Number of followers to retrieve
     * @return List of followers
     * @throws TwitterException if any.
     */
    List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret,
                         String username, int limit) throws TwitterException;

    /**
     * Retrieve the followers of a user
     * Documentation: https://dev.twitter.com/rest/reference/get/followers/list
     *
     * @param apiKey Api Key
     * @param apiSecret Api Secret
     * @param token Token
     * @param tokenSecret Token secret
     * @param username    Username
     * @return List of followers
     * @throws TwitterException if any.
     */
    List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret,
                         String username) throws TwitterException;

    /**
     * Retrieve the user a user is following
     * Documentation: https://dev.twitter.com/rest/reference/get/friends/list
     *
     * @param apiKey Api Key
     * @param apiSecret Api Secret
     * @param token Token
     * @param tokenSecret Token secret
     * @param username    Username
     * @param limit       Number of followers to retrieve
     * @return List of users the user is following
     * @throws TwitterException if any.
     */
    List<User> following(String apiKey, String apiSecret, String token, String tokenSecret,
                         String username, int limit) throws TwitterException;

    /**
     * Retrieve the user a user is following
     * Documentation: https://dev.twitter.com/rest/reference/get/friends/list
     *
     * @param apiKey Api Key
     * @param apiSecret Api Secret
     * @param token Token
     * @param tokenSecret Token secret
     * @param username    Username
     * @return List of users the user is following
     * @throws TwitterException if any.
     */
    List<User> following(String apiKey, String apiSecret, String token, String tokenSecret,
                         String username) throws TwitterException;

}
