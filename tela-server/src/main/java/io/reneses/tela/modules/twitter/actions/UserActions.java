package io.reneses.tela.modules.twitter.actions;

import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.core.dispatcher.annotations.Schedulable;
import io.reneses.tela.modules.twitter.api.AbstractTwitterApiWrapper;
import io.reneses.tela.modules.twitter.api.TwitterApiWrapperFactory;
import io.reneses.tela.modules.twitter.api.exceptions.TwitterException;
import io.reneses.tela.modules.twitter.models.User;

import java.util.List;

/**
 * Actions related with users
 */
@Module("twitter")
public class UserActions {

    private AbstractTwitterApiWrapper api;

    /**
     * Constructor with DI
     *
     * @param api API Wrapper
     */
    public UserActions(AbstractTwitterApiWrapper api) {
        this.api = api;
    }

    /**
     * The default constructor is required for the action dispatcher
     */
    public UserActions() {
        this(TwitterApiWrapperFactory.create());
    }

    //-------------------------------- Actions --------------------------------//

    /**
     * Retrieve information about the logged user
     *
     * @param accessToken Access token
     * @return Logged user
     * @throws TwitterException if any.
     */
    @Action(description = "Get the logged user", parameters = {"token"})
    @Schedulable(minimumDelay = 3600)
    public User self(String accessToken) throws TwitterException {
        return api.self(accessToken);
    }

    /**
     * Retrieve the user a user is following
     *
     * @param accessToken Access token
     * @param username    Username
     * @param limit       Number of followers to retrieve
     * @return List of users the user is following
     * @throws TwitterException if any.
     */
    @Action(description = "Get the users a user is following", parameters = {"token", "username", "limit"})
    @Schedulable(minimumDelay = 3600)
    public List<User> following(String accessToken, String username, int limit) throws TwitterException {
        return api.following(accessToken, username, limit);
    }

    /**
     * Retrieve the user a user is following
     *
     * @param accessToken Access token
     * @param username    Username
     * @return List of users the user is following
     * @throws TwitterException if any.
     */
    @Action(description = "Get the users a user is following", parameters = {"token", "username"})
    @Schedulable(minimumDelay = 3600)
    public List<User> following(String accessToken, String username) throws TwitterException {
        return api.following(accessToken, username);
    }

    /**
     * Retrieve the followers of a user
     *
     * @param accessToken Access token
     * @param username    Username
     * @param limit       Number of followers to retrieve
     * @return List of followers
     * @throws TwitterException if any.
     */
    @Action(description = "Get the followers of a user", parameters = {"token", "username", "limit"})
    @Schedulable(minimumDelay = 3600)
    public List<User> followers(String accessToken, String username, int limit) throws TwitterException {
        return api.followers(accessToken, username, limit);
    }

    /**
     * Retrieve the followers of a user
     *
     * @param accessToken Access token
     * @param username    Username
     * @return List of followers
     * @throws TwitterException if any.
     */
    @Action(description = "Get the followers of a user", parameters = {"token", "username"})
    @Schedulable(minimumDelay = 3600)
    public List<User> followers(String accessToken, String username) throws TwitterException {
        return api.followers(accessToken, username);
    }

    /**
     * Retrieve the friends of a user (intersection between followers and following)
     *
     * @param accessToken Access token
     * @param username    Username
     * @return User friends
     * @throws TwitterException if any.
     */
    @Action(description = "Get the friends of a user", parameters = {"token", "username"})
    @Schedulable(minimumDelay = 3600)
    public List<User> friends(String accessToken, String username) throws TwitterException {
        return api.friends(accessToken, username);
    }

}
