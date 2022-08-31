package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.query.QueryBuilder;
import com.bobocode.petros.bibernate.session.query.QueryResult;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;
import com.bobocode.petros.bibernate.utils.StatementUtils;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.sql.PreparedStatement.RETURN_GENERATED_KEYS;

/**
 * Implementation of {@link JdbcQueryManager}.
 */
@Slf4j
public class DefaultJdbcQueryManager implements JdbcQueryManager {

    private static final String GENERATED_KEY = "GENERATED_KEY";

    private final DataSource dataSource;

    public DefaultJdbcQueryManager(final DataSource dataSource) {
        Objects.requireNonNull(dataSource, "Parameter [dataSource] must not be null!");
        this.dataSource = dataSource;
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity that is supposed to be persisted
     * @param <T>    generic type
     * @return persisted entity with id
     */
    @Override
    public <T> T persist(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        final String sql = QueryBuilder.buildQuery(entity.getClass(), QueryType.INSERT, Collections.emptyList());
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = StatementUtils.prepareInsertStatement(connection.prepareStatement(sql, RETURN_GENERATED_KEYS), entity)) {
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                final Field field = EntityUtils.getIdField(entity.getClass());
                field.setAccessible(true);
                field.set(entity, generatedKeys.getObject(GENERATED_KEY, EntityUtils.getIdField(entity.getClass()).getType()));
            }
            log.debug("Query: '{}', result: {}", sql, entity);
            return entity;
        } catch (SQLException | IllegalAccessException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param type         class of an entity to search for
     * @param restrictions applicable restrictions
     * @param <T>          generic type
     * @param <C>          collection of type T
     * @return collection of entities
     */
    @Override
    public <T, C extends Collection<T>> C find(final Class<T> type, final List<Restriction> restrictions) {
        Objects.requireNonNull(type, "Parameter [type] must not be null!");
        Objects.requireNonNull(restrictions, "Parameter [restrictions] must not be null!");
        final String sql = QueryBuilder.buildQuery(type, QueryType.SELECT, restrictions);
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = StatementUtils.prepareSelectStatement(connection.prepareStatement(sql), type, restrictions)) {
            final ResultSet resultSet = statement.executeQuery();
            final QueryResult queryResult = EntityUtils.getQueryResult(resultSet, type);
            final C result = queryResult.isSingle() ? queryResult.wrap(queryResult.getSingleResult()) :
                    queryResult.isMulti() ? queryResult.getResultList() : (C) Collections.emptyList();
            log.debug("Query: '{}', result: {}", sql, result);
            return result;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity that is supposed to be updated
     * @param <T>    generic type
     * @return updated entity
     */
    @Override
    public <T> T update(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        final String sql = QueryBuilder.buildQuery(entity.getClass(), QueryType.UPDATE, Collections.emptyList());
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = StatementUtils.prepareUpdateStatement(connection.prepareStatement(sql), entity)) {
            final int rowsUpdated = statement.executeUpdate();
            log.debug("Updated '{}' rows for query {}", rowsUpdated, sql);
            return entity;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param entity entity that is supposed to be deleted
     * @param <T>    generic type
     */
    @Override
    public <T> void delete(final T entity) {
        Objects.requireNonNull(entity, "Parameter [entity] must not be null!");
        try {
            final Field idField = EntityUtils.getIdField(entity.getClass());
            idField.setAccessible(true);
            deleteById(entity.getClass(), idField.get(entity));
        } catch (IllegalAccessException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param type entity type
     * @param id   id (primary key)
     * @param <T>  generic type
     * @param <ID> primary key type of the given entity
     */
    @Override
    public <T, ID> void deleteById(final Class<T> type, final ID id) {
        Objects.requireNonNull(id, "Parameter [id] must not be null!");
        final String sql = QueryBuilder.buildQuery(type, QueryType.DELETE, Collections.emptyList());
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = StatementUtils.prepareDeleteStatement(connection.prepareStatement(sql), type, id)) {
            final int rowsUpdated = statement.executeUpdate();
            log.debug("Updated '{}' rows for query {}", rowsUpdated, sql);
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return instance of {@link Transaction}
     */
    @Override
    public Transaction getTransaction() {
        return null;
    }
}
