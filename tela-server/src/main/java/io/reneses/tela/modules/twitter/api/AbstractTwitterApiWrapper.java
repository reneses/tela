package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.core.history.History;
import io.reneses.tela.modules.twitter.cache.AccessTokenUsernameCache;
import io.reneses.tela.modules.twitter.cache.AccessTokenUsernameCacheImpl;
import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.models.User;
import io.reneses.tela.modules.twitter.repositories.UserRepository;
import io.reneses.tela.modules.twitter.repositories.OrientUserRepository;

import java.util.List;

/**
 * Wrapper for the Twitter API, storing the results
 */
public abstract class AbstractTwitterApiWrapper implements TwitterApi {

    TwitterApi api;
    UserRepository repository;
    History history;
    AccessTokenUsernameCache accessTokenUsernameCache;

    /**
     * Constructor injecting the required components
     *
     * @param api Twitter API
     * @param history History
     */
    AbstractTwitterApiWrapper(TwitterApi api, History history) {

        this.api = api;
        this.history = history;
        this.repository = new OrientUserRepository();
        this.accessTokenUsernameCache = new AccessTokenUsernameCacheImpl();

    }

    /**
     * Retrieve the friends of a user (intersection between followers and following)
     *
     * @param accessToken Access token
     * @param username    Username
     * @return User friends
     * @throws TwitterException if any.
     */
    public abstract List<User> friends(String accessToken, String username) throws TwitterException;

}
