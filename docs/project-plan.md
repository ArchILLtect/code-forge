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
  - [ ] Lecture/lab: Hibernate mappings, configs, testing patterns, logging
  - [ ] Set up dev and test databases, seed test data
  - [ ] Replace `System.out` with Log4J/SLF4J
- **Indie**
  - [ ] Deliver this week (Checkpoint 1)
    - [ ] Finalize **Problem Statement** and **confirm MVP stories** (align with rubric)
    - [ ] Finalize initial DB schema from `README.md` challenge domain
      - [ ] Schema sketch (initial): User, Challenge, Submission, DrillItem.
      - ⭐[ ] Create ER diagram artifact and baseline DB migrations (Flyway/Liquibase)
    - [ ] Implement entity mappings and one DAO with full CRUD
      - [ ] One entity + DAO + CRUD + tests (recommend: Challenge).
    - [ ] Create Hibernate configs for dev/test; set up test DB
    - Testing:
      - [ ] Unit tests for DAO (happy path and edge cases)
      - [ ] Replace System.out with Log4J.
    - [ ] Update project plan, journal, and time log
    - [ ] ⭐Set up GitHub labels, milestones, issue templates, and weekly journal/time log checklist
    - [ ] Push all changes to GitHub and tag Checkpoint 1
    - [ ] ⭐Add ARCHITECTURE.md and DATA_MODEL.md scaffolds
    - [ ] Rubric mapping: DB schema, DAO CRUD, logging, documentation
    - [ ] Done when: ER diagram, DAO CRUD, tests, log4j, journal, plan, GitHub tag
    - [ ] Evidence: ER diagram, DAO test report, repo tag, screenshots
    - [ ] Update journal and time log

---

### Week 5 — Web Tier and MVP Foundations _(Sept 29–Oct 5)_
- **Class**
  - [ ] JSP/Servlets, JSTL, controllers, request lifecycle
  - [ ] DAO integration patterns in the web tier
- **Indie**
  - [ ] Implement controllers/services wiring to DAO
  - [ ] Add DAO/CRUD for Submission and DrillItem; service layer for Drill logic.
  - [ ] Build basic JSPs for listing/detail and form submission
  - [ ] Implement MVP user stories 1–2 (Practice Mode basics)
  - [ ] Add error handling, validation, and simple UX
  - [ ] Expand unit and integration tests
  - [ ] ⭐Challenge run service (stubbed execution for now; real compile/run later if time).
  - [ ] Spike: shortlist and select external API, prove minimal call
  - [ ] ⭐Add GitHub Actions pipeline for build/tests/static analysis
  - [ ] Rubric mapping: web tier, DAO integration, external API, CI
  - [ ] Done when: controllers, JSPs, API spike, CI pipeline
  - [ ] Evidence: screenshots, test report, API call log, CI badge
  - [ ] Update journal and time log

---

### Week 6 — Authentication, Testing, and Logging Quality _(Oct 6–12)_
- **Class**
  - [ ] Authentication/authorization fundamentals
  - [ ] Unit/integration testing practices and coverage
- **Indie**
  - [ ] Implement authentication/authorization (register/login via identity service)
  - [ ] Secure protected routes and challenge data
  - [ ] Replace any lingering `System.out.println` with Log4J logging
  - [ ] ⭐Centralize logging with log rotation and diagnostic context
  - [ ] ⭐Raise code coverage threshold; add CI/static analysis
  - [ ] Prep for Checkpoint 2 readiness
  - [ ] Rubric mapping: authentication, logging, test coverage
  - [ ] Done when: login/register, protected routes, log4j, CI
  - [ ] Evidence: test report, screenshots, CI badge
  - [ ] Update journal and time log

---

### Week 7 — Checkpoint 2 _(Oct 13–19)_
- **Class**
  - [ ] **Checkpoint 2 due**: DB designed/created; at least one DAO with full CRUD using Hibernate; DAO unit tested; Log4J implemented
- **Indie**
  - [ ] Double-check Checkpoint 2 items are complete and visible in GitHub
  - [ ] Harden DAO and transaction boundaries; add more entities if needed
  - [ ] Journal/time log; README badges for build/coverage
  - [ ] Rubric mapping: DB, DAO, logging, test coverage
  - [ ] Done when: all checkpoint items in repo
  - [ ] Evidence: repo tag, screenshots, test report
  - [ ] Update journal and time log

---

### Week 8 — Deployment and External Services _(Oct 20–26)_
- **Class**
  - [ ] AWS environment and deployment walkthrough
  - [ ] REST/Web Services consumption patterns
- **Indie**
  - [ ] Create project DB on AWS and migrate schema (Flyway/Liquibase)
  - [ ] Update configs for AWS; externalize secrets
  - [ ] Deploy app to AWS; verify health and logs
  - [ ] Consume at least one external/public API using Java (rubric requirement)
  - [ ] Add deployed link to indie project list
  - [ ] ⭐Document API contract and rate limits
  - [ ] ⭐Add health endpoints and structured logs
  - [ ] ⭐Switch to structured JSON logs
  - [ ] Rubric mapping: deployment, external API, logging
  - [ ] Done when: deployed, API integrated, health/logs
  - [ ] Evidence: deployed URL, API call log, screenshots
  - [ ] Update journal and time log

---

### Week 9 — Checkpoint 3 and Drill Mode _(Oct 27–Nov 2)_
- **Class**
  - [ ] **Checkpoint 3 due**: Deployed to AWS; at least one JSP displays DB data; authentication implemented; deployed link added
- **Indie**
  - [ ] Validate Checkpoint 3 criteria and fix gaps
  - [ ] Implement Drill Mode persistence: track outcomes (`Correct`, `Acceptable`, `Incorrect`, `Skipped`)
  - [ ] Build algorithm to cycle skipped/incorrect challenges until completed
  - [ ] ⭐Mix solved challenges into queue for re-check (“flashcard” effect)
  - [ ] ⭐Monitoring and basic metrics; error log review
  - [ ] Rubric mapping: Drill Mode, persistence, metrics
  - [ ] Done when: Drill Mode works, metrics visible
  - [ ] Evidence: screenshots, test report, metrics log
  - [ ] Update journal and time log

---

### Week 10 — Admin Interface and Data Access Depth _(Nov 3–9)_
- **Class**
  - [ ] Advanced Hibernate: criteria, pagination, N+1 mitigation
  - [ ] Validation and error handling patterns
- **Indie**
  - [ ] Implement Admin challenge management UI (create, edit, delete challenges)
  - [ ] Restrict admin routes with authorization rules (role-based access)
  - [ ] ⭐Add pagination, sorting, and input validation
  - [ ] ⭐Optimize performance: indexes, query tuning, lazy/eager balance
  - [ ] Expand integration tests against test DB
  - [ ] Rubric mapping: admin UI, data access, performance
  - [ ] Done when: admin UI, role-based access, perf
  - [ ] Evidence: screenshots, test report
  - [ ] Update journal and time log

---

### Week 11 — Progress Tracking Dashboard and UX Polish _(Nov 10–16)_
- **Class**
  - [ ] JSP/UI refinement, accessibility basics, resilient design
- **Indie**
  - [ ] Build Progress Tracking Dashboard: number solved, % success, cycle status
  - [ ] ⭐Visualize progress (tables, charts, summaries)
  - [ ] Polish UI/UX for implemented flows
  - [ ] ⭐Add retries/timeouts for external API calls
  - [ ] Improve empty/error states and form usability
  - [ ] ⭐Update docs: architecture, endpoints, and data model diagram
  - [ ] Add progress visualizations with simple charts and pagination for history
  - [ ] Rubric mapping: dashboard, UX, API resilience
  - [ ] Done when: dashboard, charts, polished UX
  - [ ] Evidence: screenshots, test report
  - [ ] Update journal and time log

---

### Week 12 — Review, CI/CD, and Hardening _(Nov 17–23)_
- **Class**
  - [ ] Peer reviews, code reviews, presentation prep
- **Indie**
  - [ ] Address review feedback; refactor risky areas
  - [ ] Security pass: input sanitization, authZ checks
  - [ ] ⭐Add CI/CD pipeline (GitHub Actions or Maven build for compile + tests)
  - [ ] ⭐Backup/restore steps; deployment runbook
  - [ ] ⭐Code quality and lint pass; eliminate dead code
  - [ ] ⭐Add security scanning (OWASP dependency check or mvn versions:display-dependency-updates)
  - [ ] Draft presentation outline and live demo script
  - [ ] Rubric mapping: security, CI/CD, code quality
  - [ ] Done when: security, CI/CD, code review
  - [ ] Evidence: test report, CI badge, code review log
  - [ ] Update journal and time log

---

### Week 13 — Final Touches (Indie Focus) _(Nov 24–30)_
- **Class**
  - [ ] Open lab and Q&A
- **Indie**
  - [ ] Bug bash; resolve critical/high issues
  - [ ] ⭐Accessibility and responsive design checks
  - [ ] Final test coverage pass; stabilize configs
  - [ ] Tag release candidate and freeze features
  - [ ] ⭐Feature freeze, performance passes, repo hygiene
  - [ ] Rubric mapping: polish, accessibility, stability
  - [ ] Done when: bug bash, accessibility, freeze
  - [ ] Evidence: test report, screenshots, release tag
  - [ ] Update journal and time log

---

### Week 14 — Final Delivery _(Dec 1–7)_
- **Class**
  - [ ] Final presentations and submissions
- **Indie**
  - [ ] Release v1.0; publish GitHub release notes/changelog
  - [ ] Create demo video and add video link to `README.md` (rubric requirement)
  - [ ] Finalize documentation and rubric self-check
  - [ ] Final journal/time log entry
  - [ ] Celebrate completing your indie project!
  - [ ] Rubric mapping: delivery, documentation, demo
  - [ ] Done when: release, docs, demo
  - [ ] Evidence: release notes, video link, rubric checklist

---

