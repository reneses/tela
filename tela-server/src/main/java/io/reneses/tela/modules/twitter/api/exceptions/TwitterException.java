package io.reneses.tela.modules.twitter.api.exceptions;


import io.reneses.tela.core.api.exceptions.ApiException;

/**
 * Exception thrown by the Twitter API
 */
public class TwitterException extends ApiException {

    /**
     * Constructor for TwitterException.
     *
     * @param message Error message
     * @param code Error code
     */
    public TwitterException(String message, int code) {
        super(message, code);
    }

}
