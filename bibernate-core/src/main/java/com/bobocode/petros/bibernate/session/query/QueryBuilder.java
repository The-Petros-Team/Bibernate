package com.bobocode.petros.bibernate.session.query;

import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.query.enums.SqlOperator;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.util.List;
import java.util.Objects;

/**
 * Helper class that is used to build SQL queries for the statically specified parameters.
 */
public class QueryBuilder {

    /**
     * Template for INSERT query.
     */
    private static final String INSERT = "INSERT INTO %s (%s) VALUES (%s)";

    /**
     * Template for SELECT query.
     */
    private static final String SELECT = "SELECT %s FROM %s%s";

    /**
     * Template for UPDATE query.
     */
    private static final String UPDATE = "UPDATE %s SET %s%s";

    /**
     * Template for DELETE query.
     */
    private static final String DELETE = "DELETE FROM %s%s";

    /**
     * Encapsulates a main logic for query construction. Construction process is type-based, see {@link QueryType}.
     * Basically, for each type of query all the necessary parameters are processed by this method.
     *
     * @param entityClass  entity class
     * @param queryType    type of query
     * @param restrictions applicable restrictions
     * @param <T>          generic type
     * @return SQL query that is ready to be consumed by {@link java.sql.PreparedStatement}
     */
    public static <T> String buildQuery(final Class<T> entityClass, final QueryType queryType, final List<Restriction> restrictions) {
        Objects.requireNonNull(entityClass, "Parameter [entityClass] must not be null!");
        Objects.requireNonNull(queryType, "Parameter [queryType] must not be null!");
        final String tableName = EntityUtils.getTableName(entityClass);
        String sql = null;
        switch (queryType) {
            case INSERT -> {
                final List<String> columnNames = EntityUtils.getColumnNames(
                        EntityUtils.sortEntityFieldsSkipPK(entityClass)
                );
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
            case UPDATE -> sql = String.format(
                    UPDATE,
                    EntityUtils.getTableName(entityClass),
                    EntityUtils.getMappedQueryColumns(entityClass, false),
                    String.format(" %s %s", SqlOperator.WHERE.getValue(), EntityUtils.getMappedQueryColumns(entityClass, true))
            );
            case DELETE -> sql = String.format(
                    DELETE,
                    EntityUtils.getTableName(entityClass),
                    String.format(" %s %s", SqlOperator.WHERE.getValue(), EntityUtils.getMappedQueryColumns(entityClass, true))
            );
        }
        return sql;
    }
}
