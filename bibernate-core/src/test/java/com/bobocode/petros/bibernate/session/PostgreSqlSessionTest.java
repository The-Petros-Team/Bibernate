package com.bobocode.petros.bibernate.session;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class PostgreSqlSessionTest extends AbstractSessionTest {
    @Container
    private final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE);

    @Override
    void setJdbcDatabaseContainer() {
        AbstractSessionTest.genericContainer = container;
    }
}
