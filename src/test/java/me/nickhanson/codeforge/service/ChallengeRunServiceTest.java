package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChallengeRunServiceTest {

    private final ChallengeRunService svc = new ChallengeRunService();

    @Test
    void run_skippedForUnsupportedLanguageOrBlank() {
        assertEquals(Outcome.SKIPPED, svc.run(1L, null, "code").getOutcome());
        assertEquals(Outcome.SKIPPED, svc.run(1L, "", "code").getOutcome());
        assertEquals(Outcome.SKIPPED, svc.run(1L, "python", "code").getOutcome());
    }

    @Test
    void run_heuristics_matchOrder() {
        assertEquals(Outcome.SKIPPED, svc.run(1L, "java", "// please SKIP this").getOutcome());
        assertEquals(Outcome.INCORRECT, svc.run(1L, "java", "// fail now").getOutcome());
        assertEquals(Outcome.CORRECT, svc.run(1L, "java", "// correct").getOutcome());
        assertEquals(Outcome.ACCEPTABLE, svc.run(1L, "java", "// ok").getOutcome());
        assertEquals(Outcome.INCORRECT, svc.run(1L, "java", "System.out.println();").getOutcome());
    }
}

