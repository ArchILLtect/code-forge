package me.nickhanson.codeforge.service;

import me.nickhanson.codeforge.entity.Outcome;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChallengeRunServiceTest {

    private final ChallengeRunService service = new ChallengeRunService();

    @Test
    void run_incorrect_when_java_and_no_markers() {
        RunResult res = service.run(1L, "java", "System.out.println(\"ok\");");
        assertThat(res.getOutcome()).isEqualTo(Outcome.INCORRECT);
        assertThat(res.getMessage()).contains("INCORRECT");
    }

    @Test
    void run_skipped_when_code_contains_skip() {
        RunResult res = service.run(2L, "java", "// SKIP this run");
        assertThat(res.getOutcome()).isEqualTo(Outcome.SKIPPED);
        assertThat(res.getMessage()).contains("SKIPPED");
    }

    @Test
    void run_incorrect_when_code_contains_fail_or_assert_false() {
        RunResult res1 = service.run(3L, "java", "// FAIL");
        assertThat(res1.getOutcome()).isEqualTo(Outcome.INCORRECT);
        assertThat(res1.getMessage()).contains("INCORRECT");

        RunResult res2 = service.run(3L, "java", "assert false; // should fail");
        assertThat(res2.getOutcome()).isEqualTo(Outcome.INCORRECT);
    }

    @Test
    void run_skipped_when_unsupported_language_or_blank_or_null() {
        assertThat(service.run(4L, "python", "print(1)").getOutcome()).isEqualTo(Outcome.SKIPPED);
        assertThat(service.run(4L, "", "code").getOutcome()).isEqualTo(Outcome.SKIPPED);
        assertThat(service.run(4L, null, "code").getOutcome()).isEqualTo(Outcome.SKIPPED);
    }

    @Test
    void run_incorrect_when_challengeId_is_null_and_java_code_has_no_markers() {
        RunResult res = service.run(null, "java", "code");
        assertThat(res.getOutcome()).isEqualTo(Outcome.INCORRECT);
        assertThat(res.getMessage()).contains("INCORRECT");
    }

    @Test
    void run_correct_when_code_contains_correct_or_pass() {
        RunResult res1 = service.run(5L, "java", "// correct implementation");
        assertThat(res1.getOutcome()).isEqualTo(Outcome.CORRECT);
        assertThat(res1.getMessage()).contains("CORRECT");

        RunResult res2 = service.run(5L, "java", "// pass the tests");
        assertThat(res2.getOutcome()).isEqualTo(Outcome.CORRECT);
    }

    @Test
    void run_acceptable_when_code_contains_ok() {
        RunResult res = service.run(6L, "java", "// ok to accept");
        assertThat(res.getOutcome()).isEqualTo(Outcome.ACCEPTABLE);
        assertThat(res.getMessage()).contains("ACCEPTABLE");
    }
}
