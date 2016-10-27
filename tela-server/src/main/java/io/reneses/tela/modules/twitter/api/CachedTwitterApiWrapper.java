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

    private String getUsernameFromAccessToken(String accessToken) throws TwitterException {
        String username = accessTokenUsernameCache.getUsername(accessToken);
        if (username != null)
            return username;
        return super.self(accessToken).getScreenName();
    }

    /** {@inheritDoc} */
    @Override
    public User self(String accessToken) throws TwitterException {
        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "self", accessToken))) {
            LOGGER.debug("[Cache] self({}) retrieved from the cache", accessToken);
            return repository.find(getUsernameFromAccessToken(accessToken));
        }
        return super.self(accessToken);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String accessToken, String username, int limit) throws TwitterException {
        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "followers", username, limit))) {
            LOGGER.debug("[Cache] followers({}, {}, {}) retrieved from the cache", accessToken, username, limit);
            return repository.findFollowers(username);
        }
        return super.followers(accessToken, username, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String accessToken, String username, int limit) throws TwitterException {
        if (history.isPresent(new HistoryEntry(TwitterTelaModule.NAME, "following", username, limit))) {
            LOGGER.debug("[Cache] following({}, {}, {}) retrieved from the cache", accessToken, username, limit);
            return repository.findFollowing(username);
        }
        return super.following(accessToken, username, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> friends(String accessToken, String username) throws TwitterException {
        HistoryEntry entry = new HistoryEntry(TwitterTelaModule.NAME, "friends", username);
        if (history.isPresent(entry)) {
            LOGGER.debug("[Cache] friends({}, {}) retrieved from the cache", accessToken, username);
            return repository.findFriends(username);
        }
        return super.friends(accessToken, username);
    }

}
