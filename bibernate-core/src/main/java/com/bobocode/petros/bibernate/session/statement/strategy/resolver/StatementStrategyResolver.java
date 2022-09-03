package com.bobocode.petros.bibernate.session.statement.strategy.resolver;

import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface StatementStrategyResolver {

    PreparedStatement resolve(final QueryType queryType,
                              final Connection connection,
                              final String sqlQuery,
                              final StatementConfigurationOptions configOptions);

}
