package io.reneses.tela.core.api.exceptions;

/**
 * API raised an exception due to an authorization issue
 */
public class AuthorizationApiException extends ApiException {

    /**
     * Constructor for AuthorizationApiException.
     *
     * @param message Error message
     */
    public AuthorizationApiException(String message) {
        super(message, 403);
    }

}
