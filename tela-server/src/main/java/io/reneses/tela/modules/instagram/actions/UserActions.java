package io.reneses.tela.modules.instagram.actions;

import io.reneses.tela.core.dispatcher.annotations.Schedulable;
import io.reneses.tela.modules.instagram.api.AbstractInstagramApiWrapper;
import io.reneses.tela.core.dispatcher.annotations.Action;
import io.reneses.tela.core.dispatcher.annotations.Module;
import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.models.Counts;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;

import java.util.List;


/**
 * User actions
 */
@Module("instagram")
public class UserActions extends InstagramActions {

    public UserActions(AbstractInstagramApiWrapper api) {
        super(api);
    }

    /**
     * Constructor for UserActions (required by the dispatcher)
     */
    public UserActions() {
    }

    /**
     * Get the information about the logged user
     *
     * @param accessToken Access token
     * @return Logged user
     * @throws InstagramException if any.
     */
    @Action(description = "Get the information about the logged user", parameters = {"token"})
    @Schedulable(minimumDelay = 3600)
    public User self(String accessToken) throws InstagramException {
        User user = api.self(accessToken);
        return user;
    }

    /**
     * Search an user by his username and return its basic information
     * This action is commonly used to obtain the ID of an user
     *
     * @param accessToken Access token
     * @param username    Username
     * @return User
     * @throws InstagramException if any.
     */
    @Action(name = "user-basic",
            description = "Get the basic information about an user",
            parameters = {"token", "username"})
    public User search(String accessToken, String username) throws InstagramException {
        return api.search(accessToken, username);
    }

    /**
     * Obtain the information about an user
     *
     * @param accessToken Access token
     * @param userId      User ID
     * @return User
     * @throws InstagramException if any.
     */
    @Action(description = "Get the information about an user", parameters = {"token", "userId"})
    public User user(String accessToken, long userId) throws InstagramException {
        return api.user(accessToken, userId);
    }

    /**
     * Retrieve the information about an user
     *
     * @param accessToken Access token
     * @param username    User username
     * @return Username with the given username
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the information about an user", parameters = {"token", "username"})
    public User user(String accessToken, String username) throws InstagramException {
        return api.user(accessToken, username);
    }

    /**
     * Retrieve the users the logged user is following
     *
     * @param accessToken Access token
     * @param limit       Maximum number of users to return
     * @return Users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the following of the logged user", parameters = {"token", "limit"})
    public List<User> following(String accessToken, int limit) throws InstagramException {
        return api.following(accessToken, limit);
    }

    /**
     * Retrieve the users the logged user is following
     *
     * @param accessToken Access token
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the following of the logged user", parameters = {"token"})
    public List<User> following(String accessToken) throws InstagramException {
        return following(accessToken, Integer.MAX_VALUE);
    }

    /**
     * Retrieve the followers of the logged user
     *
     * @param accessToken Access token
     * @param limit       Maximum number of users to return
     * @return Followers of the logged user
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the followers of the logged user", parameters = {"token", "limit"})
    public List<User> followers(String accessToken, int limit) throws InstagramException {
        return api.followers(accessToken, limit);
    }

    /**
     * Retrieve the followers of the logged user
     *
     * @param accessToken Access token
     * @return All the followers of the logged user
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the followers of the logged user", parameters = {"token"})
    public List<User> followers(String accessToken) throws InstagramException {
        return followers(accessToken, Integer.MAX_VALUE);
    }

    /**
     * Retrieve the relationship of the logged user with other user
     *
     * @param accessToken Access token
     * @param userId      User ID of the target user
     * @return Relationship between both users
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the relationship between the logged user and a user", parameters = {"token", "userId"})
    public UserRelationship relationship(String accessToken, long userId) throws InstagramException {
        return api.relationship(accessToken, userId);
    }

    /**
     * Retrieve the relationship of the logged user with other user
     *
     * @param accessToken Access token
     * @param username    Username
     * @return Relationship between both users
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the relationship between the logged user and a user", parameters = {"token", "username"})
    public UserRelationship relationship(String accessToken, String username) throws InstagramException {
        return api.relationship(accessToken, username);
    }

    /**
     * Retrieve the friends of the logged user (intersections between followers and following)
     *
     * @param accessToken Access token
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    @Action(description = "Get the findFriends of the logged user", parameters = {"token"})
    public List<User> friends(String accessToken) throws InstagramException {
        return api.friends(accessToken);

    }

    /**
     * Retrieve the counts of an user
     *
     * @param accessToken Access token
     * @param userId      User ID
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    @Action(name = "counts", description = "", parameters = {"token", "userId"})
    public List<Counts> counts(String accessToken, long userId) throws InstagramException {
        return api.counts(accessToken, userId);
    }

    /**
     * Retrieve the counts of an user
     *
     * @param accessToken Access token
     * @param username    User username
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    @Action(name = "counts", description = "", parameters = {"token", "username"})
    public List<Counts> counts(String accessToken, String username) throws InstagramException {
        return api.counts(accessToken, username);
    }

}
