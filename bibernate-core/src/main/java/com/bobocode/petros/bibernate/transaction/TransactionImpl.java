package com.bobocode.petros.bibernate.transaction;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class TransactionImpl implements Transaction {

    private boolean isOpened;
    private final Connection connection;

    public TransactionImpl(Connection connection) {
        Objects.requireNonNull(connection, "Parameter [connection] must not be null!");
        this.connection = connection;
    }

    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
            this.isOpened = true;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    @Override
    public void commit() {
        try (connection) {
            if (!connection.getAutoCommit()) {
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
                this.connection.rollback();
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
