package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.api.models.Pagination;
import io.reneses.tela.modules.instagram.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * LikesResponse class.
 */
public class LikesResponse extends EnvelopeResponse {

    @JsonProperty("data")
    private List<User> likes;

    @JsonProperty("pagination")
    private Pagination pagination;

    /**
     * Constructor for LikesResponse.
     */
    public LikesResponse() {
        this.likes = new ArrayList<>();
    }

    /**
     * Getter for the field <code>likes</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<User> getLikes() {
        return likes;
    }

    /**
     * Setter for the field <code>likes</code>.
     *
     * @param likes a {@link java.util.List} object.
     */
    public void setLikes(List<User> likes) {
        this.likes = likes;
    }


    /**
     * Getter for the field <code>pagination</code>.
     *
     * @return a {@link io.reneses.tela.modules.instagram.api.models.Pagination} object.
     */
    public Pagination getPagination() {
        return pagination;
    }

    /**
     * Setter for the field <code>pagination</code>.
     *
     * @param pagination a {@link io.reneses.tela.modules.instagram.api.models.Pagination} object.
     */
    public void setPagination(Pagination pagination) {
        this.pagination = pagination;
    }
}
