package com.bobocode.petros.bibernate.session.jdbc;

import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.session.query.QueryBuilder;
import com.bobocode.petros.bibernate.session.query.QueryResult;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import com.bobocode.petros.bibernate.session.query.enums.QueryType;
import com.bobocode.petros.bibernate.session.statement.strategy.resolver.StatementStrategyResolver;
import com.bobocode.petros.bibernate.session.statement.strategy.resolver.StrategyResolver;
import com.bobocode.petros.bibernate.session.statement.strategy.config.StatementConfigurationOptions;
import com.bobocode.petros.bibernate.transaction.Transaction;
import com.bobocode.petros.bibernate.utils.EntityUtils;
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

/**
 * Implementation of {@link JdbcQueryManager}.
 */
@Slf4j
public class DefaultJdbcQueryManager implements JdbcQueryManager {

    private static final String GENERATED_KEY = "GENERATED_KEY";

    private final DataSource dataSource;
    private final StrategyResolver strategyResolver;

    public DefaultJdbcQueryManager(final DataSource dataSource) {
        Objects.requireNonNull(dataSource, "Parameter [dataSource] must not be null!");
        this.dataSource = dataSource;
        this.strategyResolver = StatementStrategyResolver.getInstance();
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
        final StatementConfigurationOptions configOptions = StatementConfigurationOptions.builder()
                .entity(entity)
                .entityClass(entity.getClass())
                .build();
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = this.strategyResolver.resolve(QueryType.INSERT, connection, sql, configOptions)) {
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
     * @return collection of entities
     */
    @Override
    public <T> Collection<T> find(final Class<T> type, final List<Restriction> restrictions) {
        Objects.requireNonNull(type, "Parameter [type] must not be null!");
        Objects.requireNonNull(restrictions, "Parameter [restrictions] must not be null!");
        final String sql = QueryBuilder.buildQuery(type, QueryType.SELECT, restrictions);
        final StatementConfigurationOptions configOptions = StatementConfigurationOptions.builder()
                .entityClass(type)
                .restrictions(restrictions)
                .build();
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = this.strategyResolver.resolve(QueryType.SELECT, connection, sql, configOptions)) {
            final ResultSet resultSet = statement.executeQuery();
            final QueryResult queryResult = EntityUtils.getQueryResult(resultSet, type);
            final Collection<T> result = queryResult.isSingle() ? queryResult.wrap(queryResult.getSingleResult()) :
                    queryResult.isMulti() ? queryResult.getResultList() : Collections.emptyList();
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
        final StatementConfigurationOptions configOptions = StatementConfigurationOptions.builder()
                .entity(entity)
                .entityClass(entity.getClass())
                .build();
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = this.strategyResolver.resolve(QueryType.UPDATE, connection, sql, configOptions)) {
            final int rowsUpdated = statement.executeUpdate();
            log.debug("Updated {} rows for query '{}'", rowsUpdated, sql);
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
     */
    @Override
    public <T> void deleteById(final Class<T> type, final Object id) {
        Objects.requireNonNull(id, "Parameter [id] must not be null!");
        final String sql = QueryBuilder.buildQuery(type, QueryType.DELETE, Collections.emptyList());
        final StatementConfigurationOptions configOptions = StatementConfigurationOptions.builder()
                .entityClass(type)
                .id(id)
                .build();
        try (final Connection connection = this.dataSource.getConnection();
             final PreparedStatement statement = this.strategyResolver.resolve(QueryType.DELETE, connection, sql, configOptions)) {
            final int rowsUpdated = statement.executeUpdate();
            log.debug("Updated {} rows for query '{}'", rowsUpdated, sql);
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
