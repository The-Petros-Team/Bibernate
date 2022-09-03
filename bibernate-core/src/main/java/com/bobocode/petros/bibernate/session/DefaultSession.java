package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import javax.sql.DataSource;
import java.util.List;

public class DefaultSession implements Session {

    private final DataSource dataSource;
    private final JdbcQueryManager jdbcQueryManager;

    public DefaultSession(final DataSource dataSource, final JdbcQueryManager jdbcQueryManager) {
        this.dataSource = dataSource;
        this.jdbcQueryManager = jdbcQueryManager;
    }

    @Override
    public <T> T persist(T entity) {
        return jdbcQueryManager.persist(entity);
    }

    @Override
    public <T> T findById(Class<T> type, Object id) {
        return jdbcQueryManager.find(type, List.of(Restrictions.idEq(EntityUtils.getIdField(type).getName(), id)))
                .iterator()
                .next();
    }

    @Override
    public <T> T find(Class<T> type, String propertyName, Object value) {
        return jdbcQueryManager.find(type, List.of(Restrictions.eq(propertyName, value)))
                .iterator()
                .next();
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
    public <T> Query<T> createQuery(String sql) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <T> Query<T> createQuery(String sql, Class<T> type) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Transaction getTransaction() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void close() {
        throw new UnsupportedOperationException();
    }
}
