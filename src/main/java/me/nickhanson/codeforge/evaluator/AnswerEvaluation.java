package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Outcome;

/**
 * Represents the evaluation result of a submitted answer.
 * Contains the outcome, feedback, and normalized forms of expected and submitted answers.
 * @author Nick Hanson
 */
public class AnswerEvaluation {
    private final Outcome outcome;
    private final String feedback;
    private final String normalizedExpected;
    private final String normalizedSubmitted;

    /**
     * Constructs an AnswerEvaluation with the specified details.
     * @param outcome The outcome of the evaluation.
     * @param feedback Feedback regarding the evaluation.
     * @param normalizedExpected The normalized expected answer.
     * @param normalizedSubmitted The normalized submitted answer.
     */
    public AnswerEvaluation(Outcome outcome, String feedback, String normalizedExpected, String normalizedSubmitted) {
        this.outcome = outcome;
        this.feedback = feedback;
        this.normalizedExpected = normalizedExpected;
        this.normalizedSubmitted = normalizedSubmitted;
    }

    /**
     * Gets the outcome of the evaluation.
     * @return The outcome.
     */
    public Outcome getOutcome() { return outcome; }
    public String getFeedback() { return feedback; }
    public String getNormalizedExpected() { return normalizedExpected; }
    public String getNormalizedSubmitted() { return normalizedSubmitted; }
}

