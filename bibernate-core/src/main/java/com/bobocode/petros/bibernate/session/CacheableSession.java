package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.util.Collection;
import java.util.Optional;

public class CacheableSession extends DefaultSession {
    private PersistenceContext persistenceContext;

    public CacheableSession(JdbcQueryManager jdbcQueryManager) {
        super(jdbcQueryManager);
        this.persistenceContext = new PersistenceContext();
    }

    @Override
    public <T> T persist(T entity) {
        T persistedEntity = super.persist(entity);
        persistenceContext.addToCache(persistedEntity);
        persistenceContext.addSnapshot(persistedEntity);
        return persistedEntity;
    }

    @Override
    public <T> Optional<T> findById(Class<T> type, Object id) {
        return persistenceContext.getEntityFromCacheById(type, id).or(() -> super.findById(type, id));
    }

    @Override
    public <T> Collection<T> find(Class<T> type, String propertyName, Object value) {
        return persistenceContext.getEntitiesCollectionFromCacheByProperty(type, propertyName, value)
                .orElseGet(() -> super.find(type, propertyName, value));
    }

    @Override
    public <T> T update(T entity) {
        T updatedEntity = super.update(entity);
        persistenceContext.addToCache(updatedEntity);
        return updatedEntity;
    }

    @Override
    public <T> void deleteById(Class<T> type, Object id) {
        super.deleteById(type, id);
        persistenceContext.removeEntityFromCacheByEntityKey(EntityUtils.createEntityKey(type, id));
        persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(type, id));
    }

    @Override
    public <T> void delete(T entity) {
        super.delete(entity);
        persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(entity));
        persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(entity));
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
