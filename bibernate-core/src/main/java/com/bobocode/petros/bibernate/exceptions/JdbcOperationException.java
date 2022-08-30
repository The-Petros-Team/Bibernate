package com.bobocode.petros.bibernate.exceptions;

public class JdbcOperationException extends BibernateException {

    public JdbcOperationException(String message) {
        super(message);
    }

    public JdbcOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
