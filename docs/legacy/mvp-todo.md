# Code Forge — MVP TODO (Prioritized)

This checklist captures the remaining work to reach MVP. Items are grouped and sorted by priority: P0 (must-have to ship), P1 (should-have for polish/stability), P2 (nice-to-have if time permits). Owners TBD.

Legend: 
- [ ] not started, [~] in progress, [x] done
- S/M/L = estimate (small/medium/large)

## P0 — Must-have

- [x] Practice Mode: Servlet + Routes (S)
  - [x] GET `/practice/{challengeId}` — render solve page with prompt and form.
  - [x] POST `/practice/{challengeId}/submit` — run via `ChallengeRunService`; show inline result.
  - [x] Public access (no auth) via `AuthGuardFilter` allowlist.
  - [x] Do NOT persist outcomes or modify `DrillItem` (verified via DB checks).

- [x] Practice Mode: JSP (S)
  - New `WEB-INF/jsp/practice/solve.jsp` with prompt, language select, textarea, submit button.
  - Inline outcome panel (pass/fail/error) and message text.

- [x] Feature Flags (S)
  - Add `features.practice.enabled` and `features.drill.enabled` in `application.properties`.
  - Servlets read flags; disable routes (404 or redirect) when off.

- [x] Drill Mode: Submission Flow Works End-to-End (S)
  - Confirm `POST /drill/{id}/submit` calls `ChallengeRunService.run` and `DrillService.recordOutcome`.
  - Redirect to `/drill/next` with a flash message summarizing outcome.

- [x] Drill Mode: Queue and Next Navigation (S)
  - `GET /drill` shows due queue (top N) via `drillService.getDueQueue(limit, userId)`.
  - `GET /drill/next` resolves next due or shows friendly empty state.

- [x] Error Handling & 404s (S)
  - Invalid `challengeId` returns 404 (Practice + Drill) — implemented.
  - Missing `language`/`code` shows friendly SKIPPED feedback (Practice inline; Drill flash then redirect).

- [x] Basic Tests (M)
  - Unit tests for `ChallengeRunService.run` (success/fail/error).
  - Servlet integration tests: Practice GET/POST renders and inline outcome; Drill submit redirects and persists.
  - Added unit test for drill auto-enrollment.
  - Added evaluator unit tests including timeout/size guard.

### Evaluator — High Priority (MVP Core)

- [x] Evaluator implementation (local runner, `expectedAnswer` compare) [High]
  - Scaffold added and wired into `ChallengeRunService`.
  - Timeout/size guard implemented.

- [x] PracticeServlet public flow using evaluator, no persistence [High]
  - Implemented; renders feedback inline; no DB writes (manually verified).

- [x] DrillServlet uses evaluator result to set Outcome + feedback [High]
  - Flash feedback wired: outcome + message shown on queue after redirect.

- [x] Unit + integration tests for evaluator flows [High]
  - Evaluator tests: correct/acceptable/incorrect/missing expected, timeout guard.
  - Drill auto-enrollment unit test.

## P1 — Should-have

- [x] Security Rules (S)
  - Practice is public; `AuthGuardFilter` allowlists `/practice` GET/POST.
  - Auth filter recognizes `userSub` and legacy `user`.
  - Drill routes require auth.

- [x] Flash feedback render in solve/queue (1–2 lines) [Medium]
  - Implemented on queue via session flash.

- [x] Drill JSP Polish (S)
  - `drill/queue.jsp`: friendly empty state, limit param handling, [x] enrollment banner.
  - [x] `drill/solve.jsp`: show drill stats (streak, nextDueAt) at header.
  - [x] `drill/solve.jsp`: flash message region polish and date formatting for nextDue.

- [x] Telemetry Logging (S)
  - Structured logs on submit: mode, challengeId, language (for gate), outcome, duration.
  - Rolling file appender at `logs/telemetry.log`.
  - [ ] Optional counters by mode (defer post-MVP).

- [x] DB Tests for Drill Persistence (M)
  - Verified `DrillService.recordOutcome` updates timesSeen and nextDueAt (handles null-to-set case).

- [x] Docs — MVP Guide (S)
  - Created `projects/mvp/README.md`: run Practice/Drill, flags, test notes.
  - [ ] Update `docs/project-plan.md` with final MVP acceptance (optional).

## P2 — Nice-to-have

- [x] Practice Landing Page (S)
  - GET `/practice` with a simple intro and CTA to challenges list.

- [ ] Session UX (S)
  - Persist last submitted code in session for the current challenge (Practice).

- [ ] Metrics Endpoint (M)
  - Optional `/metrics` or admin-only view to surface counters.

- [ ] Language Selector Enhancements (S)
  - Add available languages list; validate and default to `java`.

- [ ] Screenshots & Time Log (S)
  - Update `docs/screenshots/*` and `docs/reflections/TimeLog.md` for MVP.

## Acceptance Criteria Summary

Practice Mode
- User can open `/practice/{id}`, see prompt, submit code, and see result inline.
- No DB writes or drill state changes.
- Supports `java` language; invalid IDs → 404; errors → friendly message.
- Can disable via `features.practice.enabled`.

Drill Mode
- `/drill` shows due queue; `/drill/next` resolves correctly.
- `/drill/{id}` renders solve with drill stats.
- Submitting code updates `DrillItem` and redirects to next due with flash message.
- Can disable via `features.drill.enabled`.
