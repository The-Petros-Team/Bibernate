package com.bobocode.petros.bibernate.exceptions;

public class ReflectionOperationException extends BibernateException {

    public ReflectionOperationException(String message) {
        super(message);
    }

    public ReflectionOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
