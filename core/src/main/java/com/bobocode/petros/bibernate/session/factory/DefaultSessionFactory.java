package com.bobocode.petros.bibernate.session.factory;

import com.bobocode.petros.bibernate.configuration.ConnectionConfiguration;
import com.bobocode.petros.bibernate.session.Session;

import java.util.Set;

public class DefaultSessionFactory implements SessionFactory {

    private ConnectionConfiguration configuration;
    private Set<String> entityPackages;

    private DefaultSessionFactory(ConnectionConfiguration configuration, Set<String> entityPackages) {
        this.configuration = configuration;
        this.entityPackages = entityPackages;
    }

    @Override
    public Session openSession() {
        return null;
    }
}
