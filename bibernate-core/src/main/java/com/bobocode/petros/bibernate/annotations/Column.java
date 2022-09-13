package com.bobocode.petros.bibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation should be put on entity class field to mark it as a persistable property that has a representation
 * in database.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Column {

    /**
     * Allows to specify a column name for a property.
     *
     * @return column name
     */
    String name();

}
