package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeRunServiceTest {

    private final ChallengeRunService svc = new ChallengeRunService();

    /**
     * Verifies that running with unsupported language or blank language results in SKIPPED outcome.
     */
    @Test
    void run_skippedForUnsupportedLanguageOrBlank() {
        assertEquals(Outcome.SKIPPED, svc.run(1L, null, "code").getOutcome());
        assertEquals(Outcome.SKIPPED, svc.run(1L, "", "code").getOutcome());
        assertEquals(Outcome.SKIPPED, svc.run(1L, "python", "code").getOutcome());
    }
}
