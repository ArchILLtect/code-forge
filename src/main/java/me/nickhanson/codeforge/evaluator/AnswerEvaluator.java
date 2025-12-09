package me.nickhanson.codeforge.evaluator;

import me.nickhanson.codeforge.entity.Challenge;

public interface AnswerEvaluator {
    AnswerEvaluation evaluate(Challenge challenge, String submission);
}

