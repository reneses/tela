package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.models.HistoryEntry;
import io.reneses.tela.modules.twitter.TwitterTelaModule;
import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Cached version of the TwitterApiWrapper
 */
class CachedTwitterApiWrapper extends TwitterApiWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedTwitterApiWrapper.class);

    /**
     * Constructor injecting the required components
     *
     * @param api     Twitter API
     * @param history History
     */
    CachedTwitterApiWrapper(TwitterApi api, History history) {
        super(api, history);
    }

    private String getUsernameFromAccessToken(String apiKey, String apiSecret, String token, String tokenSecret)
            throws TwitterException {

        String username = accessTokenUsernameCache.getUsername(token);
        if (username != null)
            return username;
        return super.self(apiKey, apiSecret, token, tokenSecret).getScreenName();
    }

    /** {@inheritDoc} */
    @Override
    public User self(String apiKey, String apiSecret, String token, String tokenSecret) throws TwitterException {
        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "self", token))) {
            LOGGER.debug("[Cache] self({}) retrieved from the cache", token);
            return repository.find(getUsernameFromAccessToken(apiKey, apiSecret, token, tokenSecret));
        }
        return super.self(apiKey, apiSecret, token, tokenSecret);
    }

    /** {@inheritDoc} */
    @Override
    public User user(String apiKey, String apiSecret, String token, String tokenSecret,
                     String username) throws TwitterException {

        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "user", username))) {
            LOGGER.debug("[Cache] user({}) retrieved from the cache", username);
            return repository.find(getUsernameFromAccessToken(apiKey, apiSecret, token, tokenSecret));
        }
        return super.user(apiKey, apiSecret, token, tokenSecret, username);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret,
                                String username, int limit) throws TwitterException {

        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "followers", username, limit))) {
            LOGGER.debug("[Cache] followers({}, {}) retrieved from the cache", username, limit);
            return repository.findFollowers(username);
        }
        return super.followers(apiKey, apiSecret, token, tokenSecret, username, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String apiKey, String apiSecret, String token, String tokenSecret,
                                String username, int limit) throws TwitterException {

        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "following", username, limit))) {
            LOGGER.debug("[Cache] following({}, {}) retrieved from the cache", username, limit);
            return repository.findFollowing(username);
        }
        return super.following(apiKey, apiSecret, token, tokenSecret, username, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> friends(String apiKey, String apiSecret, String token,
                              String tokenSecret, String username) throws TwitterException {

        HistoryEntry entry = new HistoryEntry(TwitterTelaModule.NAME, "friends", username);
        if (history.isPresent(entry)) {
            LOGGER.debug("[Cache] friends({}) retrieved from the cache", username);
            return repository.findFriends(username);
        }
        return super.friends(apiKey, apiSecret, token, tokenSecret, username);
    }

}
