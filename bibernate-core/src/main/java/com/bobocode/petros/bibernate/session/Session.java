package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.ddl.DdlExecutionResult;
import com.bobocode.petros.bibernate.transaction.Transaction;

import java.util.Collection;
import java.util.Optional;

/**
 * Primary interface that is exposed to user and provides a set of common methods to interact with database.
 */
public interface Session {

    /**
     * Persists an entity in database.
     *
     * @param entity persistable entity
     * @return persisted entity (entity with primary key)
     * @param <T> generic type
     */
    <T> T persist(T entity);

    /**
     * Searches for a record using entity class and primary key identifier.
     *
     * @param type entity class
     * @param id id (primary key)
     * @return entity wrapper in {@link Optional}
     * @param <T> generic type
     */
    <T> Optional<T> findById(Class<T> type, Object id);

    /**
     * Searches for a collection of records by a given property.
     *
     * @param type entity class
     * @param propertyName property name
     * @param value property value
     * @return collection of entities
     * @param <T> generic type
     */
    <T> Collection<T> find(Class<T> type, String propertyName, Object value);

    /**
     * Performs update operation on a given entity.
     *
     * @param entity entity to update
     * @return updated entity
     * @param <T> generic type
     */
    <T> T update(T entity);

    /**
     * Performs delete operation by id on a given entity.
     *
     * @param type entity class
     * @param id id (primary key)
     * @param <T> generic type
     */
    <T> void deleteById(Class<T> type, Object id);

    /**
     * Performs delete operation on a given entity.
     *
     * @param entity entity to delete
     * @param <T> generic type
     */
    <T> void delete(T entity);

    /**
     * Executes a given query.
     * Not implemented yet.
     *
     * @param sql query to execute
     * @return instance of {@link Query}
     * @param <T> generic type
     */
    <T> Query<T> createQuery(String sql);

    /**
     * Executes a given query.
     * Not implemented yet.
     *
     * @param sql query to execute
     * @param type result entity class
     * @return instance of {@link Query}
     * @param <T> generic type
     */
    <T> Query<T> createQuery(String sql, Class<T> type);

    /**
     * Returns a transaction object, so after obtaining it user can handle all calls to db manually as auto-commit mode
     * in this case is disabled. So it is mandatory for the transaction to be committed or rolled back in the end.
     *
     * @return instance of {@link Transaction}
     */
    Transaction getTransaction();

    /**
     * Runs dirty checking mechanism and apply updates for the changed entities.
     */
    void flush();

    /**
     * Closes a session. Runs {@link Session#flush()} under the hood before closing the session.
     */
    void close();

    /**
     * Executes DDL statement and return simple stats.
     *
     * @param sql sql to execute
     * @return stats
     */
    DdlExecutionResult execute(final String sql);

}
