package io.reneses.tela.core.sessions.models;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

/**
 * Session model
 */
public class Session {

    private static SecureRandom random = new SecureRandom();

    private String id, accessToken;
    private Map<String, String> tokens;

    private static String generateToken() {
        return new BigInteger(130, random).toString(32);
    }

    /**
     * Constructor for Session.
     */
    public Session() {
        this(generateToken(), generateToken());
    }

    /**
     * Constructor for Session.
     *
     * @param id a {@link java.lang.String} object.
     * @param accessToken a {@link java.lang.String} object.
     */
    public Session(String id, String accessToken) {
        this(id, accessToken, new HashMap<>());
    }

    /**
     * Constructor for Session.
     *
     * @param id a {@link java.lang.String} object.
     * @param accessToken a {@link java.lang.String} object.
     * @param tokens a {@link java.util.Map} object.
     */
    public Session(String id, String accessToken, Map<String, String> tokens) {
        this.id = id;
        this.accessToken = accessToken;
        this.tokens = tokens;
    }

    /**
     * Getter for the field <code>id</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getId() {
        return id;
    }

    /**
     * Getter for the field <code>accessToken</code>.
     *
     * @return a {@link java.lang.String} object.
     */
    public String getAccessToken() {
        return accessToken;
    }

    /**
     * addModuleToken.
     *
     * @param module a {@link java.lang.String} object.
     * @param token a {@link java.lang.String} object.
     */
    public void addToken(String module, String token) {
        tokens.put(module, token);
    }

    /**
     * deleteToken.
     *
     * @param module a {@link java.lang.String} object.
     * @return a boolean.
     */
    public boolean deleteToken(String module) {
        return tokens.remove(module) != null;
    }

    /**
     * getToken.
     *
     * @param module a {@link java.lang.String} object.
     * @return a {@link java.lang.String} object.
     */
    public String getToken(String module) {
        return tokens.get(module);
    }

    /**
     * Getter for the field <code>tokens</code>.
     *
     * @return a {@link java.util.Map} object.
     */
    public Map<String, String> getTokens() {
        return tokens;
    }

    /** {@inheritDoc} */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session session = (Session) o;

        if (!id.equals(session.id)) return false;
        if (!accessToken.equals(session.accessToken)) return false;
        return tokens.equals(session.tokens);

    }

    /** {@inheritDoc} */
    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + accessToken.hashCode();
        result = 31 * result + tokens.hashCode();
        return result;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return id + ':' + accessToken;
    }

}
