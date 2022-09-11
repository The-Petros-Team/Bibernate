package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "jdbcQueryManager")
public class UpdateEntityAction implements EntityAction {
    private final Object entity;
    private final JdbcQueryManager jdbcQueryManager;

    @Override
    public void execute() {
        jdbcQueryManager.update(entity);
    }

    @Override
    public int priority() {
        return ActionPriority.UPDATE_ACTION.getPriority();
    }
}
