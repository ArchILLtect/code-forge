package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Outcome;

/**
 * Basic implementation of the AnswerEvaluator interface.
 * Compares submitted answers to expected answers with normalization.
 * @author Nick Hanson
 */
public class BasicEvaluatorService implements AnswerEvaluator {
    private static final int MAX_SOURCE_LENGTH = 10000; // simple guard for MVP

    /**
     * Evaluates a submitted answer for a given challenge.
     * @param challenge The challenge to evaluate against.
     * @param submission The submitted answer.
     * @return An AnswerEvaluation containing the evaluation results.
     */
    @Override
    public AnswerEvaluation evaluate(Challenge challenge, String submission) {
        String expected = challenge.getExpectedAnswer();
        if (expected == null || expected.isBlank()) {
            return new AnswerEvaluation(Outcome.INCORRECT,
                    "No expected answer configured for this challenge.", null, null);
        }
        if (submission == null || submission.isBlank()) {
            return new AnswerEvaluation(Outcome.SKIPPED, "No submission provided.", null, null);
        }
        if (submission.length() > MAX_SOURCE_LENGTH) {
            return new AnswerEvaluation(Outcome.SKIPPED, "Submission too long – timed out by guard.", null, null);
        }
        String nExp = Normalizer.basic(expected);
        String nSub = Normalizer.basic(submission);
        if (nExp.equals(nSub)) {
            return new AnswerEvaluation(Outcome.CORRECT, "Correct.", nExp, nSub);
        }
        String lpExp = Normalizer.stripPunctuation(nExp);
        String lpSub = Normalizer.stripPunctuation(nSub);
        if (lpExp.equals(lpSub)) {
            return new AnswerEvaluation(Outcome.ACCEPTABLE,
                    "Close — punctuation/spacing differs.", nExp, nSub);
        }
        return new AnswerEvaluation(Outcome.INCORRECT, "Keep trying.", nExp, nSub);
    }
}
