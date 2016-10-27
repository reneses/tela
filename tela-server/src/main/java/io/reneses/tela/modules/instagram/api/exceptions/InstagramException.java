package io.reneses.tela.modules.instagram.api.exceptions;


import io.reneses.tela.core.api.exceptions.ApiException;

/**
 * Generic Instagram Exception
 */
public class InstagramException extends ApiException {

    /**
     * Constructor for InstagramException.
     */
    public InstagramException() {
        this("Unknown Instagram exception", 400);
    }

    /**
     * Constructor for InstagramException.
     *
     * @param message a {@link java.lang.String} object.
     * @param code a int.
     */
    public InstagramException(String message, int code) {
        super(message, code);
    }
}
