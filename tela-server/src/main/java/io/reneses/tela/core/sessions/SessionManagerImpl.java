package io.reneses.tela.core.sessions;

import io.reneses.tela.core.sessions.exceptions.ModuleTokenNotFoundException;
import io.reneses.tela.core.sessions.exceptions.SessionNotFoundException;
import io.reneses.tela.core.sessions.models.Session;
import io.reneses.tela.core.sessions.repositories.OrientSessionManagerRepository;
import io.reneses.tela.core.sessions.repositories.AbstractSessionManagerRepository;

/**
 * Implementation of a Session Manager
 */
class SessionManagerImpl implements SessionManager {

    private AbstractSessionManagerRepository repository;

    SessionManagerImpl() {
        super();
        repository = new OrientSessionManagerRepository();
    }

    /** {@inheritDoc} */
    @Override
    public Session create() {
        Session session = new Session();
        repository.create(session);
        return session;
    }

    /** {@inheritDoc} */
    @Override
    public Session findByAccessToken(String accessToken) throws SessionNotFoundException {
        Session session = repository.findByAccessToken(accessToken);
        if (session == null)
            throw new SessionNotFoundException(accessToken);
        return session;
    }

    /** {@inheritDoc} */
    @Override
    public Session createWithModuleToken(String module, String moduleToken) {
        if (module == null)
            throw new IllegalArgumentException("The module cannot be null");
        if (moduleToken == null)
            throw new IllegalArgumentException("The module token cannot be null");
        Session session = create();
        try {
            addToken(session, module, moduleToken);
        } catch (SessionNotFoundException ignored) {
        }
        return session;
    }

    /** {@inheritDoc} */
    @Override
    public boolean existsByAccessToken(String accessToken) {
        return repository.existsByAccessToken(accessToken);
    }

    /** {@inheritDoc} */
    @Override
    public boolean delete(Session session) {
        return repository.delete(session);
    }

    /** {@inheritDoc} */
    @Override
    public String getModuleToken(Session session, String module) throws ModuleTokenNotFoundException, SessionNotFoundException {
        String moduleToken = findByAccessToken(session.getAccessToken()).getToken(module);
        if (moduleToken == null)
            throw new ModuleTokenNotFoundException(session.getId(), module);
        return moduleToken;
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteModuleToken(Session session, String module) {
        repository.deleteModuleToken(session, module);
        return session.deleteToken(module);
    }

    /** {@inheritDoc} */
    @Override
    public Session findByModuleToken(String module, String token) {
        return repository.findByModuleToken(module, token);
    }

    /** {@inheritDoc} */
    @Override
    public boolean deleteByModuleToken(String module, String token) {
        Session session = findByModuleToken(module, token);
        return session != null && delete(session);
    }

    /** {@inheritDoc} */
    @Override
    public void addToken(Session session, String module, String token) throws SessionNotFoundException {
        Session existingSession = findByModuleToken(module, token);
        if (existingSession != null)
            deleteModuleToken(existingSession, module);
        if (session.getTokens().containsKey(module))
            deleteModuleToken(session, module);
        if (!repository.addModuleToken(session, module, token))
            throw new SessionNotFoundException(session.getId());
        session.addToken(module, token);
    }

}
