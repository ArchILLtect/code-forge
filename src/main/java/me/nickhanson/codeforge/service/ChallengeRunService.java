package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
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
        log.info("Simulating run for challengeId={} language={}", challengeId, language);

        if (language == null || language.isBlank() || !"java".equalsIgnoreCase(language)) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "Unsupported language: " + language);
            log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        if (code == null || code.isBlank()) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "No code provided – counted as skipped.");
            log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }

        String normalized = code.toLowerCase();
        if (normalized.contains("skip")) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "Stub runner: marked as SKIPPED.");
            log.info("Run finished: ok=true outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        if (normalized.contains("fail") || normalized.contains("assert false")) {
            RunResult rr = new RunResult(Outcome.INCORRECT, "Stub runner: marked as INCORRECT.");
            log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        if (normalized.contains("// correct") || normalized.contains("// pass")) {
            RunResult rr = new RunResult(Outcome.CORRECT, "Stub runner: marked as CORRECT.");
            log.info("Run finished: ok=true outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        if (normalized.contains("// ok")) {
            RunResult rr = new RunResult(Outcome.ACCEPTABLE, "Stub runner: marked as ACCEPTABLE.");
            log.info("Run finished: ok=true outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        RunResult rr = new RunResult(Outcome.INCORRECT, "Stub runner: marked as INCORRECT.");
        log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
        return rr;
    }
}
