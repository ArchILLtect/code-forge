package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicEvaluatorServiceTest {

    private final BasicEvaluatorService evaluator = new BasicEvaluatorService();

    /**
     * Helper method to create a Challenge with a specified expected answer.
     */
    private Challenge challengeWithExpected(String expected) {
        Challenge ch = new Challenge("Test", Difficulty.EASY, "", "");
        ch.setExpectedAnswer(expected);
        return ch;
    }

    /**
     * Verifies that an exact match returns CORRECT outcome.
     */
    @Test
    void exactMatch_isCorrect() {
        Challenge ch = challengeWithExpected("pivotIndex");
        AnswerEvaluation eval = evaluator.evaluate(ch, "pivotIndex");
        assertEquals(Outcome.CORRECT, eval.getOutcome());
    }

    /**
     * Verifies that a match ignoring punctuation and spacing returns ACCEPTABLE outcome.
     */
    @Test
    void punctuationOrSpacing_only_isAcceptable() {
        Challenge ch = challengeWithExpected("pivot index");
        AnswerEvaluation eval = evaluator.evaluate(ch, "pivot; index");
        assertEquals(Outcome.ACCEPTABLE, eval.getOutcome());
    }

    /**
     * Verifies that a mismatched answer returns INCORRECT outcome.
     */
    @Test
    void mismatch_isIncorrect() {
        Challenge ch = challengeWithExpected("pivotIndex");
        AnswerEvaluation eval = evaluator.evaluate(ch, "somethingElse");
        assertEquals(Outcome.INCORRECT, eval.getOutcome());
    }

    /**
     * Verifies that an empty expected answer results in INCORRECT outcome.
     */
    @Test
    void missingExpected_marksIncorrect() {
        Challenge ch = challengeWithExpected("");
        AnswerEvaluation eval = evaluator.evaluate(ch, "anything");
        assertEquals(Outcome.INCORRECT, eval.getOutcome());
    }

    /**
     * Verifies that a submission exceeding the length limit is SKIPPED by the guard.
     */
    @Test
    void longSubmission_isSkippedByGuard() {
        Challenge ch = challengeWithExpected("pivotIndex");
        String longCode = "a".repeat(10001);
        AnswerEvaluation eval = evaluator.evaluate(ch, longCode);
        assertEquals(Outcome.SKIPPED, eval.getOutcome());
    }
}
