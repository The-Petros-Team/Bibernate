package com.bobocode.petros.bibernate.session.query.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum SqlOperator {

    EQ("="),
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    AND("AND"),
    OR("OR"),
    NOT("NOT"),
    IS_NULL("IS NULL"),
    IS_NOT_NULL("IS NOT NULL"),
    IN("IN"),
    WHERE("WHERE");

    @Getter
    private final String value;
}
