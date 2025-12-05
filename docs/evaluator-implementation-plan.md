# Evaluator Implementation Plan (MVP)

Purpose: Implement a lightweight, pluggable evaluation service to check user submissions against expected answers, integrate with Practice and Drill flows, and add minimal tests and docs.

---

## Step-by-step plan

1) Model addition: expected answer(s)
- Add a simple field to `Challenge` for MVP: `expectedAnswer: String`.
- Surface it in create/edit forms (new.jsp, edit.jsp) with a basic text input.
- Persist via existing DAO/service methods.

2) Interface + DTOs
- Create `AnswerEvaluator` with a single method: `AnswerEvaluation evaluate(Challenge challenge, String submission)`.
- Create `AnswerEvaluation` DTO:
  - `Outcome outcome` (CORRECT, ACCEPTABLE, INCORRECT)
  - `String feedback`
  - (Optional) `String normalizedExpected`, `String normalizedSubmitted` for debugging
- Add a small `Normalizer` utility (static methods): trim, lowercase (Locale.ROOT), collapse whitespace.

3) Basic implementation (MVP)
- Implement `BasicEvaluatorService`:
  - Read `challenge.expectedAnswer`; return INCORRECT with feedback if missing.
  - Strict match: normalizedSubmitted == normalizedExpected.
  - Loose match (post-normalization): ignore punctuation (\p{Punct}), collapse whitespace; if equal → ACCEPTABLE.
  - Else → INCORRECT.
  - Feedback: short, friendly messages (e.g., "Correct", "Close — punctuation/spacing differs", "Keep trying").
- Stateless class; safe for reuse.

4) Wire into ChallengeRunService
- Inject/instantiate `BasicEvaluatorService`.
- Replace/augment current run path:
  - Fetch `Challenge` by id
  - Evaluate submission → `AnswerEvaluation`
  - Map `AnswerEvaluation.outcome` to existing `Outcome` and build `RunResult` including feedback
  - Drill mode: call `DrillService.recordOutcome(id, outcome, code)` and stash feedback for flash
  - Practice mode: do not persist; render feedback inline

5) PracticeServlet integration
- POST `/practice/{id}/submit`:
  - Call `ChallengeRunService.run(id, language, code)` (language used as pass-through for now)
  - Put `RunResult` outcome+feedback in request and forward to practice solve JSP

6) DrillServlet integration
- POST `/drill/{id}/submit`:
  - Call `ChallengeRunService.run(id, language, code)`
  - Persist via `DrillService.recordOutcome`
  - Set flash (success/info/error) using `RunResult.feedback`; `redirect /drill/next`

7) Views (JSP)
- `challenge` create/edit: add/retain the Expected Answer input (text)
- Practice solve JSP: show outcome pill and feedback block below the form
- Drill queue/solve JSP: confirm flash message shows feedback after redirect

8) Security & XSS hygiene
- Continue to escape user-supplied content using `<c:out>` (done for `promptMd`)
- Feedback strings are plain text; escape by default in JSP to avoid injection

9) Tests
- Unit:
  - `BasicEvaluatorServiceTest`: strict match, loose match (punctuation/whitespace), incorrect, missing expected
  - `NormalizerTest`: covers whitespace/punctuation handling
- Servlet/flow tests:
  - Practice POST: returns outcome+feedback; no persistence
  - Drill POST: persists outcome, sets redirect, exposes feedback in flash

10) Docs
- Update `README.md` (Evaluator section): brief design and usage
- Update `docs/evaluator-design.md` with any implementation notes (e.g., normalization rules)
- Add `docs/screenshots/` for practice/drill feedback examples (optional)

11) Deployment/Config (optional later)
- Feature flag `features.practice.enabled`/`features.drill.enabled` already planned; ensure evaluator path works regardless of flags

---

## Implementation checklist (MVP)

Model & Views
- [ ] Add `expectedAnswer` to `Challenge` entity and DAO mapping
- [ ] Show `expectedAnswer` in `new.jsp` and `edit.jsp`; persist via `ChallengesServlet`

Core evaluator
- [ ] Create `AnswerEvaluator` interface
- [ ] Create `AnswerEvaluation` DTO
- [ ] Implement `BasicEvaluatorService` (strict + loose + feedback)
- [ ] Add `Normalizer` utility (trim, lowercase, collapse whitespace, strip punctuation)

Service wiring
- [ ] Integrate evaluator into `ChallengeRunService` and return `RunResult` with feedback
- [ ] DrillServlet POST uses `recordOutcome` and sets flash feedback; redirect `/drill/next`
- [ ] PracticeServlet POST renders outcome+feedback inline without persistence

Views & UX
- [ ] Practice solve JSP shows outcome + feedback
- [ ] Drill queue/solve JSP shows flash feedback on redirect
- [ ] Escape feedback in JSP (use `<c:out>`) to avoid XSS

Tests
- [ ] Unit tests for `BasicEvaluatorService` and `Normalizer`
- [ ] Servlet tests: Practice POST and Drill POST

Docs
- [ ] Update `README.md` (Evaluator overview)
- [ ] Update `docs/evaluator-design.md` with final normalization rules
- [ ] (Optional) Add screenshots

Ready-to-ship
- [ ] Manual smoke: Practice submit → feedback; Drill submit → persisted + redirect + flash
- [ ] All tests green
- [ ] No stored XSS in details page (promptMd escaped)

