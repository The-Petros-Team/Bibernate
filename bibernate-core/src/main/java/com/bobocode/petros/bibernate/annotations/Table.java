package com.bobocode.petros.bibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation should be put on a class level to indicate that this class references a table in relation database and
 * has appropriate mappings inside the class.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Table {

    /**
     * Allows to set a table name for a given entity.
     *
     * @return table name
     */
    String name();

    /**
     * Allows to set database schema name.
     *
     * @return schema name
     */
    String schema() default "";

}
