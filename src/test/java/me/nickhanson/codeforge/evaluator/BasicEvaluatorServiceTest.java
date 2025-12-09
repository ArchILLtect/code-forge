package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.entity.Difficulty;
import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BasicEvaluatorServiceTest {

    private final BasicEvaluatorService evaluator = new BasicEvaluatorService();

    private Challenge challengeWithExpected(String expected) {
        Challenge ch = new Challenge("Test", Difficulty.EASY, "", "");
        ch.setExpectedAnswer(expected);
        return ch;
    }

    @Test
    void exactMatch_isCorrect() {
        Challenge ch = challengeWithExpected("pivotIndex");
        AnswerEvaluation eval = evaluator.evaluate(ch, "pivotIndex");
        assertEquals(Outcome.CORRECT, eval.getOutcome());
    }

    @Test
    void punctuationOrSpacing_only_isAcceptable() {
        Challenge ch = challengeWithExpected("pivot index");
        AnswerEvaluation eval = evaluator.evaluate(ch, "pivot; index");
        assertEquals(Outcome.ACCEPTABLE, eval.getOutcome());
    }

    @Test
    void mismatch_isIncorrect() {
        Challenge ch = challengeWithExpected("pivotIndex");
        AnswerEvaluation eval = evaluator.evaluate(ch, "somethingElse");
        assertEquals(Outcome.INCORRECT, eval.getOutcome());
    }

    @Test
    void missingExpected_marksIncorrect() {
        Challenge ch = challengeWithExpected("");
        AnswerEvaluation eval = evaluator.evaluate(ch, "anything");
        assertEquals(Outcome.INCORRECT, eval.getOutcome());
    }

    @Test
    void longSubmission_isSkippedByGuard() {
        Challenge ch = challengeWithExpected("pivotIndex");
        String longCode = "a".repeat(10001);
        AnswerEvaluation eval = evaluator.evaluate(ch, longCode);
        assertEquals(Outcome.SKIPPED, eval.getOutcome());
    }
}
