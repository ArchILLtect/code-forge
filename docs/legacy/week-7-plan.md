# Week 7 Plan — Drill Submission Flow, Tests, and UX Polish (Oct 13–19)

Source inputs: week-6-plan carry-overs, current services (`DrillService`, `ChallengeRunService`), and course requirements.

## Carry-overs from Week 6
- Drill submission UI/flow (wire to scheduling logic and stub runner)
- Tests: service (scheduling) + web (submit flow) coverage for drill
- (Hygiene) Data model fetch/cascade sanity for DrillItem/Submission (light)
- Docs/screenshots updates

## Goals (Done when)
- A user can enter Drill mode, view the next due challenge, submit code, and see the next due item after submission.
- Posting a solution persists a Submission and updates the related DrillItem (timesSeen, streak, nextDueAt) using existing rules.
- Client-side pagination remains on list; no server-side Pageable anywhere.
- Tests cover happy-path and a couple of edge outcomes; build is green locally and in CI.

## Scope

### Endpoints (proposed)
- GET `/drill` — Show the due queue (top N, e.g., 10) with a CTA to "Start"/"Continue`".
- GET `/drill/next` — Resolve the next due item and redirect to `/drill/{challengeId}` (or show a friendly empty state).
- GET `/drill/{challengeId}` — Render a solve page with challenge prompt, and a submit form (language + code).
- POST `/drill/{challengeId}/submit` — Run stub (`ChallengeRunService.run`) → map to Outcome → `DrillService.recordOutcome` → redirect to `/drill/next` with a flash message summarizing the outcome.

Notes
- Use existing services:
  - `ChallengeRunService.run(challengeId, language, code)` → returns `RunResult` with `Outcome`.
  - `DrillService.recordOutcome(challengeId, outcome, code)` → persists `Submission` and updates `DrillItem` scheduling.
  - `DrillService.getDueQueue(limit)` and `DrillService.nextDue()` for queue/next behavior.

### Views (JSP)
- `drill/queue.jsp` — shows the due items (id, title, difficulty, nextDueAt), CTA to `/drill/next`.
- `drill/solve.jsp` — shows title, difficulty, promptMd, a textarea for code input, select for language (`java` stub only), and submit button.
- Flash messages for outcome (success/info) on redirect.

### Controller
- `DrillController` with mappings for the above endpoints.
- Minimal model attributes: challenge details, outcome flash, small tip text for runner stub behavior.

### Security
- Protect Drill routes via the existing `AuthGuardFilter` (MVP approach):
  - Require auth for `GET /drill`, `GET /drill/*`, and `POST /drill/*/submit`.
  - Public remains: home, challenge list/detail, health.
- Optional improvement (later): migrate to Spring Security OIDC and security DSL rules.

### Tests
- Service tests (DrillService)
  - `recordOutcome`: verify streak/timesSeen/nextDueAt per Outcome (CORRECT/ACCEPTABLE/INCORRECT/SKIPPED).
  - `getDueQueue`: due-first ordering, fallback to the soonest if none due.
- WebMvc tests (DrillController)
  - GET `/drill/next` when due exists → redirect to `/drill/{id}`.
  - GET `/drill/{id}` renders solve JSP (authenticated session).
  - POST `/drill/{id}/submit` → uses stub run → persists outcome → redirect to `/drill/next` with flash.
  - Unauthenticated access to Drill routes → redirect to `/logIn` (filter applies).

### Data/DAO hygiene (light)
- Quick audit of fetch/cascade for `Submission` and `DrillItem` to avoid orphan rows.
- Add 1–2 DAO tests only if mapping changes are made.

### Documentation
- README: add a brief "Drill mode" subsection with the flow, runner stub notes, and screenshots.
- Week 7 plan + journal update; time log entries.
- Screenshots: `docs/screenshots/week7-drill-queue.png`, `docs/screenshots/week7-drill-submit.png`.

## Acceptance Criteria
- [x] Navigating to `/drill/next` sends the user to the next due item (or friendly message if none).
- [x] Posting to `/drill/{id}/submit` saves a `Submission` and updates that challenge's `DrillItem` according to rules.
- [x] After submit, user lands on the next due item (or friendly message if queue is empty), with a flash showing the outcome.
- [x] Filter protects Drill routes: unauthenticated users are redirected to `/logIn`.
- [x] Unit tests cover at least: one happy-path submission (CORRECT) and one edge (SKIPPED/INCORRECT); plus a couple of DrillService scheduling checks.
- [x] CI is green and WAR artifact still uploads.

## Nice-to-haves (if time allows)
- Show basic Drill stats on the solve page (current streak, timesSeen).
- Add keyboard shortcuts (Ctrl+Enter to submit) on the solve page.
- Basic client-side code editor enhancements (monospace, resizing, persist code in localStorage per challenge).

## Risks / Mitigations
- Runner expectations: make it clear the runner is a stub (no real compilation). Add a help text in the solve page.
- Empty queue UX: always offer the soonest upcoming item if none are due now.
- Auth flow: rely on `AuthGuardFilter` for Week 7; defer Spring Security OIDC to a later milestone.

## Suggested Task Breakdown
1) Controller + routes scaffolding for Drill (GET `/drill`, `/drill/next`, `/drill/{id}`, POST `/drill/{id}/submit`).
2) JSPs: queue.jsp and solve.jsp; add flash message region and UI for language + code.
3) Wire to `ChallengeRunService` + `DrillService` and flash message on redirect.
4) Extend `AuthGuardFilter` to guard Drill routes.
5) Tests: WebMvc for DrillController; a couple of added service tests for scheduling rules.
6) Docs + screenshots + time log entries.

## Evidence
- Screenshots: `docs/screenshots/week7-drill-queue.png`, `docs/screenshots/week7-drill-submit.png`.
- Test report: WebMvc + service tests for drill submission.
- Actions run: green build with WAR artifact.

---

Status: Completed for Issues 27 & 28 — routes protected via AuthGuardFilter; Drill submission flow functional; unit tests added and green locally.

Additional status: Issue 38 (DAO hardening for MVP) — Transactions moved to service/DAO layers; removed controller-level `@Transactional` on GETs; added `@Version Long version` to `DrillItem`; added a version-increment test. Full suite green.
