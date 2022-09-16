package com.bobocode.petros.bibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Represents many-to-many relationship.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface ManyToMany {

    /**
     * Mapping marker that reflects a field name on a child side that maps a relation to parent side.
     *
     * @return name of the field that reflects mapping
     */
    String mappedBy() default "";

}
