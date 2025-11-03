



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

---

## 40) Post-MVP Hardening

- Title
    - feat(persistence): Enforce one DrillItem per Challenge; handle concurrency and race conditions

- Problem / context
    - Under concurrent load, duplicate DrillItem rows could be created for the same Challenge, and concurrent updates might overwrite streak/nextDueAt values (lost updates).

- Proposed solution
    - Uniqueness & race handling
        - Enforce one DrillItem per Challenge via a unique constraint (drill_items.challenge_id).
        - Update getOrCreateDrillItem to catch DataIntegrityViolationException and re-fetch the existing record.
    - Optimistic locking
        - Utilize the @Version field added in MVP.
    - Tests
        - Add DAO/service concurrency tests for create/update race scenarios.
    - Index
        - Add unique index on drill_items.challenge_id in Flyway/Liquibase migration (post-MVP; not in Week 8 scope).

- In scope
    - Service/DAO concurrency handling and tests.
    - Schema constraint via migration (post-MVP milestone).

- Out of scope
    - Refactoring unrelated domain entities.
    - Non-persistence optimizations.

- Acceptance criteria
    - [ ] Unique constraint prevents duplicate DrillItem per Challenge.
    - [ ] Concurrent updates use optimistic locking (@Version).
    - [ ] Concurrency tests confirm correctness and stability.
    - [ ] All existing tests remain green.

- Area
    - area:persistence, hardening, concurrency

- Dependencies / related issues
    - 38) DAO MVP Implementation (adds @Version)
    - Post-MVP migration plan (Flyway)

- Suggested labels
    - status:backlog, priority:P3-low, hardening, migration