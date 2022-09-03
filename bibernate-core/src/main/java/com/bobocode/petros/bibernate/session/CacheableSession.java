package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.transaction.Transaction;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class CacheableSession extends DefaultSession {

    private Map<EntityKey<?>, Object> entityCache = new HashMap<>();
    private PersistenceContext persistenceContext;
    
    public CacheableSession(DataSource dataSource, JdbcQueryManager jdbcQueryManager) {
        super(dataSource, jdbcQueryManager);
        this.persistenceContext = new PersistenceContext();
    }

    @Override
    public <T> T persist(T entity) {
        return super.persist(entity);
    }

    @Override
    public <T, ID> T findById(Class<T> type, ID id) {
        return super.findById(type, id);
    }

    @Override
    public <T> T find(Class<T> type, String propertyName, Object value) {
        return null;
    }

    @Override
    public <T> T update(T entity) {
        return super.update(entity);
    }

    @Override
    public <ID> void deleteById(ID id) {
        super.deleteById(id);
    }

    @Override
    public <T> void delete(T entity) {
        super.delete(entity);
    }

    @Override
    public <T> Query<T> createQuery(String sql) {
        return super.createQuery(sql);
    }

    @Override
    public <T> Query<T> createQuery(String sql, Class<T> type) {
        return super.createQuery(sql, type);
    }

    @Override
    public Transaction getTransaction() {
        return super.getTransaction();
    }

    @Override
    public void close() {
        super.close();
    }
}
