package com.bobocode.petros.bibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotation can be put on class property that represents a primary key.
 */
@Target({FIELD})
@Retention(RUNTIME)
public @interface Id {
}
