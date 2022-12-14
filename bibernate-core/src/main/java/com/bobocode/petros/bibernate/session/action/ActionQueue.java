package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.Session;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

/**
 * Queue for actions. Keeps track of actions that are/going to be executed by framework.
 * Its worth mentioning that actions are executed lazily by calling {@link Session#flush()} or {@link Session#close()}
 * methods.
 * Initially queue is responsible for sorting actions by their priority.
 */
@Slf4j
public class ActionQueue implements Queue<EntityAction> {

    @Delegate(excludes = Add.class)
    private final Queue<EntityAction> queue;

    public ActionQueue() {
        this.queue = new PriorityQueue<>(comparing(EntityAction::priority));
    }

    /**
     * Process all actions one by one. Please keep in mind that all actions are ordered by their priority.
     */
    public void processActions() {
        log.debug("Start processing actions, queue size: {}", queue.size());
        while (!queue.isEmpty()) {
            var action = queue.poll();
            action.execute();
        }
    }

    /**
     * Adds an action to a queue. Does not support duplicated actions.
     *
     * @param action element whose presence in this collection is to be ensured
     * @return true if element was added or false otherwise
     */
    @Override
    public boolean add(EntityAction action) {
        if (queue.contains(action)) {
            log.debug("Entity action {} already exist. Skipping...", action);
            return false;
        } else {
            log.debug("Adding action {} to queue.", action);
            return queue.add(action);
        }
    }

    private interface Add {
        boolean add(EntityAction action);
    }
}
