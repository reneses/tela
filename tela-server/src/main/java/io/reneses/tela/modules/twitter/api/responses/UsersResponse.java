package io.reneses.tela.modules.twitter.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.twitter.models.User;

import java.util.List;

/**
 * Response containing a list of users
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsersResponse {

    private List<User> users;

    @JsonProperty("next_cursor_str")
    private String nextCursor;

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

    public String getNextCursor() {
        return nextCursor;
    }

    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }
}
