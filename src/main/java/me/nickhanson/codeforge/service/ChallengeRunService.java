package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
import me.nickhanson.codeforge.entity.Challenge;
import me.nickhanson.codeforge.persistence.ChallengeDao;
import me.nickhanson.codeforge.evaluator.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Service class responsible for running and evaluating code submissions
 * against coding challenges using predefined heuristics.
 * @author Nick Hanson
 */
public class ChallengeRunService {

    private static final Logger log = LogManager.getLogger(ChallengeRunService.class);
    private static final org.apache.logging.log4j.Logger telemetry = org.apache.logging.log4j.LogManager.getLogger("telemetry");

    private final ChallengeDao challengeDao = new ChallengeDao();
    private final AnswerEvaluator evaluator = new BasicEvaluatorService();

    /**
     * Evaluates submitted code for a given challenge in an unknown mode.
     *
     * @param challengeId The ID of the challenge being evaluated.
     * @param language    The programming language of the submitted code.
     * @param code        The submitted code as a string.
     * @return A RunResult object representing the outcome of the evaluation.
     */
    public RunResult run(Long challengeId, String language, String code) {
        return runWithMode("unknown", challengeId, language, code);
    }

    /**
     * Internal method to evaluate code with a specified mode for telemetry.
     *
     * @param mode        The mode of the run (e.g., "test", "submit").
     * @param challengeId The ID of the challenge being evaluated.
     * @param language    The programming language of the submitted code.
     * @param code        The submitted code as a string.
     * @return A RunResult object representing the outcome of the evaluation.
     */
    public RunResult runWithMode(String mode, Long challengeId, String language, String code) {
        long start = System.currentTimeMillis();
        log.info("Evaluating challengeId={} language={}", challengeId, language);

        // Language gate: MVP supports only Java; blank or unsupported language counts as SKIPPED
        if (language == null || language.isBlank() || !"java".equalsIgnoreCase(language)) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "Unsupported language: " + language);
            telemetry.info("mode={} challengeId={} outcome={} durationMs={} lang={}", mode, challengeId, rr.getOutcome(), (System.currentTimeMillis() - start), language);
            log.info("Run finished: outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }

        // Retrieve the challenge
        Challenge ch = challengeDao.getById(challengeId);
        if (ch == null) {
            RunResult rr = new RunResult(Outcome.INCORRECT, "Challenge not found.");
            telemetry.info("mode={} challengeId={} outcome={} durationMs={}", mode, challengeId, rr.getOutcome(), (System.currentTimeMillis() - start));
            log.info("Run finished: outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }

        // Evaluate the answer
        AnswerEvaluation ae = evaluator.evaluate(ch, code);
        RunResult rr = new RunResult(ae.getOutcome(), ae.getFeedback());
        telemetry.info("mode={} challengeId={} outcome={} durationMs={}", mode, challengeId, rr.getOutcome(), (System.currentTimeMillis() - start));
        log.info("Run finished: outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
        return rr;
    }
}
