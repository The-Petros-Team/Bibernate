package com.bobocode.petros.bibernate.transaction;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.Session;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

@Slf4j
public class TransactionImpl implements Transaction {

    private final Connection connection;
    private boolean isOpened;
    @Setter
    private Session session;

    public TransactionImpl(Connection connection) {
        Objects.requireNonNull(connection, "Parameter [connection] must not be null!");
        this.connection = connection;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
            this.isOpened = true;
            log.trace("Transaction is started.");
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    @Override
    public void commit() {
        try (connection) {
            if (!connection.getAutoCommit()) {
                log.trace("Committing transaction.");
                this.session.flush();
                this.connection.commit();
            }
            this.isOpened = false;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    @Override
    public void rollback() {
        try (connection) {
            if (!connection.getAutoCommit()) {
                log.trace("Rolling back transaction.");
                this.connection.rollback();
                log.trace("Transaction is rolled back.");
            }
            this.isOpened = false;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    @Override
    public boolean isClosed() {
        return !this.isOpened;
    }
}
