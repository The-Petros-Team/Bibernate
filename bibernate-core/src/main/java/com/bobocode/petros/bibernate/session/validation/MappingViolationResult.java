package com.bobocode.petros.bibernate.session.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class MappingViolationResult {
    private String errorMessage;
    private String className;
}
