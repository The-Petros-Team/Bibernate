package com.bobocode.petros.bibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Represents many-to-one relationship.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ManyToOne {

    /**
     * Holds a column that represents a foreign key from the database perspective.
     *
     * @return foreign key name
     */
    String joinColumn();

}
