package com.bobocode.petros.bibernate.session.action;

/**
 * Basic interface for an action mechanism.
 */
public interface EntityAction {

    /**
     * Action logic is executed within this method.
     * Basically if it's update action, then appropriate update logic is supposed to be executed.
     */
    void execute();

    /**
     * Returns action priority value.
     * See {@link ActionPriority}
     *
     * @return priority value
     */
    int priority();
}
