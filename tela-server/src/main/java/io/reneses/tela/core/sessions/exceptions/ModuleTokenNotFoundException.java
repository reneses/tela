package io.reneses.tela.core.sessions.exceptions;


import io.reneses.tela.core.api.exceptions.AuthorizationApiException;

/**
 * Exception thrown when a module token is not found
 */
public class ModuleTokenNotFoundException extends AuthorizationApiException {

    /**
     * Constructor for ModuleTokenNotFoundException.
     *
     * @param session Session
     * @param module Module
     */
    public ModuleTokenNotFoundException(String session, String module) {
        super("There is no token defined for the module '" + module + "' within the session '" + session + "'");
    }

    /**
     * Constructor for ModuleTokenNotFoundException.
     *
     * @param message Error message
     */
    public ModuleTokenNotFoundException(String message) {
        super(message);
    }
}
