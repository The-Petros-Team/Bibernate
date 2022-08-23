package com.bobocode.petros.bibernate.exceptions;

public class MappingViolationException extends BibernateException {

    public MappingViolationException(String message) {
        super(message);
    }

    public MappingViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
