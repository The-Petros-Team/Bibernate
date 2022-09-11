package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.action.ActionQueue;
import com.bobocode.petros.bibernate.session.action.DeleteEntityAction;
import com.bobocode.petros.bibernate.session.action.InsertEntityAction;
import com.bobocode.petros.bibernate.session.action.UpdateEntityAction;
import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.util.Collection;
import java.util.Optional;

public class CacheableSession extends DefaultSession {
    private PersistenceContext persistenceContext;
    private ActionQueue actionQueue;

    public CacheableSession(JdbcQueryManager jdbcQueryManager) {
        super(jdbcQueryManager);
        this.persistenceContext = new PersistenceContext();
        this.actionQueue = new ActionQueue(persistenceContext);
    }

    @Override
    public <T> T persist(T entity) {
        T persistedEntity = super.persist(entity);
        persistenceContext.addToCache(persistedEntity);
        persistenceContext.addSnapshot(persistedEntity);
//        actionQueue.add(new InsertEntityAction(entity, jdbcQueryManager));
        return persistedEntity;
    }

    @Override
    public <T> Optional<T> findById(Class<T> type, Object id) {
        return persistenceContext.getEntityFromCacheById(type, id).or(() -> super.findById(type, id));
    }

    @Override
    public <T> Collection<T> find(Class<T> type, String propertyName, Object value) {
        Collection<T> cachedEntities = persistenceContext.getEntitiesCollectionFromCacheByProperty(type, propertyName, value);
        return cachedEntities.isEmpty() ?
                super.find(type, propertyName, value) :
                cachedEntities;
    }

    @Override
    public <T> T update(T entity) {
        actionQueue.add(new UpdateEntityAction(entity, jdbcQueryManager));
        persistenceContext.addToCache(entity);
        persistenceContext.addSnapshot(entity);
        return entity;
    }

    @Override
    public <T> void deleteById(Class<T> type, Object id) {
        actionQueue.add(new DeleteEntityAction(type, id, jdbcQueryManager));
        persistenceContext.removeEntityFromCacheByEntityKey(EntityUtils.createEntityKey(type, id));
        persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(type, id));
    }

    @Override
    public <T> void delete(T entity) {
        actionQueue.add(new DeleteEntityAction(entity, jdbcQueryManager));
        persistenceContext.removeEntityFromCacheByEntityKey(EntityUtils.createEntityKey(entity));
        persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(entity));
    }
    @Override
    public void flush() {
        persistenceContext.getChangedEntities()
                .forEach(e -> actionQueue.add(new UpdateEntityAction(e, jdbcQueryManager)));
        actionQueue.processActions();
    }

    @Override
    public void close() {
        flush();
        super.close();
        persistenceContext.clear();
    }
}
