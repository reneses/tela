package io.reneses.tela.modules.instagram.api.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.instagram.models.User;

/**
 * UserResponse class.
 */
public class UserResponse extends EnvelopeResponse {

    @JsonProperty("data")
    private User user;

    /**
     * Getter for the field <code>user</code>.
     *
     * @return a {@link User} object.
     */
    public User getUser() {
        return user;
    }

    /**
     * Setter for the field <code>user</code>.
     *
     * @param user a {@link User} object.
     */
    public void setUser(User user) {
        this.user = user;
    }
}
