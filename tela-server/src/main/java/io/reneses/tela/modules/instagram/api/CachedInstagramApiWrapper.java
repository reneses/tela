package io.reneses.tela.modules.instagram.api;

import io.reneses.tela.core.history.History;
import io.reneses.tela.core.history.models.HistoryEntry;
import io.reneses.tela.modules.instagram.InstagramTelaModule;
import io.reneses.tela.modules.instagram.api.exceptions.InstagramException;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;
import io.reneses.tela.modules.instagram.models.UserRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Cached version of the API Wrapper
 */
class CachedInstagramApiWrapper extends InstagramApiWrapper {

    private static final Logger LOGGER = LoggerFactory.getLogger(CachedInstagramApiWrapper.class);

    /**
     * Constructor with DI
     *
     * @param api     Instagram API
     * @param history History
     */
    CachedInstagramApiWrapper(InstagramApi api, History history) {
        super(api, history);
    }

    /** {@inheritDoc} */
    @Override
    public User self(String accessToken) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "self", accessToken))) {
            LOGGER.debug("[Cache] self({}) retrieved from the cache", accessToken);
            long selfId = getIdFromAccessToken(accessToken);
            return userRepository.find(selfId);
        }
        return super.self(accessToken);
    }

    /** {@inheritDoc} */
    @Override
    public User search(String accessToken, String username) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "search", username))) {
            LOGGER.debug("[Cache] search({}) retrieved from the cache", username);
            return userRepository.find(username);
        }
        return super.search(accessToken, username);
    }

    /** {@inheritDoc} */
    @Override
    public User user(String accessToken, long userId) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "user", userId))) {
            LOGGER.debug("[Cache] user({}) retrieved from the cache", userId);
            return userRepository.find(userId, true);
        }
        return super.user(accessToken, userId);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> followers(String accessToken, int limit) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "followers", accessToken, limit))) {
            LOGGER.debug("[Cache] followers({}) retrieved from the cache", accessToken);
            long selfId = getIdFromAccessToken(accessToken);
            return userRepository.findFollowers(selfId);
        }
        return super.followers(accessToken, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> following(String accessToken, int limit) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "following", accessToken, limit))) {
            LOGGER.debug("[Cache] following({}) retrieved from the cache", accessToken);
            long selfId = getIdFromAccessToken(accessToken);
            return userRepository.findFollowing(selfId);
        }
        return super.following(accessToken, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> friends(String accessToken) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "friends", accessToken))) {
            LOGGER.debug("[Cache] friends({}) retrieved from the cache", accessToken);
            long selfId = getIdFromAccessToken(accessToken);
            return userRepository.findFriends(selfId);
        }
        return super.friends(accessToken);
    }

    /** {@inheritDoc} */
    @Override
    public List<Media> selfMedia(String accessToken, int limit) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "selfMedia", accessToken, limit))) {
            LOGGER.debug("[Cache] selfMedia({}) retrieved from the cache", accessToken);
            return mediaRepository.findLatest(getIdFromAccessToken(accessToken), limit);
        }
        return super.selfMedia(accessToken, limit);
    }

    /** {@inheritDoc} */
    @Override
    public List<User> likes(String accessToken, String mediaId) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "likes", mediaId))) {
            LOGGER.debug("[Cache] likes({}) retrieved from the cache", mediaId);
            return mediaRepository.findLikes(mediaId);
        }
        return super.likes(accessToken, mediaId);
    }

    /** {@inheritDoc} */
    @Override
    public List<Comment> comments(String accessToken, String mediaId) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "comments", mediaId))) {
            LOGGER.debug("[Cache] comments({}) retrieved from the cache", mediaId);
            return commentRepository.findAll(mediaId);
        }
        return super.comments(accessToken, mediaId);
    }

    /** {@inheritDoc} */
    @Override
    public UserRelationship relationship(String accessToken, long userId) throws InstagramException {
        if (history.isPresent(new HistoryEntry(InstagramTelaModule.NAME, "relationship", accessToken, userId))) {
            LOGGER.debug("[Cache] relationship({}, {}) retrieved from the cache", accessToken, userId);
            long selfId = getIdFromAccessToken(accessToken);
            return userRepository.findLatestRelationship(selfId, userId);
        }
        return super.relationship(accessToken, userId);
    }

}
