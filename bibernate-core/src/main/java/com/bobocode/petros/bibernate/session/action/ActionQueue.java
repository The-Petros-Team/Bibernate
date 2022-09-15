package com.bobocode.petros.bibernate.session.action;

import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;

import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

@Slf4j
public class ActionQueue implements Queue<EntityAction> {

    @Delegate(excludes = Add.class)
    private final Queue<EntityAction> queue;

    public ActionQueue() {
        this.queue = new PriorityQueue<>(comparing(EntityAction::priority));
    }

    public void processActions() {
        log.debug("Start processing actions, queue size: {}", queue.size());
        while (!queue.isEmpty()) {
            var action = queue.poll();
            action.execute();
        }
    }

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
