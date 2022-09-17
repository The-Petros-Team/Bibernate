package com.bobocode.petros.bibernate.session.statement.strategy.impl;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.statement.strategy.StatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Implementation of {@link StatementStrategy} that applies configuration to a delete-based prepare statement.
 */
public class DeleteStatementStrategy implements StatementStrategy {

    /**
     * {@inheritDoc}
     *
     * @param connection    db connection
     * @param sqlQuery      sql query
     * @param configOptions statement configuration options
     * @return configured instance of {@link PreparedStatement}
     */
    @Override
    public PreparedStatement configure(final Connection connection,
                                       final String sqlQuery,
                                       final StatementConfigurationOptions configOptions) {
        try {
            final PreparedStatement statement = connection.prepareStatement(sqlQuery);
            int columnNumber = 1;
            statement.setObject(columnNumber, configOptions.getId());
            return statement;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }
}
