package com.bobocode.petros.bibernate.session.context;

import com.bobocode.petros.bibernate.exceptions.ReflectionOperationException;
import com.bobocode.petros.bibernate.session.EntityKey;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.NULL_ENTITY_PERSISTENCE_CONTEXT_CACHE_EXCEPTION;
import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.NULL_ENTITY_PERSISTENCE_CONTEXT_SNAPSHOT_EXCEPTION;
import static java.util.Objects.requireNonNull;

/**
 * Class that using to store entity into cache
 * or do entity snapshot. Also, it provides method
 * than return entities that was changed.
 */
public class PersistenceContext {

    private Map<EntityKey<?>, Object> entitySnapshots;
    private Map<EntityKey<?>, Object> cache;

    public PersistenceContext() {
        this.entitySnapshots = new HashMap<>();
        this.cache = new HashMap<>();
    }

    /**
     * Save copy of entity object to
     * snapshotMap.
     *
     * @param entity object
     * @throws NullPointerException if entity param is null
     */
    public void addSnapshot(final Object entity) {
        requireNonNull(entity, NULL_ENTITY_PERSISTENCE_CONTEXT_SNAPSHOT_EXCEPTION);
        var key = EntityUtils.createEntityKey(entity);
        entitySnapshots.put(key, replicateObject(entity));
    }

    /**
     * Save entity object into cache.
     *
     * @param entity object
     * @throws NullPointerException if entity param is null
     */
    public void addToCache(final Object entity) {
        requireNonNull(entity, NULL_ENTITY_PERSISTENCE_CONTEXT_CACHE_EXCEPTION);
        var key = EntityUtils.createEntityKey(entity);
        cache.put(key, entity);
    }

    /**
     * Use to get list of entities that
     * was stored into snapshot and cache
     * but after save will be changed. It compares
     * fields of cached entity with fields
     * of entity in snapshot map.
     *
     * @return list of changed entities
     */
    public List<Object> getChangedEntities() {
        return cache.entrySet().stream()
                .filter(this::isNotUpdated)
                .map(Map.Entry::getValue)
                .toList();
    }

    private boolean isNotUpdated(Map.Entry<EntityKey<?>, Object> entry) {
        return Arrays.stream(entry.getValue().getClass().getDeclaredFields())
                .anyMatch(Predicate.not(field -> {
                    try {
                        field.setAccessible(true);
                        EntityKey<?> key = entry.getKey();
                        var entitySnapshot = entitySnapshots.get(key);
                        var cachedEntity  = cache.get(key);

                        return field.get(entitySnapshot).equals(field.get(cachedEntity));
                    } catch (IllegalAccessException e) {
                        throw new ReflectionOperationException(e.getMessage(), e);
                    }
                }));
    }

    private Object replicateObject(Object entity) {
        try {
            var replica = entity.getClass().getDeclaredConstructor().newInstance();
            Arrays.stream(entity.getClass().getDeclaredFields())
                    .forEach(field -> {
                        try {
                            field.setAccessible(true);
                            field.set(replica, field.get(entity));
                        } catch (IllegalAccessException e) {
                            throw new ReflectionOperationException(e.getMessage(), e);
                        }
                    });
            return replica;
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new ReflectionOperationException(e.getMessage(), e);
        }
    }

    public void clear() {
        this.entitySnapshots.clear();
        this.cache.clear();
    }
}
