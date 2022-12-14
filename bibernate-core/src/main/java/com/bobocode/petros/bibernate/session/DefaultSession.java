package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

abstract class DefaultSession implements Session {

    protected final JdbcQueryManager jdbcQueryManager;

    protected DefaultSession(final JdbcQueryManager jdbcQueryManager) {
        this.jdbcQueryManager = jdbcQueryManager;
    }

    @Override
    public <T> T persist(T entity) {
        return jdbcQueryManager.persist(entity);
    }

    @Override
    public <T> Optional<T> findById(Class<T> type, Object id) {
        return jdbcQueryManager.find(type, List.of(Restrictions.idEq(EntityUtils.getIdField(type).getName(), id)))
                .stream()
                .findAny();
    }

    @Override
    public <T> Collection<T> find(Class<T> type, String propertyName, Object value) {
        return jdbcQueryManager.find(type, List.of(Restrictions.eq(propertyName, value)));
    }

    @Override
    public <T> T update(T entity) {
        return jdbcQueryManager.update(entity);
    }

    @Override
    public <T> void deleteById(Class<T> type, Object id) {
        jdbcQueryManager.deleteById(type, id);
    }

    @Override
    public <T> void delete(T entity) {
        jdbcQueryManager.delete(entity);
    }

    @Override
    public Transaction getTransaction() {
        return jdbcQueryManager.getTransaction();
    }

    @Override
    public void close() {
        getTransaction().commit();
    }
}
