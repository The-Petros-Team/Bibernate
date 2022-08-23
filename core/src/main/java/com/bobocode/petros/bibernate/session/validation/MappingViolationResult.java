package com.bobocode.petros.bibernate.session.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MappingViolationResult {
    private String errorMessage;
    private String className;
}
