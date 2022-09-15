package com.bobocode.petros.bibernate.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * A class that is supposed to be mapped to relational representation must be marked via this annotation.
 */
@Target(TYPE)
@Retention(RUNTIME)
public @interface Entity {
}
