package com.bobocode.petros.bibernate.session.query.condition;

import com.bobocode.petros.bibernate.session.query.enums.SqlOperator;
import com.bobocode.petros.bibernate.utils.EntityUtils;

import java.util.Collection;

public final class Restrictions {

    private Restrictions() {
        throw new UnsupportedOperationException();
    }

    public static Restriction idEq(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.EQ)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.EQ.getValue(), value))
                .build();
    }

    public static Restriction eq(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.EQ)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.EQ.getValue(), value))
                .build();
    }

    public static Restriction gt(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.GT)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.GT.getValue(), value))
                .build();
    }

    public static Restriction ge(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.GE)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.GE.getValue(), value))
                .build();
    }

    public static Restriction lt(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.LT)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.LT.getValue(), value))
                .build();
    }

    public static Restriction le(final String propertyName, final Object value) {
        checkInputParams(propertyName, value);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(value)
                .sqlOperator(SqlOperator.LE)
                .expression(EntityUtils.getQueryExpression("%s %s %s", propertyName, SqlOperator.LE.getValue(), value))
                .build();
    }

    public static Restriction in(final String propertyName, final Collection<Object> values) {
        checkInputParams(propertyName, values);
        return Restriction.builder()
                .propertyName(propertyName)
                .value(values)
                .sqlOperator(SqlOperator.IN)
                .expression(EntityUtils.getQueryExpression("%s %s (%s)", propertyName, SqlOperator.IN.getValue(), EntityUtils.getInClauseValues(values)))
                .build();
    }

    private static void checkInputParams(final String propertyName, final Object value) {
        if (propertyName == null || propertyName.isBlank()) {
            throw new IllegalArgumentException("Property name must not be null or empty!");
        }
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null!");
        }
    }
}
