package com.bobocode.petros.bibernate.utils;

import com.bobocode.petros.bibernate.annotations.Column;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import com.bobocode.petros.bibernate.exceptions.JdbcOperationException;
import com.bobocode.petros.bibernate.exceptions.NoEntityIdException;
import com.bobocode.petros.bibernate.exceptions.ReflectionOperationException;
import com.bobocode.petros.bibernate.session.EntityKey;
import com.bobocode.petros.bibernate.session.query.QueryResult;
import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
@UtilityClass
public class EntityUtils {

    /**
     * Extracts field that is specified as primary key (id) for the specified entity class.
     *
     * @param entityClass entity class
     * @return primary key field
     */
    public Field getIdField(final Class<?> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(Id.class))
                .findAny()
                .orElseThrow(() -> new NoEntityIdException(String.format("Entity class '%s' has no field marked with @Id", entityClass.getName())));
    }

    /**
     * Extracts value that is specified as primary key (id) for the specified entity.
     *
     * @param entity object
     * @return primary key value
     */
    public Object getIdValue(final Object entity) {
        try {
            Field id = getIdField(entity.getClass());
            id.setAccessible(true);
            return id.get(entity);
        } catch (IllegalAccessException e) {
            throw new ReflectionOperationException(e.getMessage(), e);
        }
    }

    /**
     * Create {@link EntityKey} from accepted entity
     *
     * @param entity
     * @return entity key
     */
    public EntityKey<?> createEntityKey(Object entity) {
        return new EntityKey<>(entity.getClass(), getIdValue(entity));
    }

    /**
     * Extracts column name for a specified field.
     *
     * @param field field that potentially has annotation {@link Column} put on it
     * @return column name
     */
    public String getColumnName(final Field field) {
        return Optional.ofNullable(field.getDeclaredAnnotation(Column.class))
                .map(Column::name)
                .orElseGet(field::getName);
    }

    /**
     * Extracts table name in a following format: schema.table_name.
     *
     * @param entityClass entity class
     * @return table name
     */
    public String getTableName(final Class<?> entityClass) {
        return Optional.ofNullable(entityClass.getDeclaredAnnotation(Table.class))
                .map(t -> t.schema().isBlank() ? t.name() : t.schema() + "." + t.name())
                .orElseGet(() -> createTableNameFromClass(entityClass.getSimpleName()));
    }

    /**
     * Transforms given elements to the char sequence of the following format: ?,?,?,? etc.
     *
     * @param elements elements
     * @return mapped query args
     */
    public String getQueryArguments(final List<?> elements) {
        return elements.stream().map(col -> "?").collect(Collectors.joining(","));
    }

    /**
     * Returns sorted list of entity fields with id (primary key) field excluded.
     *
     * @param entityClass entity class
     * @param <T>         generic type
     * @return list of sorted fields
     */
    public <T> List<Field> sortEntityFieldsSkipPK(final Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields())
                .filter(Predicate.not(f -> f.isAnnotationPresent(Id.class)))
                .sorted(Comparator.comparing(Field::getName))
                .collect(Collectors.toList());
    }

    /**
     * Returns fields for a given entity.
     *
     * @param entityClass entity class
     * @param <T>         generic type
     * @return list of entity fields
     */
    public <T> List<Field> getEntityFields(final Class<T> entityClass) {
        return Arrays.stream(entityClass.getDeclaredFields()).collect(Collectors.toList());
    }

    /**
     * Collects column names for the given entity class as a string that has the following format:
     * id,first_name,last_name etc.
     *
     * @param entityClass entity class
     * @param <T>         generic type
     * @return column names joined as a single string
     */
    public <T> String getColumnNames(final Class<T> entityClass) {
        return getEntityFields(entityClass).stream()
                .map(EntityUtils::getColumnName)
                .collect(Collectors.joining(","));
    }

    /**
     * Returns column names mapped to a list.
     *
     * @param fields entity fields
     * @return list of column names
     */
    public List<String> getColumnNames(final List<Field> fields) {
        return fields.stream()
                .map(EntityUtils::getColumnName)
                .collect(Collectors.toList());
    }

    /**
     * Maps expressions from the given list of restrictions to a single string.
     *
     * @param restrictions restrictions
     * @return mapped query conditions
     */
    public String getMappedQueryConditions(final List<Restriction> restrictions) {
        return restrictions.stream().map(Restriction::getExpression).collect(Collectors.joining(" "));
    }

    /**
     * Wraps result received from {@link ResultSet} to {@link QueryResult}.
     *
     * @param resultSet   result set
     * @param entityClass entity class
     * @param <T>         generic type
     * @return query result
     */
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

    /**
     * Sets the value to field for a given entity.
     *
     * @param field  field
     * @param entity entity
     * @param value  value
     * @param <T>    generic type
     */
    public <T> void setField(final Field field, final T entity, final Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionOperationException(e.getMessage(), e);
        }
    }

    /**
     * Constructs an expression for a given template.
     *
     * @param template     template
     * @param propertyName property name
     * @param sqlOperator  sql operator
     * @param value        value
     * @return expression that is ready to use
     */
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

    /**
     * Checks whether a given object is an instance of one of the given classes.
     *
     * @param value value to check
     * @return true if value is instance of the predefined classes
     */
    private boolean isDate(final Object value) {
        return (value instanceof java.util.Date)
                || (value instanceof LocalDate)
                || (value instanceof LocalTime)
                || (value instanceof LocalDateTime);
    }

    /**
     * Maps column names to a following format: field_name_1 = ?, field_name_2 = ?
     *
     * @param entityClass    entity class
     * @param primaryKeyOnly restricts mapping to a single field - id (primary key)
     * @param <T>            generic type
     * @return string of the format above
     */
    public <T> String getMappedQueryColumns(final Class<T> entityClass, final boolean primaryKeyOnly) {
        if (primaryKeyOnly) {
            return EntityUtils.getColumnName(EntityUtils.getIdField(entityClass)) + " = ?";
        } else {
            return Arrays.stream(entityClass.getDeclaredFields())
                    .filter(f -> !f.isAnnotationPresent(Id.class))
                    .map(EntityUtils::getColumnName)
                    .sorted()
                    .map(column -> column + " = ?")
                    .collect(Collectors.joining(", "));
        }
    }

    /**
     * Helps to resolve a given property for a given entity class. If property name is equal to column name, like
     * 'first_name' it should be resolved correctly as well as the case, then property name equals to entity field
     * name, like 'firstName', so this case is also handled.
     * It means, that it is possible to pass a property name in both given forms and achieve the same result.
     *
     * @param entityClass entity class
     * @param propertyName property name
     * @return property name reflected in database
     * @param <T> generic type
     */
    public <T> String resolveEntityColumnByPropertyName(final Class<T> entityClass, final String propertyName) {
        String result;
        try {
            final Field field = entityClass.getDeclaredField(propertyName);
            result = EntityUtils.getColumnName(field);
        } catch (NoSuchFieldException e) {
            log.warn("Class '{}' doesn't have a field named '{}'. Analyzing class mappings...", entityClass.getName(), propertyName);
            result = EntityUtils.getColumnNames(EntityUtils.getEntityFields(entityClass))
                    .stream()
                    .filter(column -> column.equals(propertyName))
                    .findAny()
                    .orElseThrow(() -> new ReflectionOperationException(String.format("Can't resolve property '%s' in class '%s'", propertyName, entityClass.getName())));
            log.info("Resolved a field '{}' in class '{}'", result, entityClass.getName());
        }
        return result;
    }

    /**
     * Creates table name from a given class name.
     *
     * @param className class name
     * @return formatted table name
     */
    private String createTableNameFromClass(final String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
