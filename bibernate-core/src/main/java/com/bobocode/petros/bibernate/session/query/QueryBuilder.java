package com.bobocode.petros.bibernate.session.query;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.query.enums.SqlOperator;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class QueryBuilder {

    private static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";
    private static final String SELECT = "SELECT %s FROM %s%s";

    public static <T> String buildQuery(final Class<T> entityClass,
                                        final QueryType queryType,
                                        final List<Restriction> restrictions) {
        Objects.requireNonNull(entityClass, "Parameter [entityClass] must not be null!");
        Objects.requireNonNull(queryType, "Parameter [queryType] must not be null!");
        final String tableName = EntityUtils.getTableName(entityClass);
        String sql = null;
        switch (queryType) {
            case INSERT -> {
                final List<String> columnNames = EntityUtils.getColumnNames(EntityUtils.sortEntityFields(entityClass));
                final String columns = String.join(",", columnNames);
                sql = String.format(INSERT, tableName, columns, EntityUtils.getMappedQueryValues(columnNames));
            }
            case SELECT -> {
                String conditions = EntityUtils.getMappedQueryConditions(restrictions);
                if (!restrictions.isEmpty()) {
                    conditions = String.format(" %s %s", SqlOperator.WHERE.getValue(), conditions);
                }
                sql = String.format(SELECT, EntityUtils.getColumnNames(entityClass), tableName, conditions);
            }
            case UPDATE -> {}
            case DELETE -> {}
        }
        log.debug("Query: {}", sql);
        return sql;
    }

    public static <T> PreparedStatement prepareSelectStatement(final PreparedStatement statement,
                                                               final Class<T> entityClass,
                                                               final List<Restriction> restrictions) {
        if (!restrictions.isEmpty()) {
            final List<Field> fields = EntityUtils.sortEntityFields(entityClass);
            int magicNumber = 1;
            for (final Restriction restriction : restrictions) {
                if (fields.stream().anyMatch(f -> f.getName().equals(restriction.getPropertyName()))) {
                    try {
                        statement.setObject(magicNumber++, restriction.getValue());
                    } catch (SQLException e) {
                        throw new JdbcOperationException(e.getMessage(), e);
                    }
                }
            }
        }
        return statement;
    }

    public static <T> PreparedStatement prepareInsertStatement(final PreparedStatement statement, final T entity) {
        final List<Field> fields = EntityUtils.sortEntityFields(entity.getClass());
        int magicNumber = 1;
        for (final Field field : fields) {
            field.setAccessible(true);
            try {
                statement.setObject(magicNumber++, field.get(entity));
            } catch (SQLException | IllegalAccessException e) {
                throw new JdbcOperationException(e.getMessage(), e);
            }
        }
        return statement;
    }

}
