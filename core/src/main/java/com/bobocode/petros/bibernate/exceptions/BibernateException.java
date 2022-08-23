package com.bobocode.petros.bibernate.exceptions;

public class BibernateException extends RuntimeException {

    public BibernateException(String message) {
        super(message);
    }

    public BibernateException(String message, Throwable cause) {
        super(message, cause);
    }
}
