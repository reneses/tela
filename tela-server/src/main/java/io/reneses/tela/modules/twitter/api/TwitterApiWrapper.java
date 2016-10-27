package io.reneses.tela.modules.twitter.api;

import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.models.HistoryEntry;
import io.reneses.tela.modules.twitter.TwitterTelaModule;
import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.models.User;

import java.util.List;

/**
 * Twitter API Wrapper implementation
 */
class TwitterApiWrapper extends AbstractTwitterApiWrapper {

    /**
     * Constructor injecting the required components
     *
     * @param api                      Twitter API
     * @param history                  History
     */
    TwitterApiWrapper(TwitterApi api, History history) {
        super(api, history);
    }

    /** {@inheritDoc} */
    @Override
    public User self(String accessToken) throws TwitterException {
        User user = api.self(accessToken);
        repository.create(user);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "self", accessToken));
        accessTokenUsernameCache.put(accessToken, user.getScreenName());
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String accessToken, String username, int limit) throws TwitterException {
        List<User> followers = api.followers(accessToken, username, limit);
        repository.setFollowers(followers, self(accessToken));
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "followers", username, limit));
        return followers;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String accessToken, String username) throws TwitterException {
        return followers(accessToken, username, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String accessToken, String username, int limit) throws TwitterException {
        List<User> following = api.following(accessToken, username, limit);
        repository.setFollowing(self(accessToken), following);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "following", username, limit));
        return following;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String accessToken, String username) throws TwitterException {
        return following(accessToken, username, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> friends(String accessToken, String username) throws TwitterException {
        following(accessToken, username);
        followers(accessToken, username);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "friends", username));
        return repository.findFriends(username);
    }

}
