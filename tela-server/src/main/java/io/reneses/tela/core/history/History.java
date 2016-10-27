package io.reneses.tela.core.history;


import io.reneses.tela.core.history.models.HistoryEntry;

/**
 * The History is the component in charge of storing the latest actions performed by the framework.
 * This information is mainly used to decide whether retrieve an action from the cacheManager, although it
 * could also be used for debugging / statistical / monitoring purposes.
 */
public interface History {

    /**
     * Add an entry to the history
     *
     * @param entry History entry
     */
    void add(HistoryEntry entry);

    /**
     * Check if an entry is present in the history
     *
     * @param entry History entry
     * @return True if present, false otherwise
     */
    boolean isPresent(HistoryEntry entry);

}
