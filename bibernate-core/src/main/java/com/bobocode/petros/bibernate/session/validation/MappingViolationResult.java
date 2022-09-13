package com.bobocode.petros.bibernate.session.validation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * Simple wrapper that holds an error message if any and a class name that was the reason error message was raised
 * against.
 */
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@ToString
public class MappingViolationResult {
    private String errorMessage;
    private String className;
}
