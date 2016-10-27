package io.reneses.tela.modules.instagram.api.exceptions;

/**
 * The provided access token is not valid
 */
public class InvalidAccessTokenException extends InstagramException {

    /**
     * Constructor for InvalidAccessTokenException.
     */
    public InvalidAccessTokenException() {
        super("The accessToken is not valid", 400);
    }

    /**
     * Constructor for InvalidAccessTokenException.
     *
     * @param info a {@link java.lang.String} object.
     */
    public InvalidAccessTokenException(String info) {
        super("The accessToken is not valid: " + info, 400);
    }

}
