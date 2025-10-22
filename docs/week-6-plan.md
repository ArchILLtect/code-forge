# Week 6 Plan — Authentication, Hosting, and Drill Foundations (Oct 6–12)

Source inputs: docs/project-plan.md (Week 6), Week 5 wrap-up notes, and deployment/auth checklist.

## Class Focus (from project-plan)
- Authentication/authorization fundamentals
- Unit/integration testing practices and coverage

## Indie Deliverables

### 1) Authentication & Authorization (Cognito OIDC)
- Implement OAuth2 login with Amazon Cognito (Spring Security OIDC)
- Secure protected routes and challenge data (role/authority checks)
- Replace any lingering System.out with Log4J structured logging (MDC-friendly pattern)

### 2) Drill Logic & Submissions (pushed from Week 5)
- DrillService: scheduling rules (nextDueAt), streak/timesSeen updates, queue orchestration
- Submission flow UI/controller/service: create Submission, update related DrillItem
- Add a minimal "run/execute" stub to record results (Outcome enum)

### 3) UI Pagination via CDN (course requirement)
- Remove server-side pagination entirely from service/controller APIs (no Pageable)
- Add jQuery-based pagination wiring via CDN on listing views
- Keep sort/filter behavior consistent with Week 5 (client-side where feasible)

### 4) AWS Elastic Beanstalk (Java SE, Corretto 17)
- Create Procfile
- Health readiness
  - Ensure Spring Boot Actuator enabled and /actuator/health returns UP
- Packaging & smoke tests
  - Build locally; run jar with overridden port; verify health endpoint and basic pages

### 5) CI and Packaging
- Add GitHub Actions workflow build-and-package.yml
  - mvn -B -DskipTests=false verify
  - Produce eb-bundle.zip artifact suitable for Beanstalk deployment (jar + Procfile + any EB config)
- CI cleanup to reflect jQuery pagination (no Pageable assumptions in builds/tests)

### 6) Data Model & DAO Hygiene
- Finalize entity + DAO mappings for User, Challenge, Submission, DrillItem
  - Confirm lazy/eager loading and cascade settings
- Add missing service + controller layers for submissions & result tracking

### 7) Tests
- Expand JUnit coverage for DAO + service logic (submissions + drill scheduling)
- Update WebMvc tests for list/detail and submission flows post-pagination change

### 8) Documentation
- Update README with: Cognito auth, EB deploy notes, health endpoint, CI badge/workflow summary
- Update docs/project-plan.md progress markers for Week 6
- Journal/time log entries for Week 6

## Done When
- Cognito-based login works locally (OIDC flow completes; protected routes enforced)
- DrillService updates streak/timesSeen/nextDueAt and drives a simple queue
- Submission flow persists outcomes and updates DrillItem as expected
- jQuery pagination is wired via CDN; server APIs no longer require Pageable
- EB bundle (eb-bundle.zip) produced by CI and validated with a local jar smoke test
- /actuator/health returns UP
- README and project-plan updated; tests green in CI

## Evidence
- CI build logs and artifact (eb-bundle.zip)
- Screenshots/GIF: login redirect with Cognito, drill queue working, submission recorded
- Test report (DAO + service + WebMvc)
- README and docs diffs

## Notes & Assumptions
- Target runtime: Java 17 (Corretto 17 on EB)
- Secrets for Cognito configured via environment variables or EB configuration (not committed)
- If actuator not present, add dependency and minimal config as part of this week

