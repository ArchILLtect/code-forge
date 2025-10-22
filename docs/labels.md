# Project Labels

This file lists new labels to add for CodeForge, plus a description for the existing "checkpoint" label. For each new label, create it in GitHub with the name, description, and color below (hex without the leading `#`).

## Existing Label (keep as-is)
- name: checkpoint
  - description: Use for Project Checkpoints 1–3. Apply to issues/PRs that scope, track, or deliver checkpoint requirements. Checkpoint 1 is complete; Checkpoint 2 is due this week. Include acceptance evidence (tests, screenshots) and demo notes when used.
  - color: (leave as-is)

---

## New Labels to Create

### Workflow/Status
- name: status:blocked
  - description: Work can’t proceed until a dependency or decision is resolved (e.g., environment, access, upstream review).
  - color: b60205
- name: status:in-progress
  - description: Actively being worked on; not ready for review.
  - color: 0e8a16
- name: status:needs-review
  - description: Ready for code review or product approval.
  - color: fbca04
- name: status:needs-tests
  - description: Missing or insufficient unit/integration tests; add tests before merge.
  - color: e4e669
- name: status:triage
  - description: Newly filed or uncategorized. Needs initial review, labeling, and priority assignment.
  - color: ededed
- name: status:needs-info
  - description: Awaiting more details from the reporter (steps, logs, environment) before work can continue.
  - color: fef2c0

### Priority
- name: priority:P0-critical
  - description: Critical path or breakage; address immediately.
  - color: e11d21
- name: priority:P1-high
  - description: High importance; schedule next.
  - color: d93f0b
- name: priority:P2-normal
  - description: Normal priority; plan within the current milestone.
  - color: 84b6eb
- name: priority:P3-low
  - description: Nice-to-have or non-blocking; schedule as time permits.
  - color: c2e0c6

### Area/Component
- name: area:web
  - description: Controllers, JSP/JSTL, view models, and UI wiring.
  - color: 1d76db
- name: area:service
  - description: Business logic and orchestration (e.g., DrillService, ChallengeRunService).
  - color: 006b75
- name: area:persistence
  - description: Entities, DAOs, JPA/Hibernate mappings, and database concerns.
  - color: 5319e7
- name: area:security
  - description: Authentication/authorization, Spring Security, Cognito integration.
  - color: a2eeef
- name: area:ci-cd
  - description: GitHub Actions, build, test, packaging, and release workflows.
  - color: d4c5f9
- name: area:deployment
  - description: AWS Elastic Beanstalk, environments, and runtime configs.
  - color: bfdadc

### Feature/Theme
- name: feature:drill-mode
  - description: Drill queue, scheduling (nextDueAt), streak/timesSeen, and related UI.
  - color: f9d0c4
- name: feature:practice-mode
  - description: Practice flow, challenge prompts, and basic submission behavior.
  - color: c5def5
- name: feature:cognito-auth
  - description: OAuth2/OIDC login via Amazon Cognito and protected routes.
  - color: d1d5da
- name: feature:aws-eb
  - description: Elastic Beanstalk packaging, Procfile, health checks, and deployment.
  - color: c0e585
- name: feature:pagination-jquery
  - description: Client-side pagination using jQuery/CDN; no server-side Pageable.
  - color: fef2c0
- name: api:external
  - description: Work related to external/public API selection, integration, and resilience.
  - color: 5f8b95
- name: logging
  - description: Log4J configuration, structured logging, MDC, and log hygiene.
  - color: f7c6c7

### Project
- name: project:mvp
  - description: Use for all issues and PRs that belong to the MVP release scope. Project workflow can auto-add labeled items to the "CodeForge – MVP Release" board.
  - color: e6ffcc
