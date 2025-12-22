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

2) Add a PR labeler workflow to apply area:* and feature:* labels based on changed files
- Context: Reduce manual labeling; drive better triage and automation.
- Approach:
  - Use `github/issue-labeler` or `actions/labeler` with path-based rules
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

2) Add @ControllerAdvice to inject session user into all views
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

2) Retrieve images/JSON/etc. from S3 instead of packaging in the WAR (labels: optimization, hardening)
- Title: Post‑MVP: serve static assets from S3 instead of bundling in WAR
- Context: Currently, static assets (images, JSON files) are packaged in the WAR, leading to larger deployments and less flexibility. Moving these assets to S3 will improve scalability and ease updates.
- Tasks:
  - [ ] Identify all static assets currently bundled in the WAR
  - [ ] Upload these assets to a designated S3 bucket with appropriate permissions
  - [ ] Update the application code to reference the S3 URLs for these assets
  - [ ] Move static assets (images/json) from WAR to S3-backed asset pipeline  
    - [ ] Implement AssetConfig for dev/prod asset switching  
    - [ ] Create JSP tag to generate S3 URLs dynamically  
    - [ ] Update JSPs to use <cf:asset> instead of local WAR paths  
    - [ ] Remove static files from WAR after migration
  - [ ] Test to ensure assets load correctly from S3 in all environments
- Acceptance Criteria:
  - [ ] Static assets are no longer included in the WAR file
  - [ ] Application successfully retrieves and displays assets from S3
  - [ ] Documentation updated to reflect the new asset management approach

3) Drill editor UX enhancements (labels: enhancement, ux)
- Title: Post‑MVP: drill editor polish (tooltip + last saved stamp)
- Context: Current solve page auto-saves code per challenge and shows character count. Improve feedback and clarity.
- Tasks:
  - [ ] Add small tooltip near character counter: "Saved locally (browser)".
  - [ ] Track last auto-save timestamp (update on debounce persist) and display e.g. "Last saved: 14:32:07".
  - [ ] Gracefully handle localStorage quota errors (already swallowed; consider user hint if persist fails repeatedly).
  - [ ] Optional: Add a diff warning if localStorage version diverges from server-provided starter template.
- Acceptance Criteria:
  - [ ] Tooltip renders on hover/focus next to counter.
  - [ ] Timestamp updates after typing and on submit.
  - [ ] No runtime errors in browsers without localStorage (fallback display hidden).

- [ ] Wire flash feedback into queue/solve JSP via a simple session attribute.
      Note: Add after DrillServlet posts; read feedback on next page and clear it.
- [ ] Update PracticeServlet (public) to render evaluator feedback inline, using expectedAnswer but skipping persistence.
      Add after evaluator is implemented (post-evaluator).

# Security Hardening TODOs

- [Security][Logging] Audit for sensitive token logging and neutralize:
  - Grep for `response.body()` and any logs that include `access_token`, `id_token`, or `refresh_token`.
  - Replace with size-only or redacted logs; avoid printing full headers or bodies.
  - Ensure package logger level is `info` in production (profile-based override).
- [Security][Logging] Add Log4j2 RegexFilter to drop lines containing token keys (`access_token|id_token|refresh_token`) in file/telemetry appenders.
- [Security][Testing] Add a lightweight test that generates a mock token response and asserts logs do not contain token values (use an in-memory appender or temporary file).
- [Docs] Note logging policy in `projects/mvp/README.md`: never log tokens; use redaction and info-level in prod.
