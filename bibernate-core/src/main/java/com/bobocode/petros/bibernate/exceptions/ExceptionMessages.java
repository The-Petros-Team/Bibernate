package com.bobocode.petros.bibernate.exceptions;

public enum ExceptionMessages {

    ;

    public static final String TABLES_METADATA_MESSAGE = "Couldn't fetch tables metadata. Detail: %s";
    public static final String COLUMNS_METADATA_MESSAGE = "Couldn't fetch columns metadata. Detail: %s";
    public static final String MAPPING_VALIDATION_ERROR_MSG = "Couldn't validate entity mapping. Detail %s ";
    public static final String MAPPING_VIOLATION_MESSAGE = "Found %d mapping violations. Details: %s";
    public static final String NULL_ENTITY_PERSISTENCE_CONTEXT_MSG= "%s must not be null!";
    public static final String TABLE_DOESNT_EXIST_MSG = "Table '%s' doesn't exist.";
    public static final String FIELD_DOESNT_EXIST_MSG = "Field '%s', doesn't exist in table '%s'";
}
