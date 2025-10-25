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
  - Use github/issue-labeler or actions/labeler with path-based rules
  - Map paths (e.g., `src/main/java/**/web/**` → `area:web`, `src/main/java/**/service/**` → `area:service`, etc.)
- Acceptance Criteria:
  - [ ] Labels applied automatically on PR open/sync
  - [ ] Maintainers can override labels without them being re-applied incorrectly
  - [ ] Rules documented in the workflow file and/or CONTRIBUTING.md

## Repository Hygiene

3) Fill in CODEOWNERS with your actual team handles
- Context: CODEOWNERS currently uses placeholders for security/deployment teams.
- Acceptance Criteria:
  - [ ] Replace `@your-org/security-team` and `@your-org/deployment-team` with real GitHub users/teams
  - [ ] Verify review requests are auto-assigned when touching the relevant paths

4) Add @ControllerAdvice to inject session user into all views
- Context: Controllers currently add the session user per endpoint. A global model attribute will simplify views.
- Approach:
  - Create a `GlobalModelAttributes` class annotated with `@ControllerAdvice`
  - Add a `@ModelAttribute("user")` method that returns the session user if present
- Acceptance Criteria:
  - [ ] All JSPs can reference `${user}` without per-controller boilerplate
  - [ ] No behavior change for unauthenticated users (user remains null)
