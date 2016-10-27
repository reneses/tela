package io.reneses.tela.core.history;

import io.reneses.tela.core.cache.CacheManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * HistoryFactory class.
 */
public class HistoryFactory {

    private HistoryFactory() {}

    /**
     * Create a history instance of the configured implementation
     *
     * @return History
     */
    public static History create() {
        return new HistoryImpl(CacheManagerFactory.create());
    }

}
