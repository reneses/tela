package io.reneses.tela.core.history;

import io.reneses.tela.core.cache.CacheManager;
import io.reneses.tela.core.history.models.HistoryEntry;

import java.util.stream.Stream;

public class HistoryImpl implements History {

    private static final String MODULE = "-history";
    private CacheManager cacheManager;

    public HistoryImpl(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    private String getKey(HistoryEntry entry) {
        String[] stringParams = Stream.of(entry.getParameters())
                .map(Object::toString)
                .toArray(String[]::new);
        return entry.getAction() + ":" + String.join(":", (CharSequence[]) stringParams);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(HistoryEntry entry) {
        if (entry == null)
            throw new IllegalArgumentException("The history entry to be added cannot be null");
        cacheManager.put(MODULE, getKey(entry), "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isPresent(HistoryEntry entry) {
        return entry != null && cacheManager.get(MODULE, getKey(entry)) != null;
    }

}
