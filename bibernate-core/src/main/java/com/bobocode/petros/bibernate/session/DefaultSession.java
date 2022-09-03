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
    public <T, ID> T findById(Class<T> type, ID id) {
        return jdbcQueryManager.find(type, List.of(Restrictions.idEq(EntityUtils.getIdField(type).getName(), id)));
    }

    @Override
    public <T> T find(Class<T> type, String propertyName, Object value) {
        return jdbcQueryManager.find(type, List.of(Restrictions.eq(propertyName, value)));
    }

    @Override
    public <T> T update(T entity) {
        return jdbcQueryManager.update(entity);
    }

    @Override
    public <ID> void deleteById(ID id) {
        jdbcQueryManager.deleteById(id);
    }

    @Override
    public <T> void delete(T entity) {
        jdbcQueryManager.delete(entity.getClass(), entity);
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
