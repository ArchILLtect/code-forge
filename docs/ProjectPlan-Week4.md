# Class and Indie Project Plan (Weeks 4–14)
Inputs: `README.md`, `course_calendar.md`, `project_overview.md`, `project_rubric_clean.md`
Scope: Ent. Java (Class) and 'code\-forge' (Indie)

Legend:
- Class \= Ent. Java course tasks and milestones
- Indie \= 'code\-forge' project tasks based on `README.md` user stories and MVP

### Week 4 — Hibernate and Data Model
- Class
    - [ ] Lecture/lab: Hibernate mappings, configs, testing patterns, logging
    - [ ] Set up dev and test databases, seed test data
    - [ ] Replace `System.out` with Log4J/SLF4J
- Indie
    - [ ] Finalize initial DB schema from `README.md` domain
    - [ ] Implement entity mappings and one DAO with full CRUD
    - [ ] Create Hibernate configs for dev/test; set up test DB
    - [ ] Unit tests for DAO (happy path and edge cases)
    - [ ] Confirm MVP stories align to `project_rubric_clean.md`
    - [ ] Update project plan and weekly journal

### Week 5 — Web Tier and MVP Foundations
- Class
    - [ ] JSP/Servlets, JSTL, controllers, request lifecycle
    - [ ] DAO integration patterns in the web tier
- Indie
    - [ ] Implement controllers/services wiring to DAO
    - [ ] Build basic JSPs for listing/detail and form submission
    - [ ] Implement MVP user stories 1–2 from `README.md`
    - [ ] Error handling, validation, and basic UX
    - [ ] Expand unit and integration tests

### Week 6 — AuthN/Z, Testing, and Logging Quality
- Class
    - [ ] Authentication/authorization fundamentals
    - [ ] Unit/integration testing practices and coverage
- Indie
    - [ ] Implement authentication/authorization as required by `README.md`
    - [ ] Secure protected routes and data access
    - [ ] Centralize logging; add diagnostic context and log rotation
    - [ ] Raise code coverage threshold; add CI build and static analysis
    - [ ] Prep for Checkpoint 2 readiness

### Week 7 — Checkpoint 2
- Class
    - [ ] Checkpoint 2 due: DB designed/created; at least one DAO with full CRUD using Hibernate; DAO unit tested; Log4J implemented
- Indie
    - [ ] Ensure all Checkpoint 2 items complete and visible in GitHub
    - [ ] Harden DAO and transaction boundaries; add more entities if needed
    - [ ] Journal/time log; README badges for build/coverage

### Week 8 — Deployment and External Services
- Class
    - [ ] AWS environment and deployment walkthrough
    - [ ] REST/Web Services consumption patterns
- Indie
    - [ ] Create project DB on AWS and migrate schema
    - [ ] Update configs for AWS; externalize secrets
    - [ ] Deploy app to AWS; verify health and logs
    - [ ] Integrate at least one external API per `README.md` (if in scope)
    - [ ] Add deployed link to indie project list

### Week 9 — Checkpoint 3 and Stabilization
- Class
    - [ ] Checkpoint 3 due: Deployed to AWS; at least one JSP displays DB data; authentication implemented; deployed link added
- Indie
    - [ ] Validate Checkpoint 3 criteria and fix gaps
    - [ ] Implement next prioritized MVP stories (3–4)
    - [ ] Monitoring and basic metrics; error log review

### Week 10 — Feature Iteration and Data Access Depth
- Class
    - [ ] Advanced Hibernate: criteria, pagination, N\+1 mitigation
    - [ ] Validation and error handling patterns
- Indie
    - [ ] Implement next user stories from `README.md`
    - [ ] Add pagination, sorting, and input validation
    - [ ] Performance: indexes, query tuning, lazy/eager balance
    - [ ] Expand integration tests against test DB

### Week 11 — UX Polish and Resilience
- Class
    - [ ] JSP/UI refinement, accessibility basics, resilient design
- Indie
    - [ ] UI/UX polish for implemented flows
    - [ ] Add retries/timeouts for external API calls
    - [ ] Improve empty/error states and form usability
    - [ ] Update docs: architecture, endpoints, data model diagram

### Week 12 — Review and Hardening
- Class
    - [ ] Peer reviews, code reviews, presentation prep
- Indie
    - [ ] Address review feedback; refactor risky areas
    - [ ] Security pass: input sanitization, authZ checks
    - [ ] Backup/restore steps; deployment runbook
    - [ ] Code quality and lint pass; eliminate dead code

### Week 13 — Final Touches (Indie Focus)
- Class
    - [ ] Open lab and Q\&A
- Indie
    - [ ] Bug bash; resolve critical and high issues
    - [ ] Accessibility and responsive checks
    - [ ] Final test coverage pass; stabilize configs
    - [ ] Tag release candidate and freeze features

### Week 14 — Final Delivery
- Class
    - [ ] Final presentations and submissions
- Indie
    - [ ] Release v1\.0; publish GitHub release notes and changelog
    - [ ] Create demo video; add link to `readme.md`
    - [ ] Finalize documentation and rubric self\-check (`project_rubric_clean.md`)
    - [ ] Final journal/time log entry