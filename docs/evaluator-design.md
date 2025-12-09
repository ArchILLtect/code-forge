## Evaluator Design

#### I’m implementing a separate, dedicated evaluation service responsible solely for checking challenge answers. This keeps the logic isolated, reusable, and easy to expand later.

### BasicEvaluatorService:

- Implement an AnswerEvaluator interface so different evaluators can be added later without changing other code.
- Provide an evaluate(Challenge challenge, String submission) method.
- MVP behavior:
  - The evaluator works with answers as plain Strings. Crude/simple, I know, but quick as needed.
  - It does not compile or execute code (no sandbox).
  - Instead, it performs structured text checks.

- Evaluation flow:

  - Fetch the challenge’s expected answer(s).
  - Normalize both the expected and submitted answers (trim, lowercase, collapse whitespace, etc.).
  - Perform a strict match check.
  - Perform a looser match check (this can be improved post-MVP).
  - If neither matches, return incorrect.

- The result is returned as an AnswerEvaluation DTO, which contains:
    - The evaluation outcome (Correct, Acceptable, Incorrect)
  - A feedback message
  - Optionally, the normalized forms for debugging

- This evaluator is used by my existing ChallengeRunService, which:
    - Fetches the challenge via the DAO
  - Delegates evaluation to the evaluator service
  - Saves the submission result
  - Returns a RunResult object for the UI
