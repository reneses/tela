package io.reneses.tela.core.sessions.exceptions;


import io.reneses.tela.core.api.exceptions.AuthorizationApiException;

/**
 * Exception thrown when a Session is not found
 */
public class SessionNotFoundException extends AuthorizationApiException {

    /**
     * Constructor for SessionNotFoundException.
     *
     * @param session Session not found
     */
    public SessionNotFoundException(String session) {
        super("The session '" + session + "' is not defined");
    }

}
