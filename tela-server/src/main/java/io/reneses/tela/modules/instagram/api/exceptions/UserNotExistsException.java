package io.reneses.tela.modules.instagram.api.exceptions;


/**
 * The requested user does not exist
 */
public class UserNotExistsException extends InstagramException {

    private String user;

    /**
     * Constructor for UserNotExistsException.
     *
     * @param user a {@link java.lang.String} object.
     */
    public UserNotExistsException(String user) {
        super("The requested user '" + user + "' does not exist", 404);
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
