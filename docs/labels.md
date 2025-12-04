# Project Labels

This file lists labels to add for CodeForge. For each new label, create it in GitHub with the name, description, and color below (hex without the leading `#`).

## Workflow/Status
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
    // TODO: NEW--add to GitHub
- name: status:backlog
  - description: Not scheduled for the current milestone; parked for future planning.
  - color: d1d5da

## Priority
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

## Area/Component
- name: area:web
  - description: Servlets, JSP/JSTL, view models, and UI wiring.
  - color: 1d76db
- name: area:service
  - description: Business logic and orchestration (e.g., DrillService, ChallengeRunService).
  - color: 006b75
- name: area:persistence
  - description: Entities, DAOs, Hibernate mappings, and database concerns.
  - color: 5319e7
- name: area:security
  - description: Authentication/authorization, Cognito integration, and route guards.
  - color: a2eeef
- name: area:ci-cd
  - description: GitHub Actions, build, test, packaging, and release workflows.
  - color: d4c5f9
- name: area:deployment
  - description: Tomcat, AWS environments, and runtime configs.
  - color: bfdadc
- name: area:logging
  - description: Log4J configuration, structured logging, MDC, and log hygiene.
  - color: f7c6c7
- name: area:ui/ux
  - description: UI layout, styling, accessibility, and UX flows (client-side behavior and visual polish).
  - color: f9c2ff
- name: area:documentation
  - description: README, docs/, inline JavaDoc, and project planning artifacts.
  - color: 0ea5e9

## Feature/Theme
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
  - description: Elastic Beanstalk packaging, health checks, and deployment.
  - color: c0e585
- name: feature:pagination-jquery
  - description: Client-side pagination using jQuery/CDN; no server-side Pageable.
  - color: fef2c0
- name: api:external
  - description: Work related to external/public API selection, integration, and resilience.
  - color: 5f8b95

## Hardening & Migrations
- name: hardening
  - description: Reliability, security, and performance improvements; tech debt reductions.
  - color: 8b0000
- name: migration
  - description: Database or infrastructure migrations (e.g., adopt Flyway post‑MVP).
  - color: 3a9ad9

## Project
- name: project:mvp
  - description: Use for all issues and PRs that belong to the MVP release scope. Project workflow can auto-add labeled items to the "CodeForge – MVP Release" board.
  - color: e6ffcc
- name: checkpoint
  - description: Used for checkpoint tracking issues (e.g., Week 7 – Project Checkpoint) and their templates.
  - color: 0366d6

### Cross-cutting / Initiative
- name: hardening
  - description: Stabilization, safety, and resilience work (e.g., transaction boundaries, locking, validation, error handling).
  - color: 6f42c1
- name: migration
  - description: Schema/data migrations and related tasks (e.g., Flyway/Liquibase scripts, rollout coordination).
  - color: 1f6feb

## Type
- name: enhancement
  - description: New feature work and functional improvements.
  - color: a2eeef
- name: chore
  - description: Maintenance, refactors, and non-feature tasks (e.g., config, build, logging hygiene).
  - color: d4c5f9

