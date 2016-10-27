package io.reneses.tela.core.sessions.repositories;

import io.reneses.tela.core.sessions.models.Session;

/**
 * Repository for Session objects
 */
public interface AbstractSessionManagerRepository {

    /**
     * Create a session
     *
     * @param session Session
     */
    void create(Session session);

    /**
     * Delete a session
     *
     * @param session Session
     * @return Whether the object was or not deleted
     */
    boolean delete(Session session);

    /**
     * Find a session given its access token
     *
     * @param accessToken Access token of the session
     * @return Session with the given access token, or null if not found
     */
    Session findByAccessToken(String accessToken);

    /**
     * Check if a session with the given access token exists or not
     *
     * @param accessToken Access token of the session
     * @return Whether the session exists or no
     */
    boolean existsByAccessToken(String accessToken);

    /**
     * Add a module token to an existing session
     *
     * @param session Session
     * @param module  Module
     * @param token   Token
     * @return Whether the token was added or no (if the session exists or not)
     */
    boolean addModuleToken(Session session, String module, String token);

    /**
     * Find a session given one of its module tokens
     *
     * @param module Module
     * @param token  Token
     * @return Session with the module token, or null if not found
     */
    Session findByModuleToken(String module, String token);

    /**
     * Delete a module token from one module
     *
     * @param session Session
     * @param token   Token to be deleted
     * @return Whether it was deleted or not (if it existed)
     */
    boolean deleteModuleToken(Session session, String token);

}
