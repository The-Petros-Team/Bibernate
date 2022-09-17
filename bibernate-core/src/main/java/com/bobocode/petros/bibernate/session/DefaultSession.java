package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.ddl.DdlExecutionResult;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Default implementation of {@link Session}.
 */
abstract class DefaultSession implements Session {

    protected final JdbcQueryManager jdbcQueryManager;

    protected DefaultSession(final JdbcQueryManager jdbcQueryManager) {
        this.jdbcQueryManager = jdbcQueryManager;
    }

    /**
     * {@inheritDoc}
     *
     * @param entity persistable entity
     * @param <T>    generic type
     * @return persisted entity (entity with primary key)
     */
    @Override
    public <T> T persist(T entity) {
        return jdbcQueryManager.persist(entity);
    }

    /**
     * {@inheritDoc}
     *
     * @param type entity class
     * @param id   id (primary key)
     * @param <T>  generic type
     * @return entity wrapper in {@link Optional}
     */
    @Override
    public <T> Optional<T> findById(Class<T> type, Object id) {
        return jdbcQueryManager.find(type, List.of(Restrictions.idEq(EntityUtils.getIdField(type).getName(), id)))
                .stream()
                .findAny();
    }

    /**
     * {@inheritDoc}
     *
     * @param type         entity class
     * @param propertyName property name
     * @param value        property value
     * @param <T>          generic type
     * @return collection of entities
     */
    @Override
    public <T> Collection<T> find(Class<T> type, String propertyName, Object value) {
        return jdbcQueryManager.find(type, List.of(Restrictions.eq(propertyName, value)));
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity to update
     * @param <T>    generic type
     * @return updated entity
     */
    @Override
    public <T> T update(T entity) {
        return jdbcQueryManager.update(entity);
    }

    /**
     * {@inheritDoc}
     *
     * @param type entity class
     * @param id   id (primary key)
     * @param <T>  generic type
     */
    @Override
    public <T> void deleteById(Class<T> type, Object id) {
        jdbcQueryManager.deleteById(type, id);
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity to delete
     * @param <T>    generic type
     */
    @Override
    public <T> void delete(T entity) {
        jdbcQueryManager.delete(entity);
    }

    /**
     * {@inheritDoc}
     *
     * @param sql query to execute
     * @param <T> generic type
     * @return instance of {@link Query}
     */
    @Override
    public <T> Query<T> createQuery(String sql) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @param sql  query to execute
     * @param type result entity class
     * @param <T>  generic type
     * @return instance of {@link Query}
     */
    @Override
    public <T> Query<T> createQuery(String sql, Class<T> type) {
        throw new UnsupportedOperationException();
    }

    /**
     * {@inheritDoc}
     *
     * @return instance of {@link Transaction}
     */
    @Override
    public Transaction getTransaction() {
        return jdbcQueryManager.getTransaction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        getTransaction().commit();
    }

    /**
     * Runs DDL execution logic.
     *
     * @param sql sql script
     * @return stats
     */
    @Override
    public DdlExecutionResult execute(final String sql) {
        return this.jdbcQueryManager.execute(sql);
    }
}
