package com.bobocode.petros.bibernate.utils;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

/**
 * Utility class that operates over {@link PreparedStatement} for the different {@link QueryType}.
 */
@UtilityClass
public class StatementUtils {

    /**
     * Applies specific logic required for SELECT statement.
     *
     * @param statement    instance of {@link PreparedStatement}
     * @param entityClass  instance of {@link PreparedStatement}
     * @param restrictions applicable restrictions
     * @param <T>          generic type
     * @return {@link PreparedStatement} with all parameters set
     */
    public <T> PreparedStatement prepareSelectStatement(final PreparedStatement statement,
                                                        final Class<T> entityClass,
                                                        final List<Restriction> restrictions) {
        if (!restrictions.isEmpty()) {
            final List<Field> fields = EntityUtils.sortEntityFieldsSkipPK(entityClass);
            int columnNumber = 1;
            for (final Restriction restriction : restrictions) {
                if (fields.stream().anyMatch(f -> f.getName().equals(restriction.getPropertyName()))) {
                    try {
                        statement.setObject(columnNumber++, restriction.getValue());
                    } catch (SQLException e) {
                        throw new JdbcOperationException(e.getMessage(), e);
                    }
                }
            }
        }
        return statement;
    }

    /**
     * Applies specific logic required for INSERT statement.
     *
     * @param statement instance of {@link PreparedStatement}
     * @param entity    insertable entity
     * @param <T>       generic type
     * @return {@link PreparedStatement} with all parameters set
     */
    public <T> PreparedStatement prepareInsertStatement(final PreparedStatement statement, final T entity) {
        final List<Field> fields = EntityUtils.sortEntityFieldsSkipPK(entity.getClass());
        int columnNumber = 1;
        for (final Field field : fields) {
            field.setAccessible(true);
            try {
                statement.setObject(columnNumber++, field.get(entity));
            } catch (SQLException | IllegalAccessException e) {
                throw new JdbcOperationException(e.getMessage(), e);
            }
        }
        return statement;
    }

    /**
     * Applies specific logic required for UPDATE statement.
     *
     * @param statement instance of {@link PreparedStatement}
     * @param entity    updatable entity
     * @param <T>       generic type
     * @return {@link PreparedStatement} with all parameters set
     */
    public <T> PreparedStatement prepareUpdateStatement(final PreparedStatement statement, final T entity) {
        final List<Field> fields = EntityUtils.sortEntityFieldsSkipPK(entity.getClass());
        int columnNumber = 1;
        try {
            for (final Field field : fields) {
                field.setAccessible(true);
                statement.setObject(columnNumber++, field.get(entity));
            }
            final Field idField = EntityUtils.getIdField(entity.getClass());
            idField.setAccessible(true);
            statement.setObject(columnNumber, idField.get(entity));
            return statement;
        } catch (SQLException | IllegalAccessException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * Applies specific logic required for DELETE statement.
     *
     * @param statement   instance of {@link PreparedStatement}
     * @param entityClass entity class
     * @param id          id (primary key)
     * @param <T>         generic type
     * @return {@link PreparedStatement} with all parameters set
     */
    public <T> PreparedStatement prepareDeleteStatement(final PreparedStatement statement,
                                                        final Class<T> entityClass,
                                                        final Object id) {
        EntityUtils.getIdField(entityClass);
        int columnNumber = 1;
        try {
            statement.setObject(columnNumber, id);
            return statement;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }
}
