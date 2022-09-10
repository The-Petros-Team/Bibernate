package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InsertEntityAction implements EntityAction {
    private final Object entity;
    private final JdbcQueryManager jdbcQueryManager;

    @Override
    public void execute() {
        jdbcQueryManager.persist(entity);
    }

    @Override
    public int priority() {
        return ActionPriority.INSERT_ACTION.getPriority();
    }
}
