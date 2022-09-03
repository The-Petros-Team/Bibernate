package com.bobocode.petros.bibernate.session;

import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class MySqlSessionTest extends AbstractSessionTest {
    @Container
    private final MySQLContainer<?> container = new MySQLContainer<>(MySQLContainer.NAME);

    @Override
    void setJdbcDatabaseContainer() {
        AbstractSessionTest.genericContainer = container;
    }
}
