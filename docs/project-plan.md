# Class and Indie Project Plan (Weeks 4–14)

_Last Updated: September 24, 2025_
_Inputs: README.md, course_calendar.md, project_overview.md, project_rubric_clean.md_

Indie Project Scope:
- Build a Java web app focused on: Practice Mode, Drill Mode, Progress Tracking, Problem database with Admin interface, and a polished UI.
- Stack: Spring Boot, JPA, JUnit, relational DB (local H2 for dev, MySQL/PostgreSQL later), IntelliJ IDEA.

**Legend**
- **Class** = Enterprise Java course tasks and milestones
- **Indie** = *CodeForge* project tasks (based on `README.md`, rubric, and roadmap)

---

### Week 4 — Hibernate and Data Model _(Sept 22–28)_
- **Class**
  - [x] Lecture/lab: Hibernate mappings, configs, testing patterns, logging
  - [x] Set up dev and test databases, seed test data
  - [x] Replace `System.out` with Log4J/SLF4J
- **Indie**
  - [x] Deliver this week (Checkpoint 1)
    - [x] Finalize **Problem Statement** and **confirm MVP stories** (align with rubric)
    - [x] Finalize initial DB schema from `README.md` challenge domain
      - [x] Schema sketch (initial): User, Challenge, Submission, DrillItem.
      - [x] Create ER diagram artifact and baseline DB migrations (Flyway/Liquibase)
    - [x] Implement entity mappings and one DAO with full CRUD
      - [x] One entity + DAO + CRUD + tests (Challenge).
    - [x] Create Hibernate configs for dev/test; set up test DB
    - [x] Unit tests for DAO (happy path and edge cases)
    - [x] Replace System.out with Log4J.
    - [x] Update project plan, journal, and time log
    - [x] Set up GitHub labels, milestones, issue templates, and weekly journal/time log checklist
    - [x] Push all changes to GitHub and tag Checkpoint 1
    - [x] Add ARCHITECTURE.md and DATA_MODEL.md scaffolds
    - [x] Rubric mapping: DB schema, DAO CRUD, logging, documentation
    - [x] Done when: ER diagram, DAO CRUD, tests, log4j, journal, plan, GitHub tag
    - [x] Evidence: ER diagram, DAO test report, repo tag, screenshots
    - [x] **Milestone:** Project 1 checkpoint complete! App template running and accessible locally. 🎉
    - [ ] Next: Add Submission/DrillItem DAOs, wire simple controllers/views, API spike.
    - [x] Update journal and time log
  - [ ] Implements Challenge entity + DAO + CRUD (foundation for Story 9: Admin Create Challenge, and Story 4: Learner submissions persistence).
  - DB note: DAO tested with H2, but dev DB = MySQL.

---

### Week 5 — Web Tier and MVP Foundations _(Sept 29–Oct 5)_
- **Class**
  - [ ] JSP/Servlets, JSTL, controllers, request lifecycle
  - [ ] DAO integration patterns in the web tier
- **Indie**
  - [ ] Rubric mapping: web tier, DAO integration, external API, CI
  - [ ] Implement controllers/services wiring to DAO [🔗](user-stories.md#learner) (Stories 2–3: Browse & View challenge).
  - [ ] Add DAO/CRUD for Submission and DrillItem; service layer for Drill logic. [🔗](user-stories.md#learner) (Story 4: Submit solution & see result, Story 6 outcomes).
  - [ ] Build basic JSPs for listing/detail and form submission [🔗](user-stories.md#learner) (Stories 2–4).
  - [ ] Add error handling, validation, and simple UX [🔗](user-stories.md#learner) (Stories 2, 3, 4).
  - [ ] ⭐ (Stretch Goal) Expand unit and integration tests [🔗](user-stories.md#learner) (Story 4 persistence, Story 6 outcomes).
  - [ ] ⭐ (Stretch Goal) Challenge run service (stubbed execution for now; real compile/run later if time).
  - [ ] Spike: shortlist and select external API, prove minimal call [🔗](user-stories.md#learner) (Story 8).
  - [ ] ⭐ (Stretch Goal) Add GitHub Actions pipeline for build/tests/static analysis
  - [ ] Done when: controllers, JSPs, API spike, CI pipeline
  - [ ] Evidence: screenshots, test report, API call log, CI badge
  - [ ] Update journal and time log

---

### Week 6 — Authentication, Testing, and Logging Quality _(Oct 6–12)_
- **Class**
  - [ ] Authentication/authorization fundamentals
  - [ ] Unit/integration testing practices and coverage
- **Indie**
  - [ ] Rubric mapping: authentication, logging, test coverage
  - [ ] Implement authentication/authorization (register/login via identity service) [🔗](user-stories.md#learner) (Story 1: Register/Sign in)
  - [ ] Secure protected routes and challenge data [🔗](user-stories.md#learner) (Story 2: Browse challenges)
  - [ ] Replace any lingering `System.out.println` with Log4J logging
  - [ ] ⭐ (Stretch Goal) Centralize logging with log rotation and diagnostic context
  - [ ] ⭐ (Stretch Goal) Raise code coverage threshold; add CI/static analysis
  - [ ] Prep for Checkpoint 2 readiness
  - [ ] Done when: login/register, protected routes, log4j, CI
  - [ ] Evidence: test report, screenshots, CI badge
  - [ ] Update journal and time log

---

### Week 7 — Checkpoint 2 _(Oct 13–19)_
- **Class**
  - [ ] **Checkpoint 2 due**: DB designed/created; at least one DAO with full CRUD using Hibernate; DAO unit tested; Log4J implemented
- **Indie**
  - [ ] Rubric mapping: DB, DAO, logging, test coverage
  - [ ] Double-check Checkpoint 2 items are complete and visible in GitHub (DAO + CRUD + tests, Log4J, DB schema)
  - [ ] Harden DAO and transaction boundaries; add more entities if needed [🔗](user-stories.md#learner) (Stories 4 & 6)
  - [ ] Add entries if missing (e.g. DrillItem)
  - [ ] README badges for build/coverage
  - [ ] Done when: all checkpoint items in repo
  - [ ] Evidence: repo tag, screenshots, test report
  - [ ] Update journal and time log

---

### Week 8 — Deployment and External Services _(Oct 20–26)_
- **Class**
  - [ ] AWS environment and deployment walkthrough
  - [ ] REST/Web Services consumption patterns
- **Indie**
  - [ ] Rubric mapping: deployment, external API, configs
  - [ ] Create project DB on AWS and migrate schema (Flyway/Liquibase)
  - [ ] Update configs for AWS; externalize secrets
  - [ ] Deploy app to AWS; verify health and logs
  - [ ] Consume at least one external/public API using Java (rubric requirement) [🔗](user-stories.md#learner) (Story 8: External API)
  - [ ] Add deployed link to indie project list
  - [ ] ⭐ (Stretch Goal) Document API contract and rate limits
  - [ ] ⭐ (Stretch Goal) Add health endpoints and structured logs; Switch to structured JSON logs
  - [ ] Done when: deployed, API integrated, health/logs
  - [ ] Evidence: deployed URL, API call log, screenshots
  - [ ] Update journal and time log

---

### Week 9 — Checkpoint 3 and Drill Mode _(Oct 27–Nov 2)_
- **Class**
  - [ ] **Checkpoint 3 due**: Deployed to AWS; at least one JSP displays DB data; authentication implemented; deployed link added
- **Indie**
  - [ ] Rubric mapping: Drill Mode, persistence, metrics
  - [ ] Validate Checkpoint 3 criteria and fix gaps
  - [ ] Implement Drill Mode persistence: track outcomes (`Correct`, `Acceptable`, `Incorrect`, `Skipped`) [🔗](user-stories.md#learner) (Stories 5: Drill mode)
  - [ ] Build algorithm to cycle skipped/incorrect challenges until completed [🔗](user-stories.md#learner) (Story 5)
  - [ ] ⭐ (Stretch Goal) Mix solved challenges into queue for re-check (“flashcard” effect)
  - [ ] ⭐ (Stretch Goal) Monitoring and basic metrics; error log review
  - [ ] Done when: Drill Mode works, metrics visible
  - [ ] Evidence: screenshots, test report, metrics log
  - [ ] Update journal and time log

---

### Week 10 — Admin Interface and Data Access Depth _(Nov 3–9)_
- **Class**
  - [ ] Advanced Hibernate: criteria, pagination, N+1 mitigation
  - [ ] Validation and error handling patterns
- **Indie**
  - [ ] Rubric mapping: admin UI, data access, performance
  - [ ] Implement Admin challenge management UI (create, edit, delete challenges) [🔗](user-stories.md#admin) (Stories 9–11: Admin CRUD)
  - [ ] Restrict admin routes with authorization rules (role-based access) [🔗](user-stories.md#admin) (Stories 10 & 11: security)
  - [ ] ⭐ (Stretch Goal) Add pagination, sorting, and input validation (use jQuery datatables)
  - [ ] ⭐ (Stretch Goal) Optimize performance: indexes, query tuning, lazy/eager balance
  - [ ] ⭐ (Stretch Goal) Expand integration tests against test DB
  - [ ] Done when: admin UI, role-based access, perf
  - [ ] Evidence: screenshots, test report
  - [ ] Update journal and time log

---

### Week 11 — Progress Tracking Dashboard and UX Polish _(Nov 10–16)_
- **Class**
  - [ ] JSP/UI refinement, accessibility basics, resilient design
- **Indie**
  - [ ] Rubric mapping: dashboard, UX, API resilience
  - [ ] Build Progress Tracking Dashboard: number solved, % success, cycle status [🔗](user-stories.md#learner) (Stories 6 & 7: Track outcomes, View submission history)
  - [ ] ⭐ (Stretch Goal) Enhance progress (tables, charts, summaries) with simple charts and pagination (jQuery) for history
  - [ ] Polish UI/UX for implemented flows [🔗](user-stories.md#learner) (Stories 2–7)
  - [ ] ⭐ (Stretch Goal) Add retries/timeouts for external API calls
  - [ ] ⭐ (Stretch Goal) Improve empty/error states and form usability
  - [ ] ⭐ (Stretch Goal) Update docs: architecture, endpoints, and data model diagram
  - [ ] Done when: dashboard, charts, polished UX
  - [ ] Evidence: screenshots, test report
  - [ ] Update journal and time log

---

### Week 12 — Review, CI/CD, and Hardening _(Nov 17–23)_
- **Class**
  - [ ] Peer reviews, code reviews, presentation prep
- **Indie**
  - [ ] Rubric mapping: security, CI/CD, code quality
  - [ ] Address review feedback; refactor risky areas
  - [ ] Security pass: input sanitization, authZ checks [🔗](user-stories.md#learner) (Stories 1, 9–11)
  - [ ] ⭐ (Stretch Goal) Add CI/CD pipeline (GitHub Actions or Maven build for compile + tests)
  - [ ] ⭐ (Stretch Goal) Backup/restore steps; deployment runbook
  - [ ] ⭐ (Stretch Goal) Code quality and lint pass; eliminate dead code
  - [ ] ⭐ (Stretch Goal) Add security scanning (OWASP dependency check or mvn versions:display-dependency-updates)
  - [ ] Draft presentation outline and live demo script
  - [ ] Done when: security, CI/CD, code review
  - [ ] Evidence: test report, CI badge, code review log
  - [ ] Update journal and time log

---

### Week 13 — Final Touches (Indie Focus) _(Nov 24–30)_
- **Class**
  - [ ] Open lab and Q&A
- **Indie**
  - [ ] Rubric mapping: polish, accessibility, stability
  - [ ] Bug bash; resolve critical/high issues
  - [ ] ⭐ (Stretch Goal) Accessibility and responsive design checks
  - [ ] Final test coverage pass; stabilize configs
  - [ ] Tag release candidate and freeze features
  - [ ] ⭐ (Stretch Goal) Feature freeze, performance passes, repo hygiene
  - [ ] Done when: bug bash, accessibility, freeze
  - [ ] Evidence: test report, screenshots, release tag
  - [ ] Update journal and time log

---

### Week 14 — Final Delivery _(Dec 1–7)_
- **Class**
  - [ ] Final presentations and submissions
- **Indie**
  - [ ] Rubric mapping: delivery, documentation, demo
  - [ ] Release v1.0; publish GitHub release notes/changelog
  - [ ] Create demo video and add video link to `README.md` (rubric requirement)
  - [ ] Finalize documentation and rubric self-check
  - [ ] Final journal/time log entry
  - [ ] Celebrate completing your indie project!
  - [ ] Done when: release, docs, demo
  - [ ] Evidence: release notes, video link, rubric checklist

---
