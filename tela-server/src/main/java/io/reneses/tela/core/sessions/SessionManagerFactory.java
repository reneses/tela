package io.reneses.tela.core.sessions;

/**
 * Factory class responsible of creating the appropriate instance of Abstract Session Manager
 */
public class SessionManagerFactory {

    private SessionManagerFactory(){}

    /**
     * Create and return an SessionManager
     *
     * @return Instance of an implementation of SessionManager
     */
    public static SessionManager create() {
        return new SessionManagerImpl();
    }
}
