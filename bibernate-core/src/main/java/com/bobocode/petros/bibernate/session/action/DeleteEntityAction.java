package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.jdbc.JdbcQueryManager;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementation of {@link EntityAction} that represents delete operation that is supposed to be executed against
 * a given entity.
 */
@Slf4j
@RequiredArgsConstructor
@EqualsAndHashCode(exclude = "jdbcQueryManager")
@ToString(of = {"type", "id"})
public class DeleteEntityAction implements EntityAction {

    private final Class<?> type;
    private final Object id;
    private final JdbcQueryManager jdbcQueryManager;

    public DeleteEntityAction(Object entity, JdbcQueryManager jdbcQueryManager) {
        this.type = entity.getClass();
        this.id = EntityUtils.getIdValue(entity);
        this.jdbcQueryManager = jdbcQueryManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute() {
        log.debug("Executing delete action: type - {}, id - {}", type, id);
        jdbcQueryManager.deleteById(type, id);
    }

    /**
     * {@inheritDoc}
     *
     * @return priority value
     */
    @Override
    public int priority() {
        return ActionPriority.DELETE_ACTION.getPriority();
    }
}
