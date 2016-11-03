package io.reneses.tela.modules.twitter.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.reneses.tela.modules.twitter.api.models.ApiError;
import io.reneses.tela.modules.twitter.models.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Response containing a list of users
 */
@JsonIgnoreProperties(ignoreUnknown=true)
public class UsersResponse {

    private List<User> users;
    private List<ApiError> errors;

    @JsonProperty("next_cursor_str")
    private String nextCursor;

    /**
     * Getter for the field <code>users</code>.
     *
     * @return a {@link java.util.List} object.
     */
    public List<User> getUsers() {
        return users == null? new ArrayList<>(0): users;
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
     * Get the next cursor
     *
     * @return Next cursor
     */
    public String getNextCursor() {
        return nextCursor;
    }

    /**
     * Set the next cursor
     *
     * @param nextCursor Next cursor
     */
    public void setNextCursor(String nextCursor) {
        this.nextCursor = nextCursor;
    }

    /**
     * Check if the response has errors
     *
     * @return Whether or not the request had any erorrs
     */
    public boolean hasErrors() {
        return !getErrors().isEmpty();
    }

    /**
     * Get the error
     *
     * @return Errors
     */
    public List<ApiError> getErrors() {
        return errors == null? new ArrayList<>(0) : errors;
    }

    /**
     * Set the errors
     *
     * @param errors Errors
     */
    public void setErrors(List<ApiError> errors) {
        this.errors = errors;
    }

}
