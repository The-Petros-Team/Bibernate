package com.bobocode.petros.bibernate.exceptions;

import com.bobocode.petros.bibernate.session.validation.EntityMappingValidator;

/**
 * Can be thrown if any violations found during the validation.
 * For more details see {@link EntityMappingValidator}
 */
public class MappingViolationException extends BibernateException {

    public MappingViolationException(String message) {
        super(message);
    }

    public MappingViolationException(String message, Throwable cause) {
        super(message, cause);
    }
}
