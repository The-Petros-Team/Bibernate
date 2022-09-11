package com.bobocode.petros.bibernate.session.action;

import com.bobocode.petros.bibernate.session.context.PersistenceContext;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.experimental.Delegate;

import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparing;

public class ActionQueue implements Queue<EntityAction> {

    @Delegate
    private final Queue<EntityAction> queue;
    private final PersistenceContext persistenceContext;

    public ActionQueue(PersistenceContext persistenceContext) {
        this.queue = new PriorityQueue<>(comparing(EntityAction::priority));
        this.persistenceContext = persistenceContext;
    }

    public void processActions() {
        while (!queue.isEmpty()) {
            var action = queue.poll();
            if (action instanceof UpdateEntityAction updateEntityAction) {
                var entityToUpdate = updateEntityAction.getEntity();

//                persistenceContext.getEntityFromCacheById()createEntityKey(entityToUpdate)
            }
            action.execute();
        }
    }
}
