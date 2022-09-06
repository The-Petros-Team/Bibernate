package com.bobocode.petros.bibernate.exceptions;

public class ValidationException extends BibernateException {
    public ValidationException(final String message) {
        super(message);
    }

    public ValidationException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
