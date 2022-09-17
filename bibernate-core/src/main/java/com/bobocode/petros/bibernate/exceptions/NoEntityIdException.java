package com.bobocode.petros.bibernate.exceptions;

import com.bobocode.petros.bibernate.annotations.Id;

/**
 * Can be thrown if an entity doesn't have a field marked via {@link Id} annotation.
 */
public class NoEntityIdException extends BibernateException {

    public NoEntityIdException(String message) {
        super(message);
    }

    public NoEntityIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
