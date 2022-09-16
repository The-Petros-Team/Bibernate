package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.exceptions.SessionIsClosedException;
import com.bobocode.petros.bibernate.session.action.ActionQueue;
import com.bobocode.petros.bibernate.session.action.DeleteEntityAction;
import com.bobocode.petros.bibernate.session.action.UpdateEntityAction;
import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.transaction.TransactionImpl;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Implementation of {@link DefaultSession} that brings additional logic, like caching, action queueing, dirty
 * checking etc.
 */
@Slf4j
public class CacheableSession extends DefaultSession {

    private PersistenceContext persistenceContext;
    private ActionQueue actionQueue;
    private boolean isClosed;

    public CacheableSession(JdbcQueryManager jdbcQueryManager) {
        super(jdbcQueryManager);
        this.persistenceContext = new PersistenceContext();
        this.actionQueue = new ActionQueue();
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
        return executeWithIsClosedCheck(() -> {
            var entityId = EntityUtils.getIdValue(entity);
            if (entityId == null) {
                log.debug("Entity id field is null. Saving entity {} of class {} to DB.", entity, entity.getClass());
                return doPersist(entity);
            } else {
                var foundEntity = findById(entity.getClass(), entityId);
                if (foundEntity.isPresent()) {
                    log.debug("Entity already exist in DB. Updating cache...");
                    @SuppressWarnings("unchecked") T e = (T) foundEntity.get();
                    persistenceContext.addToCache(entity);
                    return e;
                } else {
                    log.debug("Entity doesn't exist in DB. Saving entity {} of class {} to DB.", entity, entity.getClass());
                    return doPersist(entity);
                }
            }
        });
    }

    private <T> T doPersist(final T entity) {
        T persistedEntity = super.persist(entity);
        persistenceContext.addToCache(persistedEntity);
        persistenceContext.addSnapshot(persistedEntity);
        return persistedEntity;
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
        return executeWithIsClosedCheck(() -> persistenceContext.getEntityFromCacheById(type, id).or(() -> {
            log.debug("Entity with id - {} of type - {} is not found in persistence context, executing query to DB", id, type);
            Optional<T> entity = super.findById(type, id);
            entity.ifPresent(persistenceContext::addToCache);
            entity.ifPresent(persistenceContext::addSnapshot);
            return entity;
        }));
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
        return executeWithIsClosedCheck(() -> {
            Collection<T> cachedEntities = persistenceContext.getEntitiesCollectionFromCacheByProperty(type, propertyName,
                    value);
            log.debug("Found {} entities in persistence context for type - {}, where {}={}", cachedEntities.size(), type, propertyName, value);
            if (cachedEntities.isEmpty()) {
                Collection<T> entityCollection = super.find(type, EntityUtils.resolveEntityColumnByPropertyName(type, propertyName), value);
                log.debug("Found {} entities in DB of type - {}, where {}={}", cachedEntities.size(), type, propertyName, value);
                entityCollection.forEach(entity -> {
                    persistenceContext.addSnapshot(entity);
                    persistenceContext.addToCache(entity);
                });
                return entityCollection;
            }
            return cachedEntities;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity to update
     * @param <T>    generic type
     * @return id (primary key)
     */
    @Override
    public <T> T update(T entity) {
        return executeWithIsClosedCheck(() -> {
            log.debug("Updating entity: {}", entity);
            var updatedEntity = getTransaction().isClosed() ? super.update(entity) : entity;
            persistenceContext.addToCache(updatedEntity);
            persistenceContext.addSnapshot(updatedEntity);
            return updatedEntity;
        });
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
        executeWithIsClosedCheck(() -> {
            if (getTransaction().isClosed()) {
                log.debug("Deleting entity with id - {} of type {}", id, type);
                super.deleteById(type, id);
            } else {
                var deleteAction = new DeleteEntityAction(type, id, jdbcQueryManager);
                log.debug("Adding delete action {}", deleteAction);
                actionQueue.add(deleteAction);
            }
            persistenceContext.removeEntityFromCacheByEntityKey(EntityUtils.createEntityKey(type, id));
            persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(type, id));
            return null;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity to delete
     * @param <T>    generic type
     */
    @Override
    public <T> void delete(T entity) {
        executeWithIsClosedCheck(() -> {
            if (getTransaction().isClosed()) {
                log.debug("Deleting entity - {}", entity);
                super.delete(entity);
            } else {
                var deleteAction = new DeleteEntityAction(entity, jdbcQueryManager);
                log.debug("Adding delete action {}", deleteAction);
                actionQueue.add(deleteAction);
            }
            persistenceContext.removeEntityFromCacheByEntityKey(EntityUtils.createEntityKey(entity));
            persistenceContext.removeEntityFromSnapshotByEntityKey(EntityUtils.createEntityKey(entity));
            return null;
        });
    }

    /**
     * {@inheritDoc}
     *
     * @return instance of {@link Transaction}
     */
    @Override
    public Transaction getTransaction() {
        return executeWithIsClosedCheck(() -> {
            var transaction = (TransactionImpl) super.getTransaction();
            transaction.setSession(this);
            return transaction;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void flush() {
        executeWithIsClosedCheck(() -> {
            log.debug("Flush session changes to DB.");
            persistenceContext.getChangedEntities()
                    .forEach(e -> actionQueue.add(new UpdateEntityAction(e, jdbcQueryManager)));
            actionQueue.processActions();
            return null;
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        executeWithIsClosedCheck(() -> {
            flush();
            persistenceContext.clear();
            super.close();
            isClosed = true;
            log.debug("Session closed.");
            return null;
        });
    }

    private <T> T executeWithIsClosedCheck(Supplier<T> supplier) {
        if (isClosed) {
            throw new SessionIsClosedException("Session is closed. No operation allowed");
        }
        return supplier.get();
    }
}
