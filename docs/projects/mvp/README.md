# CodeForge MVP Release Project

This GitHub Project tracks the minimum set of features, quality gates, and deployment steps required to ship the CodeForge MVP.

## Project Name
CodeForge – MVP Release

## Project Description
Deliver a working MVP of CodeForge that supports:
- Cognito-based login (OIDC)
- Client-side pagination via jQuery CDN
- Drill submission flow wired to scheduling logic (stub runner)
- Health checks and packaging for AWS Elastic Beanstalk
- Documentation updates and basic test coverage

The project board groups all issues labeled `project:mvp` into a single execution view. Use this label to include new work items in the MVP scope.

## Goals (Done = MVP)
- AuthN: Cognito OIDC login works locally and on EB
- AuthZ: Public vs protected routes enforced; admin pages limited
- Drill: Users can submit, persist outcomes, and advance in queue
- Pagination: Challenges list paginates client-side (no Pageable)
- Deployability: eb-bundle.zip produced by CI; /actuator/health = UP
- Documentation: README, plan, and journal reflect Week 6+ changes
- Tests: Core DAO/service/web tests cover MVP flows

## Non-Goals (after MVP)
- Real code execution sandbox
- Advanced RBAC and profile management
- Structured JSON logging and dashboards
- Production DB migration and multi-tenant support

## Board Columns (suggested)
- Backlog
- Ready
- In Progress
- In Review
- Done

## How to Add Work to This Project
- Apply the label `project:mvp` to any issue/PR that should appear on the MVP board.
- Set up a Project workflow rule in GitHub Projects:
  1. Open the project → Settings → Workflows
  2. Add a rule: "When issue is added to the repository and label = project:mvp → Add to project"
  3. Optionally auto-set status to "Ready"

## Labels
- Required: `project:mvp`
- Common: `area:security`, `area:web`, `area:service`, `area:ci-cd`, `area:deployment`, `area:logging`, `feature:cognito-auth`, `feature:pagination-jquery`, `feature:drill-mode`, `documentation`

## Issue Templates to Use
- Feature: Cognito, route protection, client-side pagination, drill submission
- Test coverage: Drill scheduling & submission
- Tooling/CI: EB bundle and health readiness
- Documentation: Week 6 updates

## Milestone Suggestions
- MVP Ready (target date): set according to your course schedule
- Post-MVP hardening and polish

## Links
- Week 6 Plan: `docs/week-6-plan.md`
- Issue seeds: `docs/week-6-issues.md`
- Labels: `docs/labels.md`
- CI workflow: `.github/workflows/build.yml`
- Health endpoint: `/actuator/health`
