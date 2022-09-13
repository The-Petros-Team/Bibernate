package com.bobocode.petros.bibernate.session.action;

import lombok.experimental.Delegate;

import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

public class ActionQueue implements Queue<EntityAction> {

    @Delegate(excludes = Add.class)
    private final Queue<EntityAction> queue;

    public ActionQueue() {
        this.queue = new PriorityQueue<>(comparing(EntityAction::priority));
    }

    public void processActions() {
        while (!queue.isEmpty()) {
            var action = queue.poll();
            action.execute();
        }
    }

    @Override
    public boolean add(EntityAction action) {
        if (queue.contains(action)) {
            return false;
        } else {
            return queue.add(action);
        }
    }

    private interface Add {
        boolean add(EntityAction action);
    }
}
