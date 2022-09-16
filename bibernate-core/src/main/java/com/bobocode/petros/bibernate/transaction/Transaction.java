package com.bobocode.petros.bibernate.transaction;

/**
 * Allows working with transactions in a manual mode (with auto-commit mode set to false).
 */
public interface Transaction {

    /**
     * Starts the transaction.
     */
    void begin();

    /**
     * Performs a transaction commit.
     */
    void commit();

    /**
     * Performs transaction rollback.
     */
    void rollback();

    /**
     * Checks whether a transaction is closed.
     *
     * @return true if transaction is closed or false otherwise
     */
    boolean isClosed();
}
