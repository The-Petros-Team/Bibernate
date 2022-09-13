package com.bobocode.petros.bibernate.session.statement.strategy.impl;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.statement.strategy.StatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

/**
 * implementation of {@link StatementStrategy} that applies configuration to insert-based prepared statement.
 */
public class InsertStatementStrategy implements StatementStrategy {

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
            final PreparedStatement statement = connection.prepareStatement(sqlQuery, RETURN_GENERATED_KEYS);
            final List<Field> fields = EntityUtils.sortEntityFieldsSkipPK(configOptions.getEntityClass());
            int columnNumber = 1;
            for (final Field field : fields) {
                field.setAccessible(true);
                statement.setObject(columnNumber++, field.get(configOptions.getEntity()));
            }
            return statement;
        } catch (SQLException | IllegalAccessException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }
}
