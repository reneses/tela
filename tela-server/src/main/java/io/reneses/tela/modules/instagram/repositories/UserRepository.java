package io.reneses.tela.modules.instagram.repositories;


import io.reneses.tela.modules.instagram.models.Counts;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;

import java.util.List;

/**
 * Repository for User objects
 */
public interface UserRepository {

    /**
     * Find a user
     *
     * @param userId    User ID
     * @param withCount Whether the latest UserCount should be retrieved or not
     * @return User with the given ID
     */
    User find(long userId, boolean withCount);

    /**
     * Find a user
     *
     * @param userId User ID
     * @return User with the given ID
     */
    User find(long userId);

    /**
     * Find a user
     *
     * @param username  User username
     * @param withCount Whether the latest UserCount should be retrieved or not
     * @return User with the given username
     */
    User find(String username, boolean withCount);

    /**
     * Find a user
     *
     * @param username User username
     * @return User with the given username
     */
    User find(String username);

    /**
     * Find all the users
     *
     * @param withCount Whether the latest UserCount should be retrieved or not
     * @return All the users
     */
    List<User> findAll(boolean withCount);

    /**
     * Find all the users
     *
     * @return All the users
     */
    List<User> findAll();

    /**
     * Create a user
     *
     * @param user User
     */
    void createOrUpdate(User user);

    /**
     * Store the followers of a user
     *
     * @param followers Followers
     * @param followed  User being followed
     */
    void setFollowers(List<User> followers, User followed);

    /**
     * Store the users that one user is following
     *
     * @param follower Follower
     * @param followed Users being followed
     */
    void setFollowing(User follower, List<User> followed);

    /**
     * Find the followers of a user
     *
     * @param followedId Followed user ID
     * @return Followers of the user
     */
    List<User> findFollowers(long followedId);

    /**
     * Find the users a user is following
     *
     * @param followerId Follower user ID
     * @return Users being followed
     */
    List<User> findFollowing(long followerId);

    /**
     * Find the friends of a user (intersection of followers and following)
     *
     * @param userId User ID
     * @return Friends of the given user
     */
    List<User> findFriends(long userId);

    /**
     * Obtain the counts of a user
     *
     * @param userId User ID
     * @return Counts of the given user
     */
    List<Counts> counts(long userId);

    /**
     * Create and store a relationship between two users
     *
     * @param source       Source user
     * @param relationship Relationship between both user
     * @param targetId     Target ID
     */
    void createRelationship(User source, UserRelationship relationship, long targetId);

    /**
     * Create and store a relationship between two users
     *
     * @param sourceUserId Source user ID
     * @param targetUserId Target user ID
     * @return Latest relationship between two users
     */
    UserRelationship findLatestRelationship(long sourceUserId, long targetUserId);

    /**
     * Create and store a relationship between two users
     *
     * @param sourceUserId Source user ID
     * @param targetUserId Target user ID
     * @return Latest relationship between two users
     */
    List<UserRelationship> findRelationships(long sourceUserId, long targetUserId);

}
