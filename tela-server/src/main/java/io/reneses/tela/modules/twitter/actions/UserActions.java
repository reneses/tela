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

    //-------------------------------- Aux --------------------------------//

    private String[] extractCredentials(String token) throws TwitterException {
        String[] twitterCredentials = token.split(":");
        if (twitterCredentials.length != 4) {
            throw new TwitterException("Illegal session", 401);
        }
        return twitterCredentials;
    }

    //-------------------------------- Actions --------------------------------//

    /**
     * Retrieve information about the logged user
     *
     * @param token Access token
     * @return Logged user
     * @throws TwitterException if any.
     */
    @Action(description = "Get the logged user", parameters = {"token"})
    @Schedulable(minimumDelay = 3600)
    public User self(String token) throws TwitterException {
        String[] credentials = extractCredentials(token);
        return api.self(credentials[0], credentials[1], credentials[2], credentials[3]);
    }

    /**
     * Retrieve the user a user is following
     *
     * @param token Access token
     * @param username    Username
     * @param limit       Number of followers to retrieve
     * @return List of users the user is following
     * @throws TwitterException if any.
     */
    @Action(description = "Get the users a user is following", parameters = {"token", "username", "limit"})
    @Schedulable(minimumDelay = 3600)
    public List<User> following(String token, String username, int limit) throws TwitterException {
        String[] credentials = extractCredentials(token);
        return api.following(credentials[0], credentials[1], credentials[2], credentials[3], username, limit);
    }

    /**
     * Retrieve the user a user is following
     *
     * @param token Access token
     * @param username    Username
     * @return List of users the user is following
     * @throws TwitterException if any.
     */
    @Action(description = "Get the users a user is following", parameters = {"token", "username"})
    @Schedulable(minimumDelay = 3600)
    public List<User> following(String token, String username) throws TwitterException {
        String[] credentials = extractCredentials(token);
        return api.following(credentials[0], credentials[1], credentials[2], credentials[3], username);
    }

    /**
     * Retrieve the followers of a user
     *
     * @param token Access token
     * @param username    Username
     * @param limit       Number of followers to retrieve
     * @return List of followers
     * @throws TwitterException if any.
     */
    @Action(description = "Get the followers of a user", parameters = {"token", "username", "limit"})
    @Schedulable(minimumDelay = 3600)
    public List<User> followers(String token, String username, int limit) throws TwitterException {
        String[] credentials = extractCredentials(token);
        return api.followers(credentials[0], credentials[1], credentials[2], credentials[3], username, limit);
    }

    /**
     * Retrieve the followers of a user
     *
     * @param token Access token
     * @param username    Username
     * @return List of followers
     * @throws TwitterException if any.
     */
    @Action(description = "Get the followers of a user", parameters = {"token", "username"})
    @Schedulable(minimumDelay = 3600)
    public List<User> followers(String token, String username) throws TwitterException {
        String[] credentials = extractCredentials(token);
        return api.followers(credentials[0], credentials[1], credentials[2], credentials[3], username);
    }

    /**
     * Retrieve the friends of a user (intersection between followers and following)
     *
     * @param token Access token
     * @param username    Username
     * @return User friends
     * @throws TwitterException if any.
     */
    @Action(description = "Get the friends of a user", parameters = {"token", "username"})
    @Schedulable(minimumDelay = 3600)
    public List<User> friends(String token, String username) throws TwitterException {
        String[] credentials = extractCredentials(token);
        return api.friends(credentials[0], credentials[1], credentials[2], credentials[3], username);
    }

}
