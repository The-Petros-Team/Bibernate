package com.bobocode.petros.bibernate.session.query.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Holds essential filtering criteria conditions and SQL keywords.
 */
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
    IN("IN"),
    WHERE("WHERE");

    @Getter
    private final String value;
}
