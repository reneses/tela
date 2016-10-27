package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.api.models.Pagination;
import io.reneses.tela.modules.instagram.models.Comment;
import io.reneses.tela.modules.instagram.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * LikesResponse class.
 */
public class CommentsResponse extends EnvelopeResponse {

    @JsonProperty("data")
    private List<Comment> comments;

    @JsonProperty("pagination")
    private Pagination pagination;

    /**
     * Constructor for LikesResponse.
     */
    public CommentsResponse() {
        this.comments = new ArrayList<>();
    }

    /**
     * Getter for the field <code>likes</code>.
     *
     * @return a {@link List} object.
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Setter for the field <code>likes</code>.
     *
     * @param comments a {@link List} object.
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }


    /**
     * Getter for the field <code>pagination</code>.
     *
     * @return a {@link Pagination} object.
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Setter for the field <code>pagination</code>.
     *
     * @param pagination a {@link Pagination} object.
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
