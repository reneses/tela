package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.models.HistoryEntry;
import io.reneses.tela.modules.instagram.InstagramTelaModule;
import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.api.exceptions.InvalidAccessTokenException;
import io.reneses.tela.modules.instagram.api.exceptions.UserNotExistsException;
import io.reneses.tela.modules.instagram.models.*;

import java.util.List;

/**
 * Instagram API Wrapper
 */
class InstagramApiWrapper extends AbstractInstagramApiWrapper {

    /**
     * Constructor with DI
     *
     * @param api     Instagram API
     * @param history History
     */
    InstagramApiWrapper(InstagramApi api, History history) {
        super(api, history);
    }

    //------------------------------ Caching functions ------------------------------//

    Long getIdFromAccessToken(String accessToken) throws InstagramException {
        Long id = accessTokenIdCache.getId(accessToken);
        if (id != null)
            return id;
        return selfFinal(accessToken).getId();
    }

    private Long getIdFromUsername(String accessToken, String username) throws InstagramException {
        Long userId = usernameIdCache.getId(username);
        if (userId != null)
            return userId;
        userId = searchFinal(accessToken, username).getId();
        usernameIdCache.put(username, userId);
        return userId;
    }

    private void cacheUser(User user) {
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "search", user.getUsername()));
        usernameIdCache.put(user.getUsername(), user.getId());
    }

    // This method exists in order to avoid infinity recursive calls
    private User selfFinal(String accessToken) throws InstagramException {
        User user = api.self(accessToken);
        if (user == null)
            throw new InvalidAccessTokenException(accessToken);
        userRepository.createOrUpdate(user);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "self", accessToken));
        accessTokenIdCache.put(accessToken, user.getId());
        cacheUser(user);
        return user;
    }

    //------------------------------ API ------------------------------//

    /** {@inheritDoc} */
    @Override
    public User self(String accessToken) throws InstagramException {
        return selfFinal(accessToken);
    }

    // This method exists in order to avoid infinity recursive calls
    private User searchFinal(String accessToken, String username) throws InstagramException {
        User user = api.search(accessToken, username);
        if (user == null)
            throw new UserNotExistsException(username);
        userRepository.createOrUpdate(user);
        cacheUser(user);
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public User search(String accessToken, String username) throws InstagramException {
        return searchFinal(accessToken, username);
    }

    /** {@inheritDoc} */
    @Override
    public User user(String accessToken, long userId) throws InstagramException {
        User user = api.user(accessToken, userId);
        userRepository.createOrUpdate(user);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "user", userId));
        cacheUser(user);
        return user;
    }

    /** {@inheritDoc} */
    @Override
    public User user(String accessToken, String username) throws InstagramException {
        return user(accessToken, getIdFromUsername(accessToken, username));
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String accessToken, int limit) throws InstagramException {
        List<User> users = api.followers(accessToken, limit);
        userRepository.setFollowers(users, self(accessToken));
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "followers", accessToken, limit));
        users.forEach(this::cacheUser);
        return users;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String accessToken) throws InstagramException {
        return followers(accessToken, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String accessToken, int limit) throws InstagramException {
        List<User> users = api.following(accessToken, limit);
        userRepository.setFollowing(self(accessToken), users);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "following", accessToken, limit));
        users.forEach(this::cacheUser);
        return users;
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String accessToken) throws InstagramException {
        return following(accessToken, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> friends(String accessToken) throws InstagramException {
        followers(accessToken);
        following(accessToken);
        List<User> result = userRepository.findFriends(getIdFromAccessToken(accessToken));
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "friends", accessToken));
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public List<Counts> counts(String accessToken, long userId) throws InstagramException {
        user(accessToken, userId);
        List<Counts> counts = userRepository.counts(userId);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "counts", userId));
        return counts;
    }

    /** {@inheritDoc} */
    @Override
    public List<Counts> counts(String accessToken, String username) throws InstagramException {
        return counts(accessToken, getIdFromUsername(accessToken, username));
    }

    /** {@inheritDoc} */
    @Override
    public UserRelationship relationship(String accessToken, long userId) throws InstagramException {
        UserRelationship relationship = api.relationship(accessToken, userId);
        userRepository.createRelationship(self(accessToken), relationship, userId);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "relationship", accessToken, userId));
        return relationship;
    }

    /** {@inheritDoc} */
    @Override
    public UserRelationship relationship(String accessToken, String username) throws InstagramException {
        return relationship(accessToken, getIdFromUsername(accessToken, username));
    }

    /** {@inheritDoc} */
    @Override
    public List<Media> selfMedia(String accessToken, int limit) throws InstagramException {
        List<Media> media = api.selfMedia(accessToken, limit);
        media.forEach(mediaRepository::createOrUpdate);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "selfMedia", accessToken, limit));
        return media;
    }

    /** {@inheritDoc} */
    @Override
    public List<Media> selfMedia(String accessToken) throws InstagramException {
        return selfMedia(accessToken, -1);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> likes(String accessToken, String mediaId) throws InstagramException {
        List<User> likes = api.likes(accessToken, mediaId);
        mediaRepository.setLikes(mediaId,likes);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "likes", mediaId));
        return likes;
    }

    /** {@inheritDoc} */
    @Override
    public List<Comment> comments(String accessToken, String mediaId) throws InstagramException {
        List<Comment> comments = api.comments(accessToken, mediaId);
        commentRepository.setComments(mediaId, comments);
        history.add(new HistoryEntry(InstagramTelaModule.NAME, "comments", mediaId));
        return comments;
    }

}
