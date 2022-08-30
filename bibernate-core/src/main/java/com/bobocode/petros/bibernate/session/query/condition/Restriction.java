package com.bobocode.petros.bibernate.session.query.condition;

import com.bobocode.petros.bibernate.session.query.enums.SqlOperator;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * Represents SQL restrictions, like 'AND', 'OR', '>=', '>' etc.
 */
@Getter
@Setter
@Builder
public class Restriction {
    private SqlOperator sqlOperator;
    private String propertyName;
    private Object value;
    private String expression;
}
