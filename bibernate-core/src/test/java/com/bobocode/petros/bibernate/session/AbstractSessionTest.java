package com.bobocode.petros.bibernate.session;

import com.bobocode.petros.bibernate.configuration.BasicDataSourceConfiguration;
import com.bobocode.petros.bibernate.session.factory.DefaultSessionFactory;
import com.bobocode.petros.bibernate.session.factory.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Testcontainers
public abstract class AbstractSessionTest {
    protected static JdbcDatabaseContainer<?> genericContainer;
    private static SessionFactory sessionFactory;

    abstract void setJdbcDatabaseContainer();

    @BeforeEach
    void setUp() {
        setJdbcDatabaseContainer();
        sessionFactory = new DefaultSessionFactory(BasicDataSourceConfiguration.builder()
                .driverClassName(genericContainer.getDriverClassName())
                .url(genericContainer.getJdbcUrl())
                .username(genericContainer.getUsername())
                .password(genericContainer.getPassword())
                .poolSize(1)
                .build(), Collections.emptySet());
    }

    @AfterAll
    static void tearDown() {
        genericContainer.close();
    }

    //Delete this test after using sessionFactory in other tests
    @Test
    void ifSuccessThenSessionFactoryNotNull() {
        var session = sessionFactory.openSession();
        assertNotNull(session);
    }
}
