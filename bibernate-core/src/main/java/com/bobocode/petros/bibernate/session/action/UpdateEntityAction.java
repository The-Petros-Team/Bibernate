package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

/**
 * Implementation of {@link EntityAction} that represents update operation that is supposed to be executed against
 * a given entity.
 */
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "jdbcQueryManager")
public class UpdateEntityAction implements EntityAction {

    private final Object entity;
    private final JdbcQueryManager jdbcQueryManager;

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        jdbcQueryManager.update(entity);
    }

    /**
     * {@inheritDoc}
     *
     * @return priority value
     */
    @Override
    public int priority() {
        return ActionPriority.UPDATE_ACTION.getPriority();
    }
}
