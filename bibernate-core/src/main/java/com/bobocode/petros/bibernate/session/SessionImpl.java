package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.transaction.Transaction;

import javax.sql.DataSource;

public class SessionImpl implements Session {

    private final DataSource dataSource;

    public SessionImpl(final DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public <T> T persist(T entity) {
        return null;
    }

    @Override
    public <T, ID> T findById(Class<T> type, ID id) {
        return null;
    }

    @Override
    public <T> T update(T entity) {
        return null;
    }

    @Override
    public <ID> void deleteById(ID id) {

    }

    @Override
    public <T> void delete(T entity) {

    }

    @Override
    public <T> Query<T> createQuery(String sql) {
        return null;
    }

    @Override
    public <T> Query<T> createQuery(String sql, Class<T> type) {
        return null;
    }

    @Override
    public Transaction getTransaction() {
        return null;
    }
}
