package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.context.PersistenceContext;

/**
 * Entity key is used to determine a uniqueness of entities in {@link PersistenceContext}.
 * Used as a key in {@link PersistenceContext#entitySnapshots} and {@link PersistenceContext#cache}
 *
 * @param entityType entity class
 * @param id id (primary key)
 * @param <T> generic type
 */
public record EntityKey<T>(Class<T> entityType, Object id) {
}
