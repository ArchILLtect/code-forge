# Class and Indie Project Plan (Weeks 4–14)

**Legend**
- **Class** = Enterprise Java course tasks and milestones
- **Indie** = *CodeForge* project tasks (based on `README.md`, rubric, and roadmap)

---

### Week 4 — Hibernate and Data Model
- **Class**
  - [ ] Lecture/lab: Hibernate mappings, configs, testing patterns, logging
  - [ ] Set up dev and test databases, seed test data
  - [ ] Replace `System.out` with Log4J/SLF4J
- **Indie**
  - [ ] Finalize **Problem Statement** and **confirm MVP stories** (align with rubric)
  - [ ] Finalize initial DB schema from `README.md` challenge domain
  - [ ] Implement entity mappings and one DAO with full CRUD
  - [ ] Create Hibernate configs for dev/test; set up test DB
  - [ ] Unit tests for DAO (happy path and edge cases)
  - [ ] Update project plan, journal, and time log

---

### Week 5 — Web Tier and MVP Foundations
- **Class**
  - [ ] JSP/Servlets, JSTL, controllers, request lifecycle
  - [ ] DAO integration patterns in the web tier
- **Indie**
  - [ ] Implement controllers/services wiring to DAO
  - [ ] Build basic JSPs for listing/detail and form submission
  - [ ] Implement MVP user stories 1–2 (Practice Mode basics)
  - [ ] Add error handling, validation, and simple UX
  - [ ] Expand unit and integration tests

---

### Week 6 — Authentication, Testing, and Logging Quality
- **Class**
  - [ ] Authentication/authorization fundamentals
  - [ ] Unit/integration testing practices and coverage
- **Indie**
  - [ ] Implement authentication/authorization (register/login via identity service)
  - [ ] Secure protected routes and challenge data
  - [ ] Replace **any lingering `System.out.println`** with Log4J logging
  - [ ] Centralize logging with log rotation and diagnostic context
  - [ ] Raise code coverage threshold; add CI/static analysis
  - [ ] Prep for Checkpoint 2 readiness

---

### Week 7 — Checkpoint 2
- **Class**
  - [ ] **Checkpoint 2 due**: DB designed/created; at least one DAO with full CRUD using Hibernate; DAO unit tested; Log4J implemented
- **Indie**
  - [ ] Double-check Checkpoint 2 items are complete and visible in GitHub
  - [ ] Harden DAO and transaction boundaries; add more entities if needed
  - [ ] Journal/time log; README badges for build/coverage

---

### Week 8 — Deployment and External Services
- **Class**
  - [ ] AWS environment and deployment walkthrough
  - [ ] REST/Web Services consumption patterns
- **Indie**
  - [ ] Create project DB on AWS and migrate schema
  - [ ] Update configs for AWS; externalize secrets
  - [ ] Deploy app to AWS; verify health and logs
  - [ ] **Consume at least one external/public API** using Java (rubric requirement)
  - [ ] Add deployed link to indie project list

---

### Week 9 — Checkpoint 3 and Drill Mode
- **Class**
  - [ ] **Checkpoint 3 due**: Deployed to AWS; at least one JSP displays DB data; authentication implemented; deployed link added
- **Indie**
  - [ ] Validate Checkpoint 3 criteria and fix gaps
  - [ ] Implement **Drill Mode persistence**: track outcomes (`Correct`, `Acceptable`, `Incorrect`, `Skipped`)
  - [ ] Build algorithm to cycle skipped/incorrect challenges until completed
  - [ ] Mix solved challenges into queue for re-check (“flashcard” effect)
  - [ ] Monitoring and basic metrics; error log review

---

### Week 10 — Admin Interface and Data Access Depth
- **Class**
  - [ ] Advanced Hibernate: criteria, pagination, N+1 mitigation
  - [ ] Validation and error handling patterns
- **Indie**
  - [ ] Implement **Admin challenge management UI** (create, edit, delete challenges)
  - [ ] Restrict admin routes with authorization rules
  - [ ] Add pagination, sorting, and input validation
  - [ ] Optimize performance: indexes, query tuning, lazy/eager balance
  - [ ] Expand integration tests against test DB

---

### Week 11 — Progress Tracking Dashboard and UX Polish
- **Class**
  - [ ] JSP/UI refinement, accessibility basics, resilient design
- **Indie**
  - [ ] Build **Progress Tracking Dashboard**: number solved, % success, cycle status
  - [ ] Visualize progress (tables, charts, summaries)
  - [ ] Polish UI/UX for implemented flows
  - [ ] Add retries/timeouts for external API calls
  - [ ] Improve empty/error states and form usability
  - [ ] Update docs: architecture, endpoints, and data model diagram

---

### Week 12 — Review, CI/CD, and Hardening
- **Class**
  - [ ] Peer reviews, code reviews, presentation prep
- **Indie**
  - [ ] Address review feedback; refactor risky areas
  - [ ] Security pass: input sanitization, authZ checks
  - [ ] Add **CI/CD pipeline** (GitHub Actions or Maven build for compile + tests)
  - [ ] Backup/restore steps; deployment runbook
  - [ ] Code quality and lint pass; eliminate dead code

---

### Week 13 — Final Touches (Indie Focus)
- **Class**
  - [ ] Open lab and Q&A
- **Indie**
  - [ ] Bug bash; resolve critical/high issues
  - [ ] Accessibility and responsive design checks
  - [ ] Final test coverage pass; stabilize configs
  - [ ] Tag release candidate and freeze features

---

### Week 14 — Final Delivery
- **Class**
  - [ ] Final presentations and submissions
- **Indie**
  - [ ] Release v1.0; publish GitHub release notes/changelog
  - [ ] Create **demo video** and add video link to `README.md` (rubric requirement)
  - [ ] Finalize documentation and rubric self-check
  - [ ] Final journal/time log entry

---