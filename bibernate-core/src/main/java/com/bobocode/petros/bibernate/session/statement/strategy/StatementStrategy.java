package com.bobocode.petros.bibernate.session.statement.strategy;

import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;

import java.sql.Connection;
import java.sql.PreparedStatement;

public interface StatementStrategy {

    PreparedStatement configure(final Connection connection,
                                final String sqlQuery,
                                final StatementConfigurationOptions configOptions);

}
