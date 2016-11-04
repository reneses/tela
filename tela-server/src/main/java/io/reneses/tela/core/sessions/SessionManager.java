package io.reneses.tela.core.sessions;

import io.reneses.tela.core.sessions.exceptions.ModuleTokenNotFoundException;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import io.reneses.tela.core.sessions.models.Session;

/**
 * Session Manager, component responsible of handling sessions
 */
public interface SessionManager {

    /**
     * Create new session
     *
     * @return Session
     */
    Session create();

    /**
     * Create new unique session adding a token
     * This is shortcut for createOrUpdate() and then addModuleToken()
     *
     * @return Session
     * @param module Module name
     * @param moduleToken Token to add
     */
    Session createWithModuleToken(String module, String moduleToken);

    /**
     * Find a session given its access token
     *
     * @param accessToken Access token
     * @return Session with the provied access token
     * @throws SessionNotFoundException if there is no session for the given access token.
     */
    Session findByAccessToken(String accessToken) throws SessionNotFoundException;

    /**
     * Check if the provided session exists
     *
     * @param accessToken AccessToken
     * @return True if the session exists, false otherwise
     */
    boolean existsByAccessToken(String accessToken) ;

    /**
     * Delete a session and all the related tokens
     *
     * @param session Session ID
     * @return True if deleted, false otherwise
     */
    boolean delete(Session session);

    /**
     * Retrieve the module token of a session
     *
     * @param session Session
     * @param module  Module to retrieve the token of
     * @return Module token
     * @throws ModuleTokenNotFoundException if there is no such module token.
     * @throws SessionNotFoundException if the session does not exist.
     */
    String getModuleToken(Session session, String module) throws ModuleTokenNotFoundException, SessionNotFoundException;

    /**
     * Delete module token from a session
     *
     * @param session Session
     * @param module  Module
     * @return True if the toke existed and was deleted, false otherwise
     */
    boolean deleteModuleToken(Session session, String module);

    /**
     * Get a session given the module token
     *
     * @param module Module
     * @param token  Module token
     * @return Session if found, null otherwise
     */
    Session findByModuleToken(String module, String token);

    /**
     * Delete a session given the module token
     *
     * @param module Module
     * @param token  Module token
     * @return True if the session existed, false otherwise
     */
    boolean deleteByModuleToken(String module, String token);

    /**
     * Add a module token to a session
     * If a session has already been created for a certain token, the token will be deleted from it.
     *
     * @param session Session
     * @param module  Module
     * @param token   Module token
     * @throws SessionNotFoundException if the session does not exist.
     */
    void addModuleToken(Session session, String module, String token) throws SessionNotFoundException;

}
