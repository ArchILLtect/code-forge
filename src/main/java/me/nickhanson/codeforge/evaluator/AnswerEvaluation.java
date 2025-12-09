package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Outcome;

public class AnswerEvaluation {
    private final Outcome outcome;
    private final String feedback;
    private final String normalizedExpected;
    private final String normalizedSubmitted;

    public AnswerEvaluation(Outcome outcome, String feedback, String normalizedExpected, String normalizedSubmitted) {
        this.outcome = outcome;
        this.feedback = feedback;
        this.normalizedExpected = normalizedExpected;
        this.normalizedSubmitted = normalizedSubmitted;
    }

    public Outcome getOutcome() { return outcome; }
    public String getFeedback() { return feedback; }
    public String getNormalizedExpected() { return normalizedExpected; }
    public String getNormalizedSubmitted() { return normalizedSubmitted; }
}

