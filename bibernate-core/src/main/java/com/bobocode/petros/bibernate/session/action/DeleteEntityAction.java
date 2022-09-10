package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteEntityAction implements EntityAction {
    private final Class<?> type;
    private final Object id;
    private final JdbcQueryManager jdbcQueryManager;

    public DeleteEntityAction(Object entity, JdbcQueryManager jdbcQueryManager) {
        this.type = entity.getClass();
        this.id = EntityUtils.getIdValue(entity);
        this.jdbcQueryManager = jdbcQueryManager;
    }

    @Override
    public void execute() {
        jdbcQueryManager.deleteById(type, id);
    }

    @Override
    public int priority() {
        return ActionPriority.DELETE_ACTION.getPriority();
    }
}
