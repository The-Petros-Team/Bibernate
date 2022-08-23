package com.bobocode.petros.bibernate.session.validation;

import lombok.experimental.UtilityClass;

import javax.sql.DataSource;
import java.util.Set;

@UtilityClass
public class EntityMappingValidator {

    public Set<MappingViolationResult> validate(DataSource dataSource, Set<String> entityPackages) {
        return null;
    }
}
