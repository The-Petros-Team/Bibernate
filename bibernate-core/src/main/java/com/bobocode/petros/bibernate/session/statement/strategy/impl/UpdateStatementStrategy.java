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

public class UpdateStatementStrategy implements StatementStrategy {

    @Override
    public PreparedStatement configure(final Connection connection,
                                       final String sqlQuery,
                                       final StatementConfigurationOptions configOptions) {
        try {
            final PreparedStatement statement = connection.prepareStatement(sqlQuery);
            final List<Field> fields = EntityUtils.sortEntityFieldsSkipPK(configOptions.getEntityClass());
            int columnNumber = 1;
            for (final Field field : fields) {
                field.setAccessible(true);
                // TODO check if it is allowed to set null value for column, if not -> skip or throw exception ?
                statement.setObject(columnNumber++, field.get(configOptions.getEntity()));
            }
            final Field idField = EntityUtils.getIdField(configOptions.getEntityClass());
            idField.setAccessible(true);
            statement.setObject(columnNumber, idField.get(configOptions.getEntity()));
            return statement;
        } catch (SQLException | IllegalAccessException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }
}
