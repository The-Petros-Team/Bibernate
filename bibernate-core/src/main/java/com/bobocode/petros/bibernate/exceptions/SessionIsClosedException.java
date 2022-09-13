package com.bobocode.petros.bibernate.exceptions;

public class SessionIsClosedException extends BibernateException {
    public SessionIsClosedException(String message) {
        super(message);
    }

    public SessionIsClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
