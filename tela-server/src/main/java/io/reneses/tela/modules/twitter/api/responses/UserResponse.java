package io.reneses.tela.modules.twitter.api.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import io.reneses.tela.modules.twitter.api.models.ApiError;
import io.reneses.tela.modules.twitter.models.User;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class UserResponse {

    @JsonUnwrapped
    private User user;

    private List<ApiError> errors;

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

    /**
     * Get the user
     *
     * @return User
     */
    public User getUser() {
        return user;
    }

    /**
     * Set the user
     *
     * @param user User
     */
    public void setUser(User user) {
        this.user = user;
    }

}