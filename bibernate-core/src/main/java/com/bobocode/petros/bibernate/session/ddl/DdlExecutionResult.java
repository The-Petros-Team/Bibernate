package com.bobocode.petros.bibernate.session.ddl;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Simple result wrapper that holds details of ddl script execution.
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
public class DdlExecutionResult {

    public static final String SUCCESS_MSG = "Successfully executed ddl script '%s'";

    /**
     * Number of successfully executed statements.
     */
    private long successes;

    /**
     * Number of failed statements.
     */
    private long failures;

    /**
     * Human-readable message that describes execution result.
     */
    private String message;

    /**
     * Increments successes by 1.
     */
    public void addSuccess() {
        this.successes++;
    }

    /**
     * Increments failures by 1.
     */
    public void addFailure() {
        this.failures++;
    }
}
