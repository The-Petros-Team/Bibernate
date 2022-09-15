package com.bobocode.petros.bibernate.transaction;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.Session;
import lombok.Setter;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Implementation of {@link Transaction}.
 */
public class TransactionImpl implements Transaction {

    private final Connection connection;
    private boolean isOpened;
    @Setter
    private Session session;

    public TransactionImpl(Connection connection) {
        Objects.requireNonNull(connection, "Parameter [connection] must not be null!");
        this.connection = connection;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void begin() {
        try {
            connection.setAutoCommit(false);
            this.isOpened = true;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void commit() {
        try (connection) {
            if (!connection.getAutoCommit()) {
                this.session.flush();
                this.connection.commit();
            }
            this.isOpened = false;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     *
     * @return true if transaction is closed or false otherwise
     */
    @Override
    public boolean isClosed() {
        return !this.isOpened;
    }
}
