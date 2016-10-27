package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.core.history.History;
import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.models.*;
import io.reneses.tela.modules.instagram.cache.AccessTokenIdCache;
import io.reneses.tela.modules.instagram.cache.AccessTokenIdCacheImpl;
import io.reneses.tela.modules.instagram.cache.UsernameIdCache;
import io.reneses.tela.modules.instagram.cache.UsernameIdCacheImpl;
import io.reneses.tela.modules.instagram.repositories.*;

import java.util.List;

/**
 * Abstract InstagramApiWrapper, providing common functionality
 */
public abstract class AbstractInstagramApiWrapper implements InstagramApi {

    protected InstagramApi api;
    protected History history;
    protected UserRepository userRepository;
    protected MediaRepository mediaRepository;
    protected CommentRepository commentRepository;
    protected AccessTokenIdCache accessTokenIdCache;
    protected UsernameIdCache usernameIdCache;

    /**
     * Constructor with DI
     *
     * @param api     Instagram API
     * @param history History
     */
    AbstractInstagramApiWrapper(InstagramApi api, History history) {
        this.api = api;
        this.history = history;
        this.userRepository = new OrientUserRepository();
        this.mediaRepository = new OrientMediaRepository();
        this.commentRepository = new OrientCommentRepository();
        this.accessTokenIdCache = new AccessTokenIdCacheImpl();
        this.usernameIdCache = new UsernameIdCacheImpl();
    }

    /**
     * Retrieve the information about an user
     *
     * @param accessToken Access token
     * @param username    User username
     * @return Username with the given username
     * @throws InstagramException If any exception is thrown
     */
    public abstract User user(String accessToken, String username) throws InstagramException;

    /**
     * Retrieve the friends of the logged user (intersections between followers and following)
     *
     * @param accessToken Access token
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    public abstract List<User> friends(String accessToken) throws InstagramException;

    /**
     * Retrieve the counts of an user
     *
     * @param accessToken Access token
     * @param userId    User ID
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    public abstract List<Counts> counts(String accessToken, long userId) throws InstagramException;

    /**
     * Retrieve the counts of an user
     *
     * @param accessToken Access token
     * @param username    User username
     * @return All the users the logged user is following
     * @throws InstagramException If any exception is thrown
     */
    public abstract List<Counts> counts(String accessToken, String username) throws InstagramException;

    /**
     * Retrieve the relationship of the logged user with other user
     *
     * @param accessToken Access token
     * @param username    User username of the target user
     * @return Relationship between both users
     * @throws InstagramException If any exception is thrown
     */
    public abstract UserRelationship relationship(String accessToken, String username) throws InstagramException;

}
