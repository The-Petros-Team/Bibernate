package com.bobocode.petros.bibernate.exceptions;

/**
 * Basic framework-level exception.
 */
public class BibernateException extends RuntimeException {

    public BibernateException(String message) {
        super(message);
    }

    public BibernateException(String message, Throwable cause) {
        super(message, cause);
    }
}
