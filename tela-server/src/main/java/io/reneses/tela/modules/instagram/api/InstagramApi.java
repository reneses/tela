package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;

import java.util.List;


/**
 * Abstract Instagram API communicating with the Instagram platform
 * General documentation: https://www.instagram.com/developer/
 * Create credentials: https://www.instagram.com/developer/clients/manage/
 * Auth flow to obtain an access token: https://www.instagram.com/developer/authentication/
 */
interface InstagramApi {

    /**
     * Retrieve the basic information about the logged user
     * Documentation: https://www.instagram.com/developer/endpoints/users/
     *
     * @param accessToken Access token
     * @return Logged user
     * @throws InstagramException If any exception is thrown
     */
    User self(String accessToken) throws InstagramException;

    /**
     * Retrieve the basic information of a user given its username
     * Documentation: https://www.instagram.com/developer/endpoints/users/
     *
     * @param accessToken Access token
     * @return Username with the given username
     * @throws InstagramException If the user is not found
     * @param username a {@link java.lang.String} object.
     */
    User search(String accessToken, String username) throws InstagramException;

    /**
     * Retrieve the information about a user
     * Documentation: https://www.instagram.com/developer/endpoints/users/
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param userId      User ID
     * @return Username with the given username
     * @throws InstagramException If any exception is thrown
     */
    User user(String accessToken, long userId) throws InstagramException;

    /**
     * Retrieve the followers of the logged user
     * Documentation: https://www.instagram.com/developer/endpoints/relationships/
     * Required scope: follower_list
     *
     * @param accessToken Access token
     * @param limit       Maximum number of users to return
     * @return Followers of the logged user
     * @throws InstagramException If any exception is thrown
     */
    List<User> followers(String accessToken, int limit) throws InstagramException;

    /**
     * Retrieve the followers of the logged user
     * Documentation: https://www.instagram.com/developer/endpoints/relationships/
     * Required scope: follower_list
     *
     * @param accessToken Access token
     * @return All the followers of the logged user
     * @throws InstagramException If any exception is thrown
     */
    List<User> followers(String accessToken) throws InstagramException;

    /**
     * Retrieve the users the logged user is following
     * Documentation: https://www.instagram.com/developer/endpoints/relationships/
     * Required scope: follower_list
     *
     * @param accessToken Access token
     * @param limit       Maximum number of users to return
     * @return Users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    List<User> following(String accessToken, int limit) throws InstagramException;

    /**
     * Retrieve the users the logged user is following
     * Documentation: https://www.instagram.com/developer/endpoints/relationships/
     * Required scope: follower_list
     *
     * @param accessToken Access token
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    List<User> following(String accessToken) throws InstagramException;

    /**
     * Retrieve the relationship of the logged user with other user
     * - Outgoing: 'follows', 'requested' or 'none'.
     * - Incoming: 'followed_by', 'requested_by', 'blocked_by_you' or 'none'.
     * Documentation: https://www.instagram.com/developer/endpoints/relationships/
     * Required scope: follower_list
     *
     * @param accessToken Access token
     * @param userId      User ID of the target user
     * @return Relationship between both users
     * @throws InstagramException If any exception is thrown
     */
    UserRelationship relationship(String accessToken, long userId) throws InstagramException;

    /**
     * Retrieve the latest media of the logged user
     * Documentation: https://www.instagram.com/developer/endpoints/media/
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param limit       Maximum number of media to return
     * @return Media of the logged user
     * @throws InstagramException If any exception is thrown
     */
    List<Media> selfMedia(String accessToken, int limit) throws InstagramException;

    /**
     * Retrieve the latest media of the logged user
     * Documentation: https://www.instagram.com/developer/endpoints/media/
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @return All the media of the logged user
     * @throws InstagramException If any exception is thrown
     */
    List<Media> selfMedia(String accessToken) throws InstagramException;

    /**
     * Retrieve the likes of a media
     * Documentation: https://www.instagram.com/developer/endpoints/likes/
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param mediaId     Media ID
     * @return Likes of the media
     * @throws InstagramException If any exception is thrown
     */
    List<User> likes(String accessToken, String mediaId) throws InstagramException;

    /**
     * Retrieve the comments of a media
     * Documentation: https://www.instagram.com/developer/endpoints/comments/
     * Required scope: public_content
     *
     * @param accessToken Access token
     * @param mediaId     Media ID
     * @return Likes of the media
     * @throws InstagramException If any exception is thrown
     */
    List<Comment> comments(String accessToken, String mediaId) throws InstagramException;

}
