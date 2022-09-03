package com.bobocode.petros.bibernate.session.statement.strategy.config;

import com.bobocode.petros.bibernate.session.query.condition.Restriction;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class StatementConfigurationOptions {
    private Object id;
    private Object entity;
    private Class<?> entityClass;
    private List<Restriction> restrictions;
}
