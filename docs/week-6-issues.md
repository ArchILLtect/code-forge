# Week 6 – Issue Seeds (Copy/Paste into New Issues)

Use these blocks to create GitHub issues with our templates. Each includes: Problem / context, Proposed solution, In scope / Out of scope, Acceptance criteria, Area, Dependencies / related issue, and Suggested labels.

---

## 1) Authentication (MVP): Cognito Hosted UI + servlet callback + session persistence

- Title
  - feat(auth-mvp): Cognito Hosted UI login (servlet-based) + ID token verify + session persistence
- Problem / context
  - We need login working now for MVP/class demos, without migrating the whole app to Spring Security yet.

- Proposed solution
  - Keep the current servlet-based flow:
    - /logIn → redirect to Cognito Hosted UI (includes scope and encoded params)
    - /auth → exchange code for tokens, verify ID token (JWKS), store AuthenticatedUser in HTTP session
    - /me → display user from session; HomeController also exposes session user
  - Remove secrets from source; read Cognito client secret from env (COGNITO_CLIENT_SECRET).

- In scope (MVP)
  - Hosted UI redirect, code→token exchange, JWKS verification
  - Session persistence for user across requests
  - README/docs for local + EB env vars

- Out of scope (Deferred → see Post‑MVP issue)
  - Spring Security OIDC configuration and route protection
  - Role/authority mapping and admin protections

- Acceptance criteria (MVP)
  - [x] Clicking “Login with Cognito” starts Hosted UI flow and returns to /auth
  - [x] /auth verifies ID token, stores user in session, redirects to /me
  - [x] /me shows the user; navigating away/back still shows user (until logout or timeout)
  - [x] No secrets in repo; COGNITO_CLIENT_SECRET read from env (local + EB)
  - [x] README updated with local/EB env setup and .env.example added

- Area
  - area:security, area:web

- Dependencies / related issue
  - Follow‑up: Post‑MVP migration to Spring Security OIDC (see new issue below)

- Suggested labels
  - status:done, area:security, area:web, priority:P1-high

- Closing comment (paste into the existing GitHub Issue 1)
  - “Resolution: Implemented servlet-based Cognito login for MVP. The app redirects to Hosted UI (/logIn), exchanges the code on /auth, verifies the ID token, and stores the user in the HTTP session; /me and home read the session user. Secrets are no longer in source; COGNITO_CLIENT_SECRET is provided via env (local + EB). Further work to migrate to Spring Security OIDC + route protection is tracked separately in ‘Post‑MVP: Spring Security OIDC + route protection’. Closing this issue as completed for MVP.”

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
  - Depends on: Post‑MVP: Spring Security OIDC migration (see Issue 12)

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

## 8) Tooling/CI: EB Tomcat (WAR) + Health Readiness

- Title
  - ci(deploy): Produce WAR artifact for EB Tomcat and verify /actuator/health
- Problem / context
  - We’ve switched from JAR + Procfile (Java SE) to WAR packaging for JSP support. Elastic Beanstalk should use a Tomcat platform (Corretto 17), and deployments should use the built WAR artifact.

- Proposed solution
  - CI builds and uploads target/codeforge-0.0.1-SNAPSHOT.war as an artifact.
  - Verify Actuator health endpoint is exposed and returns UP locally.
  - Document EB environment setup (Tomcat platform) and manual WAR deploy step.

- In scope
  - CI artifact upload (WAR), health exposure, minimal deployment notes.

- Out of scope
  - Automated EB deployment (manual deploy acceptable this week).

- Acceptance criteria
  - [ ] CI run uploads the WAR artifact (e.g., codeforge-0.0.1-SNAPSHOT.war)
  - [ ] Local run shows /actuator/health UP
  - [ ] EB environment uses a Tomcat platform (Corretto 17) and accepts the WAR

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
  - [ ] Docs reflect Cognito envs, EB Tomcat + WAR deployment, and jQuery pagination
  - [ ] Week 6 progress marked in plan

- Area
  - area:documentation

- Dependencies / related issue
  - None

- Suggested labels
  - status:triage, area:documentation, priority:P3-low

---

## 11) Security: Secret hygiene (rotate + SSM/Secrets Manager)

- Title
  - chore(security): Rotate Cognito client secret (if needed) and move EB env secret to SSM/Secrets Manager
- Problem / context
  - The Cognito client secret must not be shared in plaintext; use AWS-managed secrets and least-privilege access. EB currently uses plain-text env vars which are visible to console users.

- Proposed solution
  - (If ever exposed) Rotate the Cognito App Client secret in AWS.
  - Store the secret in AWS Secrets Manager or SSM Parameter Store.
  - Update EB environment property Source to reference the managed secret/parameter.
  - Ensure the EB instance role has permission (IAM policy) to read the secret/parameter.

- In scope
  - Secret rotation (if needed), SSM/Secrets Manager setup, EB env property update, IAM permission check.

- Out of scope
  - Spring Security OIDC migration.

- Acceptance criteria
  - [ ] Cognito client secret not stored in plaintext in EB
  - [ ] EB environment reads secret from SSM/Secrets Manager
  - [ ] Instance role can read secret and app starts cleanly

- Area
  - area:security, area:deployment

- Dependencies / related issue
  - Week 6 Issue 1 (Cognito auth) for context

- Suggested labels
  - status:triage, area:security, area:deployment, priority:P2-normal

---

## 12) Post‑MVP: Spring Security OIDC + route protection

- Title
  - feat(security-post-mvp): Migrate to Spring Security OIDC and protect routes
- Problem / context
  - MVP uses a servlet-based Cognito flow for speed. For robustness and standardized security, migrate to Spring Security OIDC, put user in SecurityContext, and secure routes centrally.

- Proposed solution
  - Configure Spring Security OIDC for Cognito (issuer-uri or full provider/client config).
  - Replace servlet callback logic with Spring Security’s OAuth2 login flow (or run in parallel during transition).
  - Add HttpSecurity rules: permitAll for public pages, authenticated for drill/submission, ADMIN for admin CRUD.
  - Map OIDC claims to authorities (e.g., email/roles) and expose principal in controllers as needed.
  - Keep secrets in env/SSM; remove any unused servlet auth code post-migration.

- In scope
  - Security config, route protection, principal mapping, docs updates.

- Out of scope
  - Advanced RBAC, custom user provisioning.

- Acceptance criteria
  - [ ] Visiting protected routes redirects to Cognito and returns successfully
  - [ ] SecurityContext holds the authenticated principal; controllers can inject it
  - [ ] Public vs. protected vs. admin routes behave as expected
  - [ ] Secrets are read from env/SSM (no repo secrets)
  - [ ] Legacy servlet-based auth removed (or clearly deprecated) after migration

- Area
  - area:security

- Dependencies / related issue
  - Relates to: 2) Protect Routes and Challenge Data
  - Supersedes: 1) Authentication (MVP) for long-term auth strategy

- Suggested labels
  - status:triage, enhancement, area:security, post-mvp, priority:P2-normal
