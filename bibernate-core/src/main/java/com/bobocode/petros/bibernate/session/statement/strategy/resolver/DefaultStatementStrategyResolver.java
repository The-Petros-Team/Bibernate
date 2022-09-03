package com.bobocode.petros.bibernate.session.statement.strategy.resolver;

import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.statement.strategy.StatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.DeleteStatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.InsertStatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.SelectStatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.impl.UpdateStatementStrategy;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class DefaultStatementStrategyResolver implements StatementStrategyResolver {

    private final StatementStrategy insertStatementStrategy;
    private final StatementStrategy selectStatementStrategy;
    private final StatementStrategy updateStatementStrategy;
    private final StatementStrategy deleteStatementStrategy;

    private DefaultStatementStrategyResolver() {
        this.insertStatementStrategy = new InsertStatementStrategy();
        this.selectStatementStrategy = new SelectStatementStrategy();
        this.updateStatementStrategy = new UpdateStatementStrategy();
        this.deleteStatementStrategy = new DeleteStatementStrategy();
    }

    public PreparedStatement resolve(final QueryType queryType,
                                     final Connection connection,
                                     final String sqlQuery,
                                     final StatementConfigurationOptions configOptions) {
        return switch (queryType) {
            case INSERT -> this.insertStatementStrategy.configure(connection, sqlQuery, configOptions);
            case SELECT -> this.selectStatementStrategy.configure(connection, sqlQuery, configOptions);
            case UPDATE -> this.updateStatementStrategy.configure(connection, sqlQuery, configOptions);
            case DELETE -> this.deleteStatementStrategy.configure(connection, sqlQuery, configOptions);
        };
    }

    public static StatementStrategyResolver getInstance() {
        return StatementStrategyResolverHolder.get();
    }

    enum StatementStrategyResolverHolder {

        ;

        private static final StatementStrategyResolver STRATEGY_RESOLVER = new DefaultStatementStrategyResolver();

        static StatementStrategyResolver get() {
            return STRATEGY_RESOLVER;
        }
    }
}
