package com.bobocode.petros.bibernate.utils;

import com.bobocode.petros.bibernate.annotations.Column;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.exceptions.NoEntityIdException;
import com.bobocode.petros.bibernate.exceptions.ReflectionOperationException;
import com.bobocode.petros.bibernate.session.query.QueryResult;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Utility class that provides some generic operations on entities.
 */
@UtilityClass
public class EntityUtils {

    public Field getIdField(final Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new NoEntityIdException(String.format("Entity class '%s' has no field marked with @Id", entityClass.getName())));
    }

    public String getColumnName(final Field field) {
        return Optional.ofNullable(field.getDeclaredAnnotation(Column.class))
                .map(Column::name)
                .orElseGet(field::getName);
    }

    public String getTableName(final Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getDeclaredAnnotation(Table.class))
                .map(t -> t.schema().isBlank() ? t.name() : t.schema() + "." + t.name())
                .orElseGet(() -> createTableNameFromClass(entityClass.getSimpleName()));
    }

    public String getMappedQueryValues(final List<?> elements) {
        return elements.stream().map(col -> "?").collect(Collectors.joining(","));
    }

    public <T> List<Field> sortEntityFields(final Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(Predicate.not(f -> f.isAnnotationPresent(Id.class)))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    public <T> List<Field> getEntityFields(final Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields()).collect(Collectors.toList());
    }

    public <T> String getColumnNames(final Class<T> entityClass) {
        return getEntityFields(entityClass).stream()
                .map(EntityUtils::getColumnName)
                .collect(Collectors.joining(","));
    }

    public List<String> getColumnNames(final List<Field> fields) {
        return fields.stream()
                .map(EntityUtils::getColumnName)
                .collect(Collectors.toList());
    }

    public String getInClauseValues(final Collection<Object> values) {
        return values.stream()
                .map(Object::toString)
                .collect(Collectors.joining(","));
    }

    public String getMappedQueryConditions(final List<Restriction> restrictions) {
        return restrictions.stream().map(Restriction::getExpression).collect(Collectors.joining(" "));
    }

    public <T> QueryResult getQueryResult(final ResultSet resultSet, final Class<T> entityClass) {
        final QueryResult queryResult = new QueryResult();
        final List<Object> results = queryResult.getResults();
        try {
            while (resultSet.next()) {
                final T entity = entityClass.getDeclaredConstructor().newInstance();
                final Field[] fields = entityClass.getDeclaredFields();
                for (final Field field : fields) {
                    final Object value = resultSet.getObject(getColumnName(field), field.getType());
                    if (value != null) {
                        setField(entity.getClass().getDeclaredField(field.getName()), entity, value);
                    }
                }
                results.add(entity);
            }
            return queryResult;
        } catch (SQLException e) {
            throw new JdbcOperationException(e.getMessage(), e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException | NoSuchFieldException e) {
            throw new ReflectionOperationException(e.getMessage(), e);
        }
    }

    public <T> void setField(final Field field, final T entity, final Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionOperationException(e.getMessage(), e);
        }
    }

    public String getQueryExpression(final String template,
                                     final String propertyName,
                                     final String sqlOperator,
                                     final Object value) {
        String remappedValue = null;
        if (value instanceof String
                || value instanceof Character
                || value instanceof UUID
                || isDate(value)) {
            remappedValue = "'" + value + "'";
        }
        return String.format(template, propertyName, sqlOperator, remappedValue == null ? value : remappedValue);
    }

    private boolean isDate(final Object value) {
        return (value instanceof java.util.Date)
                || (value instanceof LocalDate)
                || (value instanceof LocalTime)
                || (value instanceof LocalDateTime);
    }

    private String createTableNameFromClass(final String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
