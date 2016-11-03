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
    public User self(String apiKey, String apiSecret, String token, String tokenSecret) throws TwitterException {
        User user = api.self(apiKey, apiSecret, token, tokenSecret);
        repository.create(user);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "self", token));
        accessTokenUsernameCache.put(token, user.getScreenName());
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public User user(String apiKey, String apiSecret, String token, String tokenSecret,
                     String username) throws TwitterException {

        User user = api.user(apiKey, apiSecret, token, tokenSecret, username);
        repository.create(user);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "user", username));
        accessTokenUsernameCache.put(token, user.getScreenName());
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret, String username, int limit) throws TwitterException {
        List<User> followers = api.followers(apiKey, apiSecret, token, tokenSecret, username, limit);
        repository.setFollowers(followers, self(apiKey, apiSecret, token, tokenSecret));
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "followers", username, limit));
        return followers;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String apiKey, String apiSecret, String token, String tokenSecret, String username) throws TwitterException {
        return followers(apiKey, apiSecret, token, tokenSecret, username, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String apiKey, String apiSecret, String token, String tokenSecret, String username, int limit) throws TwitterException {
        List<User> following = api.following(apiKey, apiSecret, token, tokenSecret, username, limit);
        repository.setFollowing(self(apiKey, apiSecret, token, tokenSecret), following);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "following", username, limit));
        return following;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String apiKey, String apiSecret, String token, String tokenSecret, String username) throws TwitterException {
        return following(apiKey, apiSecret, token, tokenSecret, username, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> friends(String apiKey, String apiSecret, String token, String tokenSecret, String username) throws TwitterException {
        following(apiKey, apiSecret, token, tokenSecret, username);
        followers(apiKey, apiSecret, token, tokenSecret, username);
        history.add(new HistoryEntry(TwitterTelaModule.NAME, "friends", username));
        return repository.findFriends(username);
    }

}
