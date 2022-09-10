package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.session.action.ActionQueue;
import com.bobocode.petros.bibernate.session.action.DeleteEntityAction;
import com.bobocode.petros.bibernate.session.action.InsertEntityAction;
import com.bobocode.petros.bibernate.session.action.UpdateEntityAction;
import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;

import java.util.HashMap;
import java.util.Map;

public class CacheableSession extends DefaultSession {

    private Map<EntityKey<?>, Object> entityCache = new HashMap<>();
    private PersistenceContext persistenceContext;
    private ActionQueue actionQueue;

    public CacheableSession(JdbcQueryManager jdbcQueryManager) {
        super(jdbcQueryManager);
        this.persistenceContext = new PersistenceContext();
        this.actionQueue = new ActionQueue();
    }

    @Override
    public <T> T persist(T entity) {
        actionQueue.add(new InsertEntityAction(entity, jdbcQueryManager));
        return super.persist(entity);
    }

    @Override
    public <T> T update(T entity) {
        actionQueue.add(new UpdateEntityAction(entity, jdbcQueryManager));
        return super.update(entity);
    }

    @Override
    public <T> void deleteById(Class<T> type, Object id) {
        actionQueue.add(new DeleteEntityAction(type, id, jdbcQueryManager));
        super.deleteById(type, id);
    }

    @Override
    public <T> void delete(T entity) {
        actionQueue.add(new DeleteEntityAction(entity, jdbcQueryManager));
        super.delete(entity);
    }
    @Override
    public void flush() {
        actionQueue.execute();
    }

    @Override
    public void close() {
        flush();
        super.close();
        persistenceContext.clear();
    }
}
