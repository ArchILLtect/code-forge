package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.evaluator.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Represents the result of a code run for a challenge.
 * Includes the outcome and an optional message.
 * Used by ChallengeRunService to return the result of code evaluation.
 * Only a placeholder for demonstration purposes.
 * @author Nick Hanson
 */
public class ChallengeRunService {

    private static final Logger log = LogManager.getLogger(ChallengeRunService.class);

    private final ChallengeDao challengeDao = new ChallengeDao();
    private final AnswerEvaluator evaluator = new BasicEvaluatorService();

    /**
     * Evaluates the submitted code for a specific challenge using predefined heuristics.
     * Heuristics (order matters):
     * - Unsupported or blank language -> SKIPPED
     * - Empty/blank code -> SKIPPED
     * - code contains "skip" -> SKIPPED
     * - code contains "fail" or "assert false" -> INCORRECT
     * - code contains "// correct" or "// pass" -> CORRECT
     * - code contains "// ok" -> ACCEPTABLE
     * - otherwise -> INCORRECT
     *
     * @param challengeId The ID of the challenge being evaluated.
     * @param language    The programming language of the submitted code.
     * @param code        The submitted code as a string.
     * @return A RunResult object representing the outcome of the evaluation.
     */
    public RunResult run(Long challengeId, String language, String code) {
        long start = System.currentTimeMillis();
        log.info("Evaluating challengeId={} language={}", challengeId, language);

        // Language gate: MVP supports only Java; blank or unsupported language counts as SKIPPED
        if (language == null || language.isBlank() || !"java".equalsIgnoreCase(language)) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "Unsupported language: " + language);
            log.info("Run finished: outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }

        Challenge ch = challengeDao.getById(challengeId);
        if (ch == null) {
            RunResult rr = new RunResult(Outcome.INCORRECT, "Challenge not found.");
            log.info("Run finished: outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        AnswerEvaluation ae = evaluator.evaluate(ch, code);
        RunResult rr = new RunResult(ae.getOutcome(), ae.getFeedback());
        log.info("Run finished: outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
        return rr;
    }
}
