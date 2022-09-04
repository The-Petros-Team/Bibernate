package com.bobocode.petros.bibernate.session;

import org.testcontainers.containers.MySQLContainer;

public class MySqlSessionTest extends AbstractSessionTest {
    private final MySQLContainer<?> container = new MySQLContainer<>(MySQLContainer.NAME)
            .withInitScript("script/init-mysql-db.sql")
            .withReuse(true);

    @Override
    void setJdbcDatabaseContainer() {
        AbstractSessionTest.genericContainer = container;
    }
}
