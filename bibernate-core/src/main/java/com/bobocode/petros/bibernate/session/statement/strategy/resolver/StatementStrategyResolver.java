package com.bobocode.petros.bibernate.session.statement.strategy.resolver;

import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;

/**
 * Helps to resolve an appropriate strategy to process sql query.
 * It decides on a fly what strategy should be applied to handle q query.
 */
public interface StatementStrategyResolver {

    /**
     * Resolves strategy and runs once resolved, so as a result configured instance of prepared statement is returned.
     *
     * @param queryType type of sql query
     * @param connection db connection
     * @param sqlQuery sql query
     * @param configOptions statement configuration options
     * @return configured prepared statement
     */
    PreparedStatement resolve(final QueryType queryType,
                              final Connection connection,
                              final String sqlQuery,
                              final StatementConfigurationOptions configOptions);

}
