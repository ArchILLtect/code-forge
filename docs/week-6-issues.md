# Week 6 – Issue Seeds (Copy/Paste into New Issues)

Use these blocks to create GitHub issues with our templates. Each includes: Problem / context, Proposed solution, In scope / Out of scope, Acceptance criteria, Area, Dependencies / related issue, and Suggested labels.

---

## 1) Feature: Cognito OAuth2/OIDC Login (Spring Security)

- Title
  - feat(security): Cognito OAuth2/OIDC login via Spring Security
- Problem / context
  - We need user authentication via AWS Cognito to meet the Week 6 auth goals and protect routes. No login flow exists yet.

- Proposed solution
  - Configure Spring Security OAuth2 client for Cognito.
  - Add OIDC login endpoints (/login, /logout) and a minimal success/failure handler.
  - Read secrets via environment variables (see README env section).

- In scope
  - Spring Security config, client registration/provider properties, basic login/logout.
  - Local test using Cognito-hosted UI; verify ID token and principal mapping.

- Out of scope
  - Custom user provisioning, roles DB, or advanced claims mapping (later weeks if needed).

- Acceptance criteria
  - [ ] Visiting a protected route redirects to Cognito login and returns to the app on success
  - [ ] Authenticated principal available in controllers
  - [ ] No secrets committed; properties read from env

- Area
  - area:security

- Dependencies / related issue
  - Cognito user pool, app client, and domain configured in AWS

- Suggested labels
  - status:triage, enhancement, feature:cognito-auth, area:security, priority:P1-high

---

## 2) Feature: Protect Routes and Challenge Data (Authorization)

- Title
  - feat(security): Protect routes and challenge data (authorization)
- Problem / context
  - After login, certain routes must be protected (challenge CRUD, drill pages tied to user identity). Public pages should remain open.

- Proposed solution
  - Spring Security configuration with antMatchers/HttpSecurity DSL to secure routes.
  - Add a simple role/authority check (e.g., ADMIN for admin pages) using OIDC claims mapping or a default mapping.

- In scope
  - PermitAll for public pages (home, challenge list/detail), authenticated for drill/submission, ADMIN for admin CRUD.

- Out of scope
  - Fine-grained ACLs and DB-backed roles.

- Acceptance criteria
  - [ ] Public pages accessible without login
  - [ ] Protected routes require login and redirect to /login when unauthenticated
  - [ ] Admin routes require elevated authority; access denied otherwise

- Area
  - area:security

- Dependencies / related issue
  - Depends on: Feature: Cognito OAuth2/OIDC Login

- Suggested labels
  - status:triage, enhancement, feature:cognito-auth, area:security, priority:P1-high

---

## 3) Feature: jQuery-based Pagination via CDN (Client-side)

- Title
  - feat(web): Client-side pagination via jQuery CDN (no Pageable)
- Problem / context
  - Server-side pagination (Pageable) was removed. We need client-side pagination per class requirement using a CDN.

- Proposed solution
  - Add jQuery and pagination plugin (e.g., simple pagination or DataTables-lite mode) via CDN.
  - Convert challenge list to a client-side paginated table; keep difficulty filter client-side.

- In scope
  - Include CDN scripts/styles, initialize pagination on the list page, basic next/prev UI.

- Out of scope
  - Complex sorting/filtering beyond what we already expose.

- Acceptance criteria
  - [ ] Challenges list paginates client-side without any Pageable in controllers/services
  - [ ] Sort/filters work (client-side) as currently expected

- Area
  - area:web

- Dependencies / related issue
  - None

- Suggested labels
  - status:triage, enhancement, feature:pagination-jquery, area:web, priority:P2-normal

---

## 4) Feature: Drill Submission Flow (UI + Controller + Service wiring)

- Title
  - feat(drill): Submission flow using stub runner; update DrillItem and redirect to next due
- Problem / context
  - DrillService scheduling exists, but the user-facing submit flow needs wiring to create Submissions and update DrillItem (streak, timesSeen, nextDueAt) with the stub run service.

- Proposed solution
  - Add a submit form on the drill/solve page (language + code + action).
  - Controller posts to record outcome via run stub → create Submission → update DrillItem → redirect to next item in queue.

- In scope
  - Submission controller endpoint(s), use existing Outcome enum, service wiring.

- Out of scope
  - Real code execution or sandboxing (stub only).

- Acceptance criteria
  - [ ] Posting a solution creates a Submission row and updates the related DrillItem fields
  - [ ] Next item in the due queue is shown after submit

- Area
  - area:web, area:service

- Dependencies / related issue
  - Feature: jQuery-based Pagination (not strictly required)

- Suggested labels
  - status:triage, enhancement, feature:drill-mode, area:web, area:service, priority:P1-high

---

## 5) Test Coverage: Drill Scheduling and Submission Flow

- Title
  - test(service,web): Coverage for Drill scheduling rules and submission flow
- Problem / context
  - We need tests covering DrillService scheduling (nextDueAt, streak, timesSeen) and the submission flow that persists outcomes.

- Proposed solution
  - Add service-level tests for recordOutcome and queue building.
  - Add WebMvc tests covering submit endpoint and redirects.

- In scope
  - Unit/integration tests for DrillService and Submission controller.

- Out of scope
  - End-to-end browser tests.

- Acceptance criteria
  - [ ] Service tests verify rules for CORRECT/ACCEPTABLE/INCORRECT/SKIPPED (streak/nextDueAt)
  - [ ] Web tests verify POST submits create a Submission and redirect to next due

- Area
  - area:service, area:web

- Dependencies / related issue
  - Feature: Drill Submission Flow

- Suggested labels
  - status:triage, status:needs-tests, area:service, area:web, priority:P2-normal

---

## 6) Persistence: Finalize User Entity + DAO

- Title
  - feat(persistence): Finalize User entity + DAO (minimal OIDC linkage)
- Problem / context
  - User model needs to be present/confirmed for auth features and submission ownership. Confirm mappings.

- Proposed solution
  - Create/confirm User entity (id, username/email, roles/authorities minimal), DAO, and relationships to Submission (if required now).

- In scope
  - Entity, DAO, basic CRUD tests, minimal fields for OIDC principal linkage.

- Out of scope
  - Full RBAC persistence and profile pages (later weeks).

- Acceptance criteria
  - [ ] User DAO CRUD tests pass
  - [ ] Relationships compile and basic lookups work (e.g., find submissions by user)

- Area
  - area:persistence

- Dependencies / related issue
  - Cognito login; may map OIDC subject to a local user in future work

- Suggested labels
  - status:triage, enhancement, area:persistence, priority:P2-normal

---

## 7) Persistence Hygiene: Lazy/Eager and Cascade for Challenge, Submission, DrillItem

- Title
  - chore(persistence): Review lazy/eager and cascade settings for core entities
- Problem / context
  - We need to confirm fetch/cascade settings to avoid N+1 or orphan records during submissions and deletes.

- Proposed solution
  - Review entities and annotate fetch/cascade explicitly where needed (e.g., submissions under challenge; drillItem relationships).

- In scope
  - Audit mappings and adjust fetch/cascade; add a couple of DAO tests to assert cascades.

- Out of scope
  - Performance tuning beyond fetch mode sanity.

- Acceptance criteria
  - [ ] No unexpected orphan rows on delete
  - [ ] Reasonable defaults for fetch; no major N+1 in common paths

- Area
  - area:persistence

- Dependencies / related issue
  - Test Coverage issue for DAO behaviors if updated

- Suggested labels
  - status:triage, enhancement, area:persistence, priority:P3-low

---

## 8) Tooling/CI: EB Bundle + Health Readiness

- Title
  - ci(deploy): EB bundle (Procfile + app.jar) and /actuator/health UP
- Problem / context
  - Ensure CI produces eb-bundle.zip (Procfile + app.jar) and that /actuator/health is UP for EB.

- Proposed solution
  - Validate build.yml job uploads eb-bundle artifact.
  - Verify application.yml exposes health and server.port uses EB $PORT.
  - Optional smoke test step before upload.

- In scope
  - CI packaging, artifact verification, health exposure.

- Out of scope
  - Automated EB deployment (manual deploy acceptable this week).

- Acceptance criteria
  - [ ] CI run shows eb-bundle.zip uploaded
  - [ ] Local run shows /actuator/health UP
  - [ ] Procfile uses app.jar and starts with $PORT

- Area
  - area:ci-cd, area:deployment

- Dependencies / related issue
  - None

- Suggested labels
  - status:triage, area:ci-cd, area:deployment, feature:aws-eb, priority:P2-normal

---

## 9) Logging Hygiene: Replace any lingering System.out

- Title
  - chore(logging): Replace remaining System.out with Log4J
- Problem / context
  - Course requirement is Log4J; ensure no System.out.println remains.

- Proposed solution
  - Search code for System.out and replace with logger calls at appropriate levels.

- In scope
  - Code scan and minimal refactor to Log4J.

- Out of scope
  - Structured JSON logging (future).

- Acceptance criteria
  - [ ] No System.out calls in main code
  - [ ] Tests remain green

- Area
  - area:logging

- Dependencies / related issue
  - None

- Suggested labels
  - status:triage, area:logging, priority:P3-low

---

## 10) Documentation: Week 6 Docs & Plan Updates

- Title
  - docs: Update Week 6 docs (README, project-plan, journal)
- Problem / context
  - Keep docs aligned with auth, pagination, and EB changes.

- Proposed solution
  - Update project-plan.md checkboxes for Week 6.
  - Update README if any config or env changes.
  - Add journal/time log entries for Week 6.

- In scope
  - README, docs/project-plan.md, docs/reflections/WeeklyJournal.md, and week-6-plan status.

- Out of scope
  - Detailed architecture write-ups (later).

- Acceptance criteria
  - [ ] Docs reflect Cognito envs, EB bundle/health, and jQuery pagination
  - [ ] Week 6 progress marked in plan

- Area
  - area:documentation

- Dependencies / related issue
  - None

- Suggested labels
  - status:triage, area:documentation, priority:P3-low
