package me.nickhanson.codeforge.service;

import lombok.Getter;
import me.nickhanson.codeforge.entity.Outcome;

/**
 * Represents the result of a code run, including the outcome and an associated message.
 * This is a simple value object used to encapsulate the result of executing code in the stub runner.
 */
@Getter
public class RunResult {

    // The outcome of the code run, represented as an Outcome enum.
    private final Outcome outcome;
    // A message providing additional details about the code run result.
    private final String message;

    /**
     * Constructs a new RunResult with the specified outcome and message.
     *
     * @param outcome The outcome of the code run (e.g., SUCCESS, FAILURE).
     * @param message A descriptive message about the result of the code run.
     */
    public RunResult(Outcome outcome, String message) {
        this.outcome = outcome;
        this.message = message;
    }
}