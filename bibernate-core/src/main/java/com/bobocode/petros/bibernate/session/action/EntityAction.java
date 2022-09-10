package com.bobocode.petros.bibernate.session.action;

public interface EntityAction {
    void execute();

    int priority();
}
