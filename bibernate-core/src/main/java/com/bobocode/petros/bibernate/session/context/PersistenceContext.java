package com.bobocode.petros.bibernate.session.context;

import com.bobocode.petros.bibernate.exceptions.ReflectionOperationException;
import com.bobocode.petros.bibernate.session.EntityKey;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.bobocode.petros.bibernate.exceptions.ExceptionMessages.NULL_ENTITY_PERSISTENCE_CONTEXT_MSG;
import static java.util.Objects.requireNonNull;

/**
 * Class that using to store entity into cache
 * or do entity snapshot. Also, it provides method
 * than return entities that was changed.
 */

@Slf4j
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
        requireNonNull(entity, String.format(NULL_ENTITY_PERSISTENCE_CONTEXT_MSG, "Snapshot entity "));
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
        requireNonNull(entity, String.format(NULL_ENTITY_PERSISTENCE_CONTEXT_MSG, "Cacheable entity "));
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

    public Object removeEntityFromCacheByEntityKey(EntityKey<?> entityKey) {
        return cache.remove(entityKey);
    }

    public Object removeEntityFromSnapshotByEntityKey(EntityKey<?> entityKey) {
        return entitySnapshots.remove(entityKey);
    }

    public <T> Optional<T> getEntityFromCacheById(Class<T> entityType, Object id) {
        return Optional.ofNullable(entityType.cast(cache.get(new EntityKey<>(entityType, id))));
    }

    public <T> Collection<T> getEntitiesCollectionFromCacheByProperty(Class<T> entityType, String propertyName, Object value) {
        return cache.values()
                .stream()
                .filter(entity -> entity.getClass().isAssignableFrom(entityType))
                .filter(entity -> compareEntityByProperty(entity, propertyName, value))
                .map(entityType::cast)
                .collect(Collectors.toSet());
    }

    private boolean compareEntityByProperty(Object entity, String propertyName, Object propertyValue) {
        try {
            Field field = entity.getClass().getDeclaredField(propertyName);
            field.setAccessible(true);
            var value = field.get(entity);
            return value.equals(propertyValue);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            log.error(String.format("Cannot get field %s from entity %s", propertyName, entity));
            return false;
        }
    }

    private boolean isNotUpdated(Map.Entry<EntityKey<?>, Object> entry) {
        return Arrays.stream(entry.getValue().getClass().getDeclaredFields())
                .anyMatch(Predicate.not(field -> {
                    try {
                        field.setAccessible(true);
                        EntityKey<?> key = entry.getKey();
                        var entitySnapshot = entitySnapshots.get(key);
                        var cachedEntity = cache.get(key);

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
