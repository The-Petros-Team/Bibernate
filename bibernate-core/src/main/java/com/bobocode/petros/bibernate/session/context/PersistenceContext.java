package com.bobocode.petros.bibernate.session.context;

import com.bobocode.petros.bibernate.session.EntityKey;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersistenceContext {

    private Map<EntityKey<?>, Object> entitySnapshots;
    private Map<EntityKey<?>, Object> cache;

    public PersistenceContext() {
        this.entitySnapshots = new HashMap<>();
        this.cache = new HashMap<>();
    }

    public void addSnapshot(final Object entity) {

    }

    public void addToCache(final Object entity) {

    }

    public List<Object> getChangedEntities() {
        // do comparison
        return null;
    }

    public void clear() {
        this.entitySnapshots.clear();
        this.cache.clear();
    }
}
