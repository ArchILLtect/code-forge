# Code Forge — MVP TODO (Prioritized)

This checklist captures the remaining work to reach MVP. Items are grouped and sorted by priority: P0 (must-have to ship), P1 (should-have for polish/stability), P2 (nice-to-have if time permits). Owners TBD.

Legend: 
- [ ] not started, [~] in progress, [x] done
- S/M/L = estimate (small/medium/large)

## P0 — Must-have

- [ ] Practice Mode: Servlet + Routes (S)
  - GET `/practice/{challengeId}` — render solve page with prompt and form.
  - POST `/practice/{challengeId}/submit` — run via `ChallengeRunService`; show inline result.
  - Do NOT persist outcomes or modify `DrillItem`.

- [ ] Practice Mode: JSP (S)
  - New `WEB-INF/jsp/practice/solve.jsp` (or conditional reuse of `drill/solve.jsp`).
  - Prompt, language select (start with `java`), textarea, submit button.
  - Inline outcome panel (pass/fail/error) and message text.

- [ ] Feature Flags (S)
  - Add `features.practice.enabled` and `features.drill.enabled` in `application.properties`.
  - Servlets read flags; disable routes (404 or redirect) when off.

- [ ] Drill Mode: Submission Flow Works End-to-End (S)
  - Confirm `POST /drill/{id}/submit` calls `ChallengeRunService.run` and `DrillService.recordOutcome`.
  - Redirect to `/drill/next` with a flash message summarizing outcome.

- [ ] Drill Mode: Queue and Next Navigation (S)
  - `GET /drill` shows due queue (top N) via `drillService.getDueQueue(limit)`.
  - `GET /drill/next` resolves next due or shows friendly empty state.

- [ ] Error Handling & 404s (S)
  - Invalid `challengeId` returns 404 (Practice + Drill).
  - Missing `language`/`code` or runner errors show friendly messages.

- [ ] Basic Tests (M)
  - Unit tests for `ChallengeRunService.run` (success/fail/error).
  - Servlet integration tests: Practice GET/POST renders and inline outcome; Drill submit redirects and persists.

## P1 — Should-have

- [ ] Security Rules (S)
  - Decide if Practice is public or requires auth; enforce via existing filter.
  - Ensure Drill routes require auth consistently.

- [ ] Drill JSP Polish (S)
  - `drill/queue.jsp`: friendly empty state, limit param handling.
  - `drill/solve.jsp`: show drill stats (streak, nextDueAt), flash message region.

- [ ] Telemetry Logging (S)
  - Structured logs on submit: mode, challengeId, language, outcome, duration.
  - Simple counters by mode for attempts and pass/fail.

- [ ] DB Tests for Drill Persistence (M)
  - Verify `DrillService.recordOutcome` updates streak/nextDueAt correctly.

- [ ] Docs — MVP Guide (S)
  - Add `projects/mvp/README.md`: how to run Practice/Drill, flags, test notes.
  - Update `docs/project-plan.md` or add `docs/mvp-overview.md` with scope and acceptance criteria.

## P2 — Nice-to-have

- [ ] Practice Landing Page (S)
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

## Suggested Timeline (10 days, part-time)
- Day 1–2: Feature flags; Practice servlet GET/POST; practice JSP with inline result.
- Day 3: Drill flow verification/polish; empty queue UX; error handling.
- Day 4: Telemetry logging hooks.
- Day 5–6: Unit + servlet integration tests; fix regressions.
- Day 7: DB tests for Drill; validate scheduling updates.
- Day 8: Optional Practice landing page; UX tweaks.
- Day 9: Docs, screenshots, time log; finalize security rules.
- Day 10: Smoke test; enable Practice flag; optionally enable Drill.

