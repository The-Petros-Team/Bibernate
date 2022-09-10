package com.bobocode.petros.bibernate.session.action;

import lombok.experimental.Delegate;

import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

public class ActionQueue implements Queue<EntityAction> {

    @Delegate
    private final Queue<EntityAction> queue;

    public ActionQueue() {
        this.queue = new PriorityQueue<>(comparing(EntityAction::priority));
    }

    public void execute() {
        while (!queue.isEmpty()) {
            var action = queue.poll();
            action.execute();
        }
    }
}
