package com.bobocode.petros.bibernate.session.statement.strategy;

import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.DeleteStatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.InsertStatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.SelectStatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.UpdateStatementStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Strategy for prepared statement configuration.
 * Basically there are several types of queries, like insert, update etc. and each of them requires prepared
 * statement to be configured but a bit in a different way.
 *
 * More details:
 * See {@link QueryType}
 * See {@link InsertStatementStrategy}
 * See {@link SelectStatementStrategy}
 * See {@link UpdateStatementStrategy}
 * See {@link DeleteStatementStrategy}
 */
public interface StatementStrategy {

    /**
     * Configures and returns an instance of prepared statement, created from a given connection.
     *
     * @param connection    db connection
     * @param sqlQuery      sql query
     * @param configOptions statement configuration options
     * @return configured instance of {@link PreparedStatement}
     */
    PreparedStatement configure(final Connection connection,
                                final String sqlQuery,
                                final StatementConfigurationOptions configOptions);

}
