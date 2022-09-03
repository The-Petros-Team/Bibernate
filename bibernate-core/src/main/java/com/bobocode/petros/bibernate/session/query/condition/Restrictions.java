package com.bobocode.petros.bibernate.session.query.condition;

import com.bobocode.petros.bibernate.session.query.enums.SqlOperator;
import com.bobocode.petros.bibernate.utils.EntityUtils;

/**
 * Helper class that provides static methods for query usage.
 */
public final class Restrictions {

    private Restrictions() {
        throw new UnsupportedOperationException();
    }

    /**
     * Wraps primary key property and its value, constructs an expression based on them.
     *
     * @param propertyName property name
     * @param value        property value
     * @return instance of {@link Restriction}
     */
    public static Restriction idEq(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.EQ)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.EQ.getValue(), value))
                .build();
    }

    /**
     * Wraps a given property that should be compared via {@link SqlOperator#EQ}, constructs an expression based on them.
     *
     * @param propertyName property name
     * @param value        property value
     * @return instance of {@link Restriction}
     */
    public static Restriction eq(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.EQ)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.EQ.getValue(), value))
                .build();
    }

    /**
     * Wraps a given property for the future comparison using {@link SqlOperator#GT}, constructs an expression based on them.
     *
     * @param propertyName property name
     * @param value        property value
     * @return instance of {@link Restriction}
     */
    public static Restriction gt(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.GT)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.GT.getValue(), value))
                .build();
    }

    /**
     * Wraps a given property for the future comparison using {@link SqlOperator#GE}, constructs an expression based on them.
     *
     * @param propertyName property name
     * @param value        property value
     * @return instance of {@link Restriction}
     */
    public static Restriction ge(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.GE)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.GE.getValue(), value))
                .build();
    }

    /**
     * Wraps a given property for the future comparison using {@link SqlOperator#LT}, constructs an expression based on them.
     *
     * @param propertyName property name
     * @param value        property value
     * @return instance of {@link Restriction}
     */
    public static Restriction lt(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.LT)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.LT.getValue(), value))
                .build();
    }

    /**
     * Wraps a given property for the future comparison using {@link SqlOperator#LE}, constructs an expression based on them.
     *
     * @param propertyName property name
     * @param value        property value
     * @return instance of {@link Restriction}
     */
    public static Restriction le(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.LE)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.LE.getValue(), value))
                .build();
    }

    /**
     * Applies validation against property name and property value.
     *
     * @param propertyName property name
     * @param value        property value
     */
    private static void checkInputParams(final String propertyName, final Object value) {
        if (propertyName == null || propertyName.isBlank()) {
            throw new IllegalArgumentException("Property name must not be null or empty!");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null!");
        }
    }
}
