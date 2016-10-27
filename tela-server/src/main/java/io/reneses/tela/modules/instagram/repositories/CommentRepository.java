package io.reneses.tela.modules.instagram.repositories;


import io.reneses.tela.modules.instagram.models.Comment;

import java.util.List;

/**
 * Repository for Comment objects
 */
public interface CommentRepository {

    /**
     * Find the comment with the given ID
     *
     * @param commentId Comment ID
     * @return Comment
     */
    Comment find(long commentId);

    /**
     * Find all the comments of a media
     *
     * @param mediaId ID of the media
     * @return Comments of the given media
     */
    List<Comment> findAll(String mediaId);

    /**
     * Create or update a comment
     *
     * @param comment Comment ot be created or updated
     */
    void createOrUpdate(Comment comment);

    /**
     * Add comments to a media
     *
     * @param mediaId Media ID
     * @param comments Comments to add
     */
    void setComments(String mediaId, Iterable<Comment> comments);

}
