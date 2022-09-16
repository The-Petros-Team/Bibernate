package com.bobocode.petros.bibernate.exceptions;

/**
 * Can be thrown if any reflection-related error occurred.
 */
public class ReflectionOperationException extends BibernateException {

    public ReflectionOperationException(String message) {
        super(message);
    }

    public ReflectionOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
