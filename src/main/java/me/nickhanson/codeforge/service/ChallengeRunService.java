package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * Service class that provides a stubbed implementation for evaluating challenge submissions.
 * This service uses simple heuristics to determine the outcome of a code submission.
 * Note: This implementation does not compile or execute the code; it is purely for demonstration/testing purposes.
 */
@Service
public class ChallengeRunService {

    private static final Logger log = LoggerFactory.getLogger(ChallengeRunService.class);

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

        // Check for unsupported language or blank language
        if (!StringUtils.hasText(language) || !"java".equalsIgnoreCase(language)) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "Unsupported language: " + language);
            log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        // Check for empty or blank code
        if (!StringUtils.hasText(code)) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "No code provided – counted as skipped.");
            log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }

        String normalized = code.toLowerCase();
        // Check for skip indicators
        if (normalized.contains("skip")) {
            RunResult rr = new RunResult(Outcome.SKIPPED, "Stub runner: marked as SKIPPED.");
            log.info("Run finished: ok=true outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        // Check for failure indicators
        if (normalized.contains("fail") || normalized.contains("assert false")) {
            RunResult rr = new RunResult(Outcome.INCORRECT, "Stub runner: marked as INCORRECT.");
            log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        // Check for correct indicators
        if (normalized.contains("// correct") || normalized.contains("// pass")) {
            RunResult rr = new RunResult(Outcome.CORRECT, "Stub runner: marked as CORRECT.");
            log.info("Run finished: ok=true outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        // Check for acceptable indicators
        if (normalized.contains("// ok")) {
            RunResult rr = new RunResult(Outcome.ACCEPTABLE, "Stub runner: marked as ACCEPTABLE.");
            log.info("Run finished: ok=true outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
            return rr;
        }
        // Default case: incorrect
        RunResult rr = new RunResult(Outcome.INCORRECT, "Stub runner: marked as INCORRECT.");
        log.info("Run finished: ok=false outcome={} in {}ms", rr.getOutcome(), (System.currentTimeMillis() - start));
        return rr;
    }
}
