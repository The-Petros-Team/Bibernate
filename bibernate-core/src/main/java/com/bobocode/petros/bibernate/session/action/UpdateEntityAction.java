package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "jdbcQueryManager")
@ToString(of = "entity")
public class UpdateEntityAction implements EntityAction {
    private final Object entity;
    private final JdbcQueryManager jdbcQueryManager;

    @Override
    public void execute() {
        log.debug("Executing update action for class - {}, entity - {}", entity.getClass(), entity);
        jdbcQueryManager.update(entity);
    }

    @Override
    public int priority() {
        return ActionPriority.UPDATE_ACTION.getPriority();
    }
}
