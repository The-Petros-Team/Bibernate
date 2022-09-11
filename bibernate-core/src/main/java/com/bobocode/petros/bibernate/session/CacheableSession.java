package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
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
        return persistenceContext.getEntityFromCacheById(type, id).or(() -> {
            Optional<T> entity = super.findById(type, id);
            entity.ifPresent(persistenceContext::addToCache);
            entity.ifPresent(persistenceContext::addSnapshot);
            return entity;
        });
    }

    @Override
    public <T> Collection<T> find(Class<T> type, String propertyName, Object value) {
        Collection<T> cachedEntities = persistenceContext.getEntitiesCollectionFromCacheByProperty(type, propertyName, value);
        if (cachedEntities.isEmpty()) {
            Collection<T> entityCollection = super.find(type, propertyName, value);
            entityCollection.forEach(entity -> {
                persistenceContext.addSnapshot(entity);
                persistenceContext.addToCache(entity);
            });
            return entityCollection;
        }
        return cachedEntities;
    }

    @Override
    public <T> T update(T entity) {
        T updatedEntity = super.update(entity);
        persistenceContext.addToCache(updatedEntity);
        persistenceContext.addSnapshot(updatedEntity);
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
        persistenceContext.removeEntityFromCacheByEntityKey(EntityUtils.createEntityKey(entity));
        persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(entity));
    }

    @Override
    public void close() {
        super.close();
        persistenceContext.clear();
    }
}
