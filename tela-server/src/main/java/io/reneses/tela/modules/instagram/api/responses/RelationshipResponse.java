package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.api.models.Pagination;
import io.reneses.tela.modules.instagram.models.User;

import java.util.List;

/**
 * RelationshipResponse class.
 */
public class RelationshipResponse extends EnvelopeResponse {

    @JsonProperty("pagination")
    private Pagination pagination;

    @JsonProperty("data")
    private List<User> users;

    /**
     * Getter for the field <code>users</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<User> getUsers() {
        return users;
    }

    /**
     * Setter for the field <code>users</code>.
     *
     * @param users a {@link java.util.List} object.
     */
    public void setUsers(List<User> users) {
        this.users = users;
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
