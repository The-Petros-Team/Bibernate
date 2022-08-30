package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.query.QueryBuilder;
import com.bobocode.petros.bibernate.session.query.QueryResult;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.condition.Restrictions;
import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static java.sql.PreparedStatement.RETURN_GENERATED_KEYS;

public class DefaultJdbcQueryManager implements JdbcQueryManager {

    private static final String GENERATED_KEY = "GENERATED_KEY";

    private final DataSource dataSource;

    public DefaultJdbcQueryManager(final DataSource dataSource) {
        Objects.requireNonNull(dataSource, "Parameter [dataSource] must not be null!");
        this.dataSource = dataSource;
    }

    @Override
    public <T> T persist(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        final String sql = QueryBuilder.buildQuery(entity.getClass(), QueryType.INSERT, Collections.emptyList());
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = QueryBuilder.prepareInsertStatement(connection.prepareStatement(sql, RETURN_GENERATED_KEYS), entity)) {
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                final Field field = EntityUtils.getIdField(entity.getClass());
                field.setAccessible(true);
                field.set(entity, generatedKeys.getObject(GENERATED_KEY, EntityUtils.getIdField(entity.getClass()).getType()));
            }
            return entity;
        } catch (SQLException | IllegalAccessException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    @Override
    public <T, C extends Collection<T>> Collection<T> find(final Class<T> type, final List<Restriction> restrictions) {
        Objects.requireNonNull(type, "Parameter [type] must not be null!");
        Objects.requireNonNull(restrictions, "Parameter [restrictions] must not be null!");
        final String sql = QueryBuilder.buildQuery(type, QueryType.SELECT, restrictions);
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = QueryBuilder.prepareSelectStatement(connection.prepareStatement(sql), type, restrictions)) {
            final ResultSet resultSet = statement.executeQuery();
            final QueryResult queryResult = EntityUtils.getQueryResult(resultSet, type);
            return queryResult.getResults().size() <= 1 ?
                    queryResult.wrap(queryResult.getSingleResult()) : queryResult.getResultList();
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T update(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        /*final String sql = QueryBuilder.buildQuery(entity.getClass(), QueryType.UPDATE, Arrays.asList(Restrictions.idEq(EntityUtils, entity)));
        try (final Connection connection = this.dataSource.getConnection();
        final PreparedStatement statement = QueryBuilder.)*/
        return null;
    }

    @Override
    public <ID> void deleteById(final ID id) {

    }

    @Override
    public <T, ID> void delete(final Class<T> type, final ID id) {

    }

    @Override
    public Transaction getTransaction() {
        return null;
    }
}
