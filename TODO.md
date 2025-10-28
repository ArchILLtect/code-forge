# TODO

## CI / Workflow Enhancements

1) Add a lightweight CI guard to fail PRs if the checklist isn’t completed
  - Context: Our PR template includes a checklist; enforce completion to prevent accidental merges.
  - Options:
    - PR comment/action check (e.g., parse PR body and fail if unchecked boxes remain)
    - Conventional-commit or custom action that validates presence of key markers
  - Acceptance Criteria:
    - [ ] CI job fails when any mandatory checklist item is unchecked
    - [ ] The failure message links to the PR template section to fix
    - [ ] Job is optional on drafts and required on ready-for-review PRs

1) Add a PR labeler workflow to apply area:* and feature:* labels based on changed files
- Context: Reduce manual labeling; drive better triage and automation.
- Approach:
  - Use gitHub/issue-labeler or actions/labeler with path-based rules
  - Map paths (e.g., `src/main/java/**/web/**` → `area:web`, `src/main/java/**/service/**` → `area:service`, etc.)
- Acceptance Criteria:
  - [ ] Labels applied automatically on PR open/sync
  - [ ] Maintainers can override labels without them being re-applied incorrectly
  - [ ] Rules documented in the workflow file and/or CONTRIBUTING.md

## Repository Hygiene

1) Fill in CODEOWNERS with your actual team handles
- Context: CODEOWNERS currently uses placeholders for security/deployment teams.
- Acceptance Criteria:
  - [ ] Replace `@your-org/security-team` and `@your-org/deployment-team` with real GitHub users/teams
  - [ ] Verify review requests are auto-assigned when touching the relevant paths

1) Add @ControllerAdvice to inject session user into all views
- Context: Controllers currently add the session user per endpoint. A global model attribute will simplify views.
- Approach:
  - Create a `GlobalModelAttributes` class annotated with `@ControllerAdvice`
  - Add a `@ModelAttribute("user")` method that returns the session user if present
- Acceptance Criteria:
  - [ ] All JSPs can reference `${user}` without per-controller boilerplate
  - [ ] No behavior change for unauthenticated users (user remains null)

## Post MVP

1) Adopt Flyway with initial baseline migration (labels: migration, hardening)
- Title: Post‑MVP: adopt Flyway with `V1__init_schema.sql`
- Context: After MVP deployment using Hibernate auto‑DDL (update → validate), baseline the verified RDS schema and move to versioned migrations for safety and collaboration.
- Tasks:
  - [ ] Export current RDS schema as `V1__init_schema.sql`
  - [ ] Add Flyway dependency and enable Flyway in Spring Boot
  - [ ] Configure `spring.jpa.hibernate.ddl-auto=validate` for prod; keep schema immutable under JPA
  - [ ] Place `V1__init_schema.sql` in `src/main/resources/db/migration/`
  - [ ] Document migration workflow for devs (how to add `V2__...sql`, `V3__...sql`)
- Acceptance Criteria:
  - [ ] App validates baseline on startup; no pending migrations
  - [ ] Subsequent schema changes are applied via new versioned scripts and verified in CI
