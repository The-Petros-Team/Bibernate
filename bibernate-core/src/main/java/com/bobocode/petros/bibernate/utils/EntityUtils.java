package com.bobocode.petros.bibernate.utils;

import com.bobocode.petros.bibernate.annotations.Column;
import com.bobocode.petros.bibernate.annotations.Id;
import com.bobocode.petros.bibernate.annotations.Table;
import com.bobocode.petros.bibernate.exceptions.NoEntityIdException;
import com.bobocode.petros.bibernate.exceptions.ReflectionOperationException;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
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

    public <T> void setField(final Field field, final T entity, final Object value) {
        try {
            field.setAccessible(true);
            field.set(entity, value);
        } catch (IllegalAccessException e) {
            throw new ReflectionOperationException(e.getMessage(), e);
        }
    }

    private String createTableNameFromClass(final String className) {
        return className.substring(0, 1).toLowerCase() + className.substring(1);
    }
}
