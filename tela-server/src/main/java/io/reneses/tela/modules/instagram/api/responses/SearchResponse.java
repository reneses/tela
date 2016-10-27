package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.models.User;

import java.util.List;

/**
 * SearchResponse class.
 */
public class SearchResponse extends EnvelopeResponse {

    @JsonProperty("data")
    private List<User> users;

    /**
     * getUser.
     *
     * @param username a {@link java.lang.String} object.
     * @return a {@link User} object.
     */
    public User getUser(String username) {
        if (users == null)
            return null;
        for (User user : users) {
            if (user.getUsername().equalsIgnoreCase(username))
                return user;
        }
        return null;
    }

    /**
     * Setter for the field <code>users</code>.
     *
     * @param users a {@link List} object.
     */
    public void setUsers(List<User> users) {
        this.users = users;
    }
}
