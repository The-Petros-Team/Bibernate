package com.bobocode.petros.bibernate.session.statement.strategy.config;

import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * Helper class that keeps mandatory information for prepared statement configuration.
 */
@Getter
@Setter
@Builder
@ToString
public class StatementConfigurationOptions {
    /**
     * Entity identifier (primary key).
     */
    private Object id;

    /**
     * Instantiated entity.
     */
    private Object entity;

    /**
     * Class of an entity.
     */
    private Class<?> entityClass;

    /**
     * List of restrictions that can be applied to a query.
     */
    private List<Restriction> restrictions;
}
