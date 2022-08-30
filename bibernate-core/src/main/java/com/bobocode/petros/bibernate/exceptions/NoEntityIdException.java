package com.bobocode.petros.bibernate.exceptions;

public class NoEntityIdException extends BibernateException {

    public NoEntityIdException(String message) {
        super(message);
    }

    public NoEntityIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
