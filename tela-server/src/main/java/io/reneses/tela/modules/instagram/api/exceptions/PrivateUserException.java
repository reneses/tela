package io.reneses.tela.modules.instagram.api.exceptions;

/**
 * The requested user is private
 */
public class PrivateUserException extends InstagramException {

    private String user;

    /**
     * Constructor for PrivateUserException.
     *
     * @param user a {@link java.lang.String} object.
     */
    public PrivateUserException(String user) {
        super("The requested user '" + user + "' is private", 403);
        this.user = user;
    }

    /**
     * Getter for the field <code>user</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getUser() {
        return user;
    }
}
