package com.bobocode.petros.bibernate.session.statement.strategy.impl;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.statement.strategy.StatementStrategy;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SelectStatementStrategy implements StatementStrategy {

    @Override
    public PreparedStatement configure(final Connection connection,
                                       final String sqlQuery,
                                       final StatementConfigurationOptions configOptions) {
        try {
            final PreparedStatement statement = connection.prepareStatement(sqlQuery);
            final List<Restriction> restrictions = configOptions.getRestrictions();
            if (!restrictions.isEmpty()) {
                final List<Field> fields = EntityUtils.sortEntityFieldsSkipPK(configOptions.getEntityClass());
                int columnNumber = 1;
                for (final Restriction restriction : restrictions) {
                    if (fields.stream().anyMatch(f -> f.getName().equals(restriction.getPropertyName()))) {
                        statement.setObject(columnNumber++, restriction.getValue());
                    }
                }
            }
            return statement;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }
}
