# MVP – Issue Seeds (Copy/Paste into New Issues)

Use these blocks to create GitHub issues with our templates. Each block includes: Title, Problem/context, Proposed solution, In scope / Out of scope, Acceptance criteria, Area, Dependencies/related issues, and Suggested labels. Priorities: P0 (must-have), P1 (should-have), P2 (nice-to-have).

---

## 53) Practice Mode (MVP): Non-persistent challenge submission and evaluation (P0)

- Title
  - feat(practice): Practice Mode — submit code for a challenge and see inline result (no persistence)
- Problem / context
  - We need a way for users to try challenges and get an evaluation without affecting Drill scheduling or writing to the DB.
  - Note: A Practice page exists as a stub for development; this issue focuses on wiring the run flow and result display while ensuring no persistence.
- Proposed solution
  - Add PracticeServlet with routes:
    - GET /practice/{challengeId} — render solve page with prompt, language select (java), code textarea
    - POST /practice/{challengeId}/submit — run via ChallengeRunService.run; render outcome inline
  - JSP: WEB-INF/jsp/practice/solve.jsp (or reuse drill/solve.jsp conditionally)
  - Do not call DrillService or create Submissions.
- In scope
  - Servlet + JSP
  - Outcome display (pass/fail/error) and friendly messaging
- Out of scope
  - Real code execution/sandbox; session persistence of code (optional later)
- Acceptance criteria
  - [ ] /practice/{id} renders solve page and allows submitting
  - [ ] Outcome displays inline; no DB writes occur
  - [ ] Invalid challenge → 404; runner errors → friendly message
- Area
  - area:web, feature:practice-mode
- Dependencies / related issue
  - Uses ChallengeService and ChallengeRunService
- Suggested labels
  - status:triage, enhancement, feature:practice-mode, area:web, priority:P0-critical, project:mvp

---

## 54) Feature Flags: Toggle Practice and Drill availability via config (P0)

- Title
  - feat(config): Feature flags for Practice and Drill routes
- Problem / context
  - Need a safe rollout and quick disable path for new routes.
- Proposed solution
  - Add features.practice.enabled and features.drill.enabled in application.properties
  - Check flags in servlets; disable routes via 404/redirect when off
- In scope
  - Config read and simple branching in controllers
- Out of scope
  - Advanced flag management/UI
- Acceptance criteria
  - [ ] Flags present and read at runtime
  - [ ] Disabling a flag hides corresponding routes
- Area
  - area:web
- Dependencies / related issue
  - Practice Mode and Drill Mode routes
- Suggested labels
  - status:triage, chore, area:web, priority:P0-critical, project:mvp

---

## 55) Drill Mode: Submission flow wiring and next navigation polish (P0)

- Title
  - feat(drill): Submission flow — run stub, persist outcome, redirect to next due
- Problem / context
  - DrillServlet + services exist, but we need to verify and polish the user-facing flow end-to-end.
- Proposed solution
  - Ensure POST /drill/{id}/submit → ChallengeRunService.run → DrillService.recordOutcome
  - Redirect to /drill/next; flash success/info/error message
  - Handle empty queue in /drill and /drill/next gracefully
- In scope
  - Controller wiring, JSP flash messages, empty-queue UX
- Out of scope
  - Real runner/sandbox
- Acceptance criteria
  - [ ] Submit persists and updates DrillItem; redirect to next due
  - [ ] /drill renders due queue; /drill/next resolves to an item or friendly empty state
- Area
  - area:web, area:service, feature:drill-mode
- Dependencies / related issue
  - Uses ChallengeRunService and DrillService
- Suggested labels
  - status:triage, enhancement, feature:drill-mode, area:web, area:service, priority:P0-critical, project:mvp

---

## 56) Error Handling: Friendly messages and 404s for invalid IDs/runner errors (P0)

- Title
  - chore(web): Error handling — 404 for invalid challenge, friendly messages for runner errors
- Problem / context
  - Users need clear feedback without leaking stack traces.
- Proposed solution
  - Standardize outcome mapping and error messages in Practice and Drill flows
  - Validate required params (language, code); handle missing values
- In scope
  - Servlet/JSP message blocks; basic validation
- Out of scope
  - Centralized error pages
- Acceptance criteria
  - [ ] Invalid IDs → 404 in both modes
  - [ ] Runner exceptions show friendly error panel; logs capture details
- Area
  - area:web
- Dependencies / related issue
  - Practice Mode, Drill Mode submit
- Suggested labels
  - status:triage, chore, area:web, priority:P0-critical, project:mvp

---

## 57) Security: Guard Drill routes; Practice is public (no auth) (P1)

- Title
  - feat(security): Guard Drill routes (require auth); Practice is public (no auth required)
- Problem / context
  - Drill should require login; Practice is intended to be public for MVP demos and try-outs.
- Proposed solution
  - Use existing AuthGuardFilter to protect GET /drill, GET /drill/*, POST /drill/*/submit
  - Explicitly allow anonymous access to Practice routes (GET/POST /practice/**)
- In scope
  - Filter rules and simple tests
- Out of scope
  - Spring Security migration
- Acceptance criteria
  - [ ] Unauth Drill requests redirect to /logIn
  - [ ] Practice routes accessible without login (GET/POST)
- Area
  - area:security, area:web, feature:cognito-auth
- Dependencies / related issue
  - Practice and Drill routes
- Suggested labels
  - status:triage, enhancement, area:security, priority:P1-high, project:mvp

---

## 58) Telemetry: Log counters for attempts and outcomes by mode (P1)

- Title
  - chore(logging): Structured logs and counters for submit events
- Problem / context
  - Need minimal visibility into usage and outcomes without full analytics.
- Proposed solution
  - Log structured entries on submit: mode, challengeId, language, outcome, duration
  - Optional in-memory counters per mode
- In scope
  - Logging statements, small counter utility
- Out of scope
  - Metrics endpoint/UI
- Acceptance criteria
  - [ ] Logs include fields above
  - [ ] Basic counters tracked (if implemented)
- Area
  - area:logging, area:web
- Dependencies / related issue
  - Practice and Drill submit handlers
- Suggested labels
  - status:triage, chore, area:logging, priority:P1-high, project:mvp

---

## 59) Tests: Unit and servlet integration for Practice and Drill flows (P1)

- Title
  - test(service,web): Unit and servlet integration tests — runner outcomes and submit flow
- Problem / context
  - Validate core behaviors and prevent regressions.
- Proposed solution
  - Unit tests for ChallengeRunService.run (success/fail/error)
  - Servlet integration tests for Practice GET/POST and Drill POST redirect
- In scope
  - Tests + basic fixtures
- Out of scope
  - Full E2E browser tests
- Acceptance criteria
  - [ ] Unit tests cover outcome mapping
  - [ ] Servlet tests pass for Practice/Drill flows
- Area
  - area:service, area:web, status:needs-tests
- Dependencies / related issue
  - Practice Mode, Drill flow
- Suggested labels
  - status:triage, status:needs-tests, area:service, area:web, priority:P1-high, project:mvp

---

## 60) DB Tests: DrillService.recordOutcome scheduling updates (P1)

- Title
  - test(service): DB-backed tests for Drill scheduling and persistence
- Problem / context
  - Verify scheduling rules and state updates for Drill.
- Proposed solution
  - Tests for CORRECT/ACCEPTABLE/INCORRECT/SKIPPED effects on streak/timesSeen/nextDueAt; queue ordering
- In scope
  - Service/DAO tests
- Out of scope
  - WebMvc
- Acceptance criteria
  - [ ] Assertions on scheduling rules and queue behavior
- Area
  - area:service, area:persistence, status:needs-tests
- Dependencies / related issue
  - Drill submission flow
- Suggested labels
  - status:triage, area:service, area:persistence, status:needs-tests, priority:P1-high, project:mvp

---

## 61) Docs: MVP guide and screenshots (P2)

- Title
  - docs: MVP guide — Practice & Drill usage, flags, and test notes
- Problem / context
  - Provide clarity for demo and reviewer.
- Proposed solution
  - Add projects/mvp/README.md and update docs; include screenshots for flows
- In scope
  - Docs and images
- Out of scope
  - Deep architecture write-ups
- Acceptance criteria
  - [ ] MVP docs added/updated; screenshots included
- Area
  - area:documentation
- Dependencies / related issue
  - Practice and Drill flows complete
- Suggested labels
  - status:triage, area:documentation, priority:P2-normal, project:mvp

---

## 62) Practice Landing Page (optional) (P2)

- Title
  - feat(practice): Landing page at /practice with CTA to challenges
- Problem / context
  - Simple entry point for Practice Mode.
- Proposed solution
  - GET /practice shows intro + links to challenges or accepts an ID
- In scope
  - Basic page + routing
- Out of scope
  - Advanced filtering/search
- Acceptance criteria
  - [ ] /practice accessible and helpful; links to challenge list
- Area
  - area:web, feature:practice-mode
- Dependencies / related issue
  - Practice servlet exists
- Suggested labels
  - status:triage, enhancement, feature:practice-mode, area:web, priority:P2-normal, project:mvp
