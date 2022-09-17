package com.bobocode.petros.bibernate.exceptions;

import com.bobocode.petros.bibernate.session.CacheableSession;

/**
 * Can be thrown if session is closed but user tries to perform any operation without opening
 * a new session.
 * See {@link CacheableSession}
 */
public class SessionIsClosedException extends BibernateException {

    public SessionIsClosedException(String message) {
        super(message);
    }

    public SessionIsClosedException(String message, Throwable cause) {
        super(message, cause);
    }
}
