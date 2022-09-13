package com.bobocode.petros.bibernate.session.action;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Action priorities.
 * See {@link UpdateEntityAction}
 * See {@link DeleteEntityAction}
 */
@RequiredArgsConstructor
public enum ActionPriority {
    INSERT_ACTION(1),
    UPDATE_ACTION(2),
    DELETE_ACTION(3);

    @Getter
    private final int priority;
}
