package com.bobocode.petros.bibernate.session;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreSqlSessionTest extends AbstractSessionTest {

    private final PostgreSQLContainer<?> container = new PostgreSQLContainer<>(PostgreSQLContainer.IMAGE)
            .withInitScript("script/init-postgres-db.sql")
            .withReuse(true);

    @Override
    void setJdbcDatabaseContainer() {
        AbstractSessionTest.genericContainer = container;
    }
}
