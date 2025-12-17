package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Challenge;

/**
 * Interface for evaluating submitted answers against challenges.
 * @author Nick Hanson
 */
public interface AnswerEvaluator {

    /**
     * Evaluates a submitted answer for a given challenge.
     * @param challenge The challenge to evaluate against.
     * @param submission The submitted answer.
     * @return An AnswerEvaluation containing the evaluation results.
     */
    AnswerEvaluation evaluate(Challenge challenge, String submission);
}

