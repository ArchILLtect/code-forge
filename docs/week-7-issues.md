# Week 7 – Project Checkpoint 2 (Copy/Paste Issue Content)

Use this content with the "Project Checkpoint" issue template. Copy the Title into the title field, then paste the rest into the body/fields.

---

- Title
  - checkpoint: 2 – Database/DAO/Tests/Logging (Week 7)

- Checkpoint number
  - 2

- Due week/date
  - Week 7 (Oct 13–19, 2025)

- Scope per rubric / plan
  - Database designed and created
  - At least one DAO with full CRUD using Hibernate/JPA
  - DAO is fully unit tested
  - Log4J is implemented (no System.out.println)

- Checkpoint 2 – requirements (check these once verified)
  - [ ] Database designed and created (JPA config and tables present)
  - [ ] At least one DAO with full CRUD implemented using Hibernate/JPA
  - [ ] DAO unit/integration tests passing
  - [ ] Log4J implemented (no System.out.println)

- Evidence (links, screenshots)
  - H2 console screenshot showing CHALLENGE table and row count
  - Test summary (CI or local): `mvn -B -DskipTests=false test`
  - Surefire report(s):
    - `target/surefire-reports/TEST-me.nickhanson.codeforge.persistence.ChallengeDaoTest.xml`
  - Log4J config file reference: `src/main/resources/log4j2.properties`
  - Health endpoint screenshot: `GET /actuator/health` returns `UP`

- References
  - Plan: `docs/project-plan.md` (Week 7 – Checkpoint 2)
  - Week 7 plan: `docs/week-7-plan.md`
  - Issue seeds: `docs/week-6-issues.md`
  - README (logging + stack notes): `README.md`

- Demo plan (live)
  1) Start app: `mvn spring-boot:run`
  2) Show `/actuator/health` → status `UP`
  3) Open H2 console → run `SELECT COUNT(*) FROM CHALLENGE;`
  4) Run tests: `mvn -B -DskipTests=false test` → show green summary
  5) Show logging output on startup (Log4J console appender)

- Approval checklist
  - [ ] All checkpoint requirements checked above
  - [ ] Evidence attached (screenshots, links)
  - [ ] References added (plan, PRs/CI)
  - [ ] Ready for review

- Suggested labels (auto-applied by templates/automation in this repo)
  - `checkpoint`, `status:triage`, `project:mvp`, `priority:P1-high`, `area:persistence`, `area:logging`

---

## 27) Feature: Drill submission flow (UI + Controller + Service wiring)

- Title
  - feat(drill): Drill submission flow using stub runner; update DrillItem and redirect to next due
- Problem / context
  - DrillService and ChallengeRunService exist, but there is no user-facing Drill flow to submit code and advance the due queue.
- Proposed solution
  - Add DrillController with endpoints:
    - GET /drill — show queue summary (top N) with CTA
    - GET /drill/next — resolve next due and redirect to /drill/{id}, or show friendly empty state
    - GET /drill/{id} — solve page with prompt, language select (java), and code textarea
    - POST /drill/{id}/submit — run stub, map to Outcome, DrillService.recordOutcome, flash message, redirect to /drill/next
  - Views (JSP): drill/queue.jsp and drill/solve.jsp
  - Show flash outcome after submit; add brief tip explaining the stub runner behavior.
- In scope
  - Controller, JSPs, wiring to ChallengeRunService and DrillService, flash messages, empty-queue UX
- Out of scope
  - Real code execution or sandboxing, advanced editor features (tracked separately)
- Acceptance criteria
  - [ ] /drill shows a due summary with CTA to continue
  - [ ] /drill/next redirects to a due challenge or shows a friendly message if none
  - [ ] /drill/{id} renders solve JSP with prompt and form
  - [ ] POST /drill/{id}/submit persists Submission, updates DrillItem, redirects to next due, shows flash outcome
- Area
  - area:web, area:service
- Dependencies / related issue
  - Uses existing DrillService and ChallengeRunService
- Suggested labels
  - status:triage, enhancement, feature:drill-mode, priority:P1-high, area:web, area:service

---

## 28) Security: Guard Drill routes (AuthGuardFilter)

- Title
  - feat(security): Protect Drill routes via AuthGuardFilter (MVP)
- Problem / context
  - Drill routes must require login; unauthenticated users should be redirected to /logIn.
- Proposed solution
  - Extend AuthGuardFilter to require auth for:
    - GET /drill, GET /drill/*, POST /drill/*/submit
  - Keep home, challenge list/detail, and health public.
- In scope
  - Filter updates and tests
- Out of scope
  - Spring Security OIDC migration (tracked post-MVP)
- Acceptance criteria
  - [ ] Unauthenticated access to any Drill route redirects to /logIn
  - [ ] Authenticated users can access Drill pages and submit
- Area
  - area:security, area:web
- Dependencies / related issue
  - 27) Drill submission flow (routes must be protected)
- Suggested labels
  - status:triage, enhancement, area:security, priority:P1-high

---

## 29) Tests: DrillService scheduling and recordOutcome coverage

- Title
  - test(service): Coverage for Drill scheduling rules and recordOutcome
- Problem / context
  - Need tests to validate nextDueAt, streak, timesSeen rules and queue behavior.
- Proposed solution
  - Add unit tests for DrillService:
    - recordOutcome for CORRECT, ACCEPTABLE, INCORRECT, SKIPPED
    - getDueQueue ordering (due first, nulls first), and fallback to soonest when none due
- In scope
  - Unit/service tests only
- Out of scope
  - WebMvc; covered in a separate issue
- Acceptance criteria
  - [ ] Tests assert correct updates to streak/timesSeen/nextDueAt per outcome
  - [ ] Queue ordering rules covered; fallback path verified
- Area
  - area:service, status:needs-tests
- Dependencies / related issue
  - 27) Drill submission flow (uses these rules)
- Suggested labels
  - status:triage, area:service, status:needs-tests, priority:P2-normal

---

## 30) Tests: DrillController WebMvc tests

- Title
  - test(web): WebMvc coverage for DrillController routes and redirects
- Problem / context
  - Ensure the Drill flow pages render and redirects occur as expected, including unauthenticated redirects.
- Proposed solution
  - WebMvc tests:
    - GET /drill/next when due exists → redirect to /drill/{id}
    - GET /drill/{id} renders solve JSP (session user present)
    - POST /drill/{id}/submit uses stub outcome, sets flash, redirects to /drill/next
    - Unauthenticated attempts to Drill routes → redirected to /logIn by filter
- In scope
  - WebMvc tests with view resolver and session attrs
- Out of scope
  - Full integration tests
- Acceptance criteria
  - [ ] Tests cover the above scenarios and pass
- Area
  - area:web, status:needs-tests
- Dependencies / related issue
  - 27) Drill submission flow, 28) Security guard
- Suggested labels
  - status:triage, area:web, status:needs-tests, priority:P2-normal

---

## 31) Persistence hygiene: Review fetch/cascade for DrillItem and Submission

- Title
  - chore(persistence): Review lazy/eager and cascade settings for Drill entities
- Problem / context
  - Avoid orphan rows and unintended cascades as submissions are created and items deleted.
- Proposed solution
  - Audit mappings and adjust fetch/cascade where sensible; add 1–2 DAO tests if changes are made
- In scope
  - Mapping review and small tests (only if changes are made)
- Out of scope
  - Broad refactors or performance tuning
- Acceptance criteria
  - [ ] No unexpected orphans in basic delete paths; mappings remain sane
- Area
  - area:persistence
- Dependencies / related issue
  - Related to Drill submission flow
- Suggested labels
  - status:triage, area:persistence, priority:P3-low

---

## 32) Documentation: Drill mode docs and Week 7 updates

- Title
  - docs: Update README with Drill mode; add Week 7 journal/time log and screenshots
- Problem / context
  - Docs must reflect the new Drill feature and serve as demo evidence.
- Proposed solution
  - README: add Drill mode subsection and stub runner notes
  - Journal and TimeLog: add Week 7 entries
  - Screenshots: week7-drill-queue.png, week7-drill-submit.png in docs/screenshots/
- In scope
  - Docs and images
- Out of scope
  - Deep architecture write-ups
- Acceptance criteria
  - [ ] README updated; journal/time log entries added; screenshots present and referenced
- Area
  - area:documentation
- Dependencies / related issue
  - 27) Drill submission flow
- Suggested labels
  - status:triage, area:documentation, priority:P3-low

---

## 33) UX: Solve page enhancements (stats + shortcut)

- Title
  - feat(ux): Show streak/timesSeen and add Ctrl+Enter submit shortcut
- Problem / context
  - Improve usability and feedback during Drill sessions.
- Proposed solution
  - Display basic stats (current streak, timesSeen) on solve page; add keyboard shortcut to submit
- In scope
  - JSP tweaks and minimal JS
- Out of scope
  - Heavy UI rework
- Acceptance criteria
  - [ ] Stats visible on solve page; Ctrl+Enter submits form
- Area
  - area:web, area:ux
- Dependencies / related issue
  - 27) Drill submission flow
- Suggested labels
  - status:triage, enhancement, area:web, area:ux, priority:P3-low

---

## 34) UX: Code editor niceties

- Title
  - feat(ux): Monospace/resizable editor and localStorage persistence per challenge
- Problem / context
  - Improve code entry experience in the solve view.
- Proposed solution
  - Use monospace styling, make textarea resizable, and persist user code in localStorage keyed by challengeId
- In scope
  - Front-end only
- Out of scope
  - Third-party editor integrations
- Acceptance criteria
  - [ ] Monospace/resizable editor; code persists per challengeId on reload
- Area
  - area:web, area:ux
- Dependencies / related issue
  - 27) Drill submission flow
- Suggested labels
  - status:triage, enhancement, area:web, area:ux, priority:P3-low
