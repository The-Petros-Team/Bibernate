package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import com.bobocode.petros.bibernate.transaction.Transaction;

import java.util.Collection;
import java.util.List;

/**
 * Provides basic CRUD operations over an entity.
 */
public interface JdbcQueryManager {

    /**
     * Persists an entity to database.
     *
     * @param entity entity that is supposed to be persisted
     * @param <T>    generic type
     * @return persisted entity with id
     */
    <T> T persist(T entity);

    /**
     * Allows to search for the specific records in database based on provided restrictions.
     * See also: {@link Restriction}
     * See also: {@link Restrictions}
     *
     * @param type         class of an entity to search for
     * @param restrictions applicable restrictions
     * @param <T>          generic type
     * @return collection of entities
     */
    <T> Collection<T> find(Class<T> type, List<Restriction> restrictions);

    /**
     * Updates a given entity.
     *
     * @param entity entity that is supposed to be updated
     * @param <T>    generic type
     * @return updated entity
     */
    <T> T update(T entity);

    /**
     * Performs delete operation on a given entity.
     *
     * @param entity entity that is supposed to be deleted
     * @param <T>    generic type
     */
    <T> void delete(T entity);

    /**
     * Performs delete operation based on a given id and entity class.
     *
     * @param type entity type
     * @param id   id (primary key)
     * @param <T>  generic type
     */
    <T> void deleteById(Class<T> type, Object id);

    /**
     * Allows to manually handle operations over the database records. Has autoCommit value set to false.
     *
     * @return instance of {@link Transaction}
     */
    Transaction getTransaction();

}
