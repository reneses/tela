package io.reneses.tela.modules.instagram.repositories;


import io.reneses.tela.modules.instagram.models.Media;
import io.reneses.tela.modules.instagram.models.User;

import java.util.List;

/**
 * Repository for Media object
 */
public interface MediaRepository {

    /**
     * Find a media given its ID
     *
     * @param mediaId Media ID
     * @return Media
     */
    Media find(String mediaId);

    /**
     * Find all the media posted by an user
     *
     * @param userId User ID
     * @return Media posted by the given user
     */
    List<Media> findAll(long userId);

    /**
     * Find the latest media of an user
     *
     * @param userId User ID
     * @param limit Maximum number of media to return
     * @return Latest media of the given user
     */
    List<Media> findLatest(long userId, int limit);

    /**
     * Create or update a media
     *
     * @param media Media
     */
    void createOrUpdate(Media media);

    /**
     * Store the likes of a media
     *
     * @param mediaId Media ID
     * @param likes Likes
     */
    void setLikes(String mediaId, List<User> likes);

    /**
     * Find all the likes of a media
     *
     * @param mediaId Media ID
     * @return User who have liked the given media
     */
    List<User> findLikes(String mediaId);

}
