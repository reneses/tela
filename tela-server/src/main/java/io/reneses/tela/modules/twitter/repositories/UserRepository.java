package io.reneses.tela.modules.twitter.repositories;

import io.reneses.tela.modules.twitter.models.User;
import java.util.List;

/**
 * User Repository
 */
public interface UserRepository {

    /**
     * Find a user by its username (or screen name)
     *
     * @param username Username
     * @return User with the given username, null if it does not exist
     */
    User find(String username);

    /**
     * Create an user
     *
     * @param user User
     */
    void create(User user);

    /**
     * Save the followers of an user. All the users will be updated/created.
     *
     * @param followers Following users
     * @param followed Followed user
     */
    void setFollowers(Iterable<User> followers, User followed);

    /**
     * Save the users an user is following. All the users will be updated/created.
     *
     * @param followers Following user
     * @param followed Followed users
     */
    void setFollowing(User followers, Iterable<User> followed);

    /**
     * Find the followers of an user
     *
     * @param username Username
     * @return User followers
     */
    List<User> findFollowers(String username);

    /**
     * Find the users an user is following
     *
     * @param username Username
     * @return Users the user is following
     */
    List<User> findFollowing(String username);

    /**
     * Find the friends of an user, this is, the intersection of followers and following
     *
     * @param username Username
     * @return User friends
     */
    List<User> findFriends(String username);

}
