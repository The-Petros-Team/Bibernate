package com.bobocode.petros.bibernate.exceptions;

import com.bobocode.petros.bibernate.session.jdbc.DefaultJdbcQueryManager;

/**
 * Can be thrown when an error occurred on database connectivity level.
 * See {@link DefaultJdbcQueryManager}
 */
public class JdbcOperationException extends BibernateException {

    public JdbcOperationException(String message) {
        super(message);
    }

    public JdbcOperationException(String message, Throwable cause) {
        super(message, cause);
    }
}
