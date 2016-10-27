package io.reneses.tela.modules.instagram.cache;

/**
 * Cache storing the user ID of an username
 */
public interface UsernameIdCache {

    /**
     * Cache the user ID of an username
     *
     * @param username Username
     * @param userId User ID
     */
    void put(String username, long userId);

    /**
     * Get the user ID of a given username
     *
     * @param username Username
     * @return User ID, or null if not found
     */
    Long getId(String username);

}
