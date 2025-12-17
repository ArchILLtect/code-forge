<h2 align="center">CodeForge ⚒️ README</h2>

<div align="center">
  <a href="https://github.com/ArchILLtect/code-forge/actions/workflows/ci.yml">
    <img alt="CI" src="https://github.com/ArchILLtect/code-forge/actions/workflows/build-and-package.yml/badge.svg">
  </a>
  <img alt="Java" src="https://img.shields.io/badge/Java-17-007396?logo=java&logoColor=white">
  <img alt="Servlets/JSP" src="https://img.shields.io/badge/Servlets%2FJSP-Tomcat_9-4CAF50">
  <img alt="Hibernate" src="https://img.shields.io/badge/Hibernate-6.x-59666C?logo=hibernate&logoColor=white">
  <img alt="Maven" src="https://img.shields.io/badge/Maven-3%2B-C71A36?logo=apachemaven&logoColor=white">
  <a href="https://opensource.org/licenses/MIT">
    <img alt="License" src="https://img.shields.io/badge/License-MIT-blue">
  </a>
</div>


Think of it as a simpler, friendlier alternative to LeetCode — focused on clarity, fundamentals, and mastery through repetition.

---
### Problem Statement
Most coding-practice platforms (like LeetCode or HackerRank) are powerful, but they aren’t always friendly to beginners. They assume too much context, provide cryptic feedback, and emphasize leaderboards or ads over clarity. They also push learners to focus heavily on **time/space complexity metrics** and competitive performance scoring. While important for advanced preparation, these distractions can overwhelm beginners who are still working to master syntax, logic, and step-by-step problem-solving. This creates frustration and discourages learners who want *fundamentals-first* growth.

**CodeForge** is a Java-based coding practice platform that focuses on clarity, repetition, and approachability. It’s designed for students and developers to help them strengthen their algorithm and problem-solving skills through the following core features:
- **Practice Mode**: straightforward challenges with clean prompts and examples.
- **Drill Mode**: a “flashcard for code” loop — missed/skipped problems reappear until solved.
- **Progress Tracking**: history per challenge and overall streaks to visualize learning.
- **Instructor/Admin Tools**: easy challenge CRUD to keep content fresh.

CodeForge’s goal is to provide a *friendlier, clarity-first alternative* to existing platforms — helping learners master problem-solving without distraction.

---
## ✨ Features (Planned / In Progress)
- **Practice Mode**
  Browse algorithm challenges by topic and difficulty, write your solution, and get instant test feedback.
  - Early stub runner implemented (ChallengeRunService) for Week 5 to support upcoming submit flow.

- **Drill Mode**
  A “flashcard for code” system. Missed or skipped problems come back in future cycles until you solve them correctly — with some solved ones mixed in to re-check your mastery.
  - Week 7: Drill UI + controller + submit flow wired end-to-end (Issues 27 & 28). Queue page, next redirect, solve page, and submit now work; outcomes persist and scheduling updates.

- **Progress Tracking**
  Track your problem status (`Correct`, `Acceptable`, `Incorrect`, `Skipped`) and see your overall improvement over time.

- **Expandable Challenge Set**
  Admins/instructors can easily add new problems, test cases, and starter code to grow the platform.

- **Admin Tools (Week 5)**
  Basic Challenge management (Create/Edit/Delete), list pagination/sorting, filter by difficulty, validation and friendly error pages (404/500).

---
## 📚 Tech Stack (Current)

- ### Backend
  - **Java 17 (LTS)** — Core language
  - Servlets + JSP (Tomcat 9)
  - Hibernate 6.x (JPA ORM)
  - **Project Lombok** — Reduces boilerplate (getters, setters, builders, etc.)
  - Log4j2 for application logging
  - DAO layer with SessionFactoryProvider

- ### Frontend (Server-Side + Client Enhancements)
  - JSP + JSTL for views
  - Monaco Editor (CDN) for code editing in Drill/Practice pages
  - Minimal vanilla JS for editor sync, hint toggle, and flash messages
  - Lightweight CSS (“CodeForge UI”) without a large framework

- ### Database
  - MySQL (local/dev tests and prod)
  - Test DB reset via `DbReset` + `cleandb.sql`
  - Seed data managed in `src/test/resources/cleandb.sql` (predictable schema)
  - **AWS RDS** — Cloud-hosted DB for deployment

- ### Authentication & Security
  - Amazon Cognito Hosted UI (servlet-based flow)
  - AuthGuardFilter gate for Drill and admin routes
  - Public Practice routes (GET/POST) with evaluator feedback, no persistence
  - ID token validation (JWKS, RSA256) and HTTP session storage of the user

- ### Evaluator (MVP)
  - Evaluator scaffold + ChallengeRunService (non-executing heuristics in MVP)
  - Expected answer compare and outcome mapping (CORRECT/ACCEPTABLE/etc.)
  - Timeout/memory guard planned for local runner

- ### Testing
  - JUnit 5 test suite
  - Mockito for unit tests and servlet/filter behavior stubbing
  - DbReset single-source DB reset strategy
  - Surefire plugin 3.2.x
  - **JaCoCo** — (Optional/Stretch) Test coverage reporting

- ### Build & Deployment
  - Maven (3.9.x)
  - WAR packaging
  - GitHub Actions for CI (build + test + artifact upload)
  - **AWS (Elastic Beanstalk / EC2)** — Hosting & deployment
  - **Docker** (Stretch) — Containerized environment

- ### Development Environment
  - **IntelliJ IDEA** — Primary IDE
  - **Git & GitHub** — Version control and collaboration
  - **Postman / cURL** — API testing
  - **Draw.io / Mermaid** — (Optional/Stretch) Diagrams and architecture sketches

---

## 🗃️ Project Docs
- ✅ [Problem Statement](docs/problem-statement.md)
- ✅ [User Stories (MVP)](docs/user-stories.md)
- ✅ [Project Plan](docs/project-plan.md)
- ✅ [Screen Designs (Low-Fi)](docs/screen-designs.md)
- ✅ [Reflections](docs/reflections/WeeklyJournal.md)
- ✅ [Time Log](docs/reflections/TimeLog.md)
- ✅ ER Diagram (ERD): [docs/screenshots/CodeForgeERD.png](docs/screenshots/CodeForgeERD.png)
- ✅ Deployment runbook: [docs/deployment.md](docs/deployment.md)

---
## 🗺️ Roadmap
- [x] Initial project setup
- [x] User auth & progress tracking
- [ ] Problem database + admin interface
- [ ] Practice mode (basic submission → test feedback)
- [ ] Drill mode with spaced-repetition queue
- [ ] Polished UI + expanded problem library

---
## 🚩 Milestone
- **Project 1 checkpoint complete!**
- App template is running and accessible locally (localhost:5000)
- H2 console enabled
- Challenge entity mapped, Spring Data repository with CRUD, JPA tests passing, Log4J logging (no System.out)
- Week 5: Challenge Admin CRUD (Create/Edit/Delete), list pagination/sorting and difficulty filtering, JSP error pages (404/500) via @ControllerAdvice, lightweight service layer, expanded seed data, and WebMvc + JPA tests all green.
- Week 5: Added DrillService for scheduling (nextDueAt), streak/timesSeen updates, and a due queue; dedicated service tests added and passing.
- Week 5: Added stub ChallengeRunService to simulate solution execution and unit tests.
- Week 7: Implemented Drill submission flow (Issue 27) and protected Drill routes via AuthGuardFilter (Issue 28). Added unit tests for filter coverage and run heuristics.

---
## ℹ️ Drill Scheduling (v1)
The backend DrillService applies simple spaced-repetition-style rules when recording outcomes and when building the due queue.

- recordOutcome(challengeId, outcome, code)
  - Updates DrillItem: timesSeen++, streak changes based on outcome, sets nextDueAt.
  - Creates a Submission audit row.
- Scheduling rules:
  - CORRECT / ACCEPTABLE: increment streak; schedule by new streak
    - streak 1 → +30 minutes
    - streak 2 → +1 day
    - streak 3 → +3 days
    - streak ≥ 4 → +7 days
  - INCORRECT: streak = 0; nextDueAt = now + 5 minutes
  - SKIPPED: streak unchanged; nextDueAt = now + 10 minutes
- getDueQueue(limit):
  - Returns items due now (or unscheduled) sorted with null nextDueAt first, then past due by time.
  - If none are due, returns the single soonest upcoming item to avoid an empty queue.

This powers the Drill mode UI.

---
## 💪 Challenge Topics
CodeForge will include challenges from:
- Arrays & Strings
- HashMaps & Sets
- Stacks & Queues
- Recursion & Backtracking
- Sorting & Searching
- Linked Lists & Trees (basics → moderate)
- Introductory Dynamic Programming

# 🚀 Getting Started

## 1️⃣ Clone the repository
```bash
git clone https://github.com/ArchILLtect/code-forge.git
cd code-forge
```

---

## 🔐 Runtime configuration (local development)

### Cognito client secret (required for login)

The Cognito client secret **must NOT be committed**.  
Set it via environment variable or JVM system property.

**Windows (PowerShell):**
```powershell
$env:COGNITO_CLIENT_SECRET="your_client_secret"
```

**Windows (cmd.exe):**
```cmd
set COGNITO_CLIENT_SECRET=your_client_secret
```

**macOS / Linux (bash):**
```bash
export COGNITO_CLIENT_SECRET=your_client_secret
```

Non-secret Cognito values (user pool ID, region, etc.) live in:
```
src/main/resources/cognito.properties
```

---

## 🧪 Test database setup (required to run tests)

Tests use a **local MySQL test database** and are intentionally isolated from production data.

### 2️⃣ Create a local MySQL test database
```sql
CREATE DATABASE cf_test_db;
```

Ensure MySQL is running locally.

---

### 3️⃣ Create `src/test/resources/test-db.properties` (untracked)

Create the file:
```
src/test/resources/test-db.properties
```

Example contents:
```properties
# JDBC for local test database (untracked)
hibernate.connection.url=jdbc:mysql://localhost:3306/cf_test_db?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
hibernate.connection.driver_class=com.mysql.cj.jdbc.Driver
hibernate.connection.username=<your_username>
hibernate.connection.password=<your_password>
hibernate.dialect=org.hibernate.dialect.MySQLDialect

# Connection pool (local defaults)
hibernate.c3p0.min_size=1
hibernate.c3p0.max_size=10
hibernate.c3p0.timeout=300
hibernate.c3p0.max_statements=50
hibernate.c3p0.idle_test_period=3000

hibernate.show_sql=false
hibernate.hbm2ddl.auto=none
```

> **Important**
> - Do NOT commit this file.
> - Each contributor must create their own local test DB and properties file.

---

### 4️⃣ How test DB reset works

- `DbReset` runs before each test class.
- It calls `Database.runSQL("cleandb.sql")`.
- `cleandb.sql` lives in `src/test/resources/`.
- Tests always start from a clean, predictable state.

---

## ▶️ Run tests
```bash
mvn -q -DskipTests=false test
```

---

## 🛠 Build & run the application

### Build the WAR
```bash
mvn clean package
```

### Deploy to Tomcat 9
- Copy `target/codeforge.war` into Tomcat’s `webapps/` directory
- Start Tomcat
- Open:
```
http://localhost:8080/codeforge/
```

---

## ⚠️ Notes on environments

- Production credentials are provided via environment variables (Elastic Beanstalk).
- Local development may temporarily point to production DBs for demo purposes.
- Tests always use the local test DB defined in `test-db.properties` and never touch production data.

---

## Build the WAR:

- Windows cmd.exe
```
cmd /c 'cd /d "C:\Users\nickh\Documents\My Projects\Java\code-forge" && mvn -q -DskipTests=false clean package'
```

Deploy to Tomcat 9:
- Copy `target/codeforge.war` into your Tomcat 9 `webapps/` directory (rename to `codeforge.war` if needed)
- Start Tomcat and open: `http://localhost:8080/codeforge/`

Required runtime configuration:
- Cognito client secret must be provided via environment or system property (do NOT commit secrets):
  - `COGNITO_CLIENT_SECRET` (environment variable)
  - or `-DCOGNITO_CLIENT_SECRET=...` (Java system property)
- Non-secret Cognito values live in `src/main/resources/cognito.properties`

---

## Configuration required by QuoteService
QuoteService loads settings from `src/main/resources/application.properties` at runtime.

Required keys:
- `quote.api.url` — endpoint URL returning JSON array of quotes compatible with ZenQuotes shape.
- `quote.timeout.seconds` — HTTP connect timeout in seconds (default 5 if missing).

Example `application.properties` (already present):
```
quote.api.url=https://zenquotes.io/api/random
quote.timeout.seconds=5
```

JSON → POJO mapping:
- Response elements map to: `record ApiResponse(String q, String a, String h) {}`
- Home page invokes `QuoteService#getRandomQuote()` and forwards the `quote` to the JSP

---

## 🧪 Stub Run Service (Heuristics)
The early runner does not compile or execute code. It deterministically simulates outcomes to unblock UI wiring. Heuristics (order matters):
- Unsupported or blank language → outcome=SKIPPED
- Empty/blank code → outcome=SKIPPED
- Code contains "skip" (case-insensitive) → outcome=SKIPPED
- Code contains "fail" or "assert false" → outcome=INCORRECT
- Code contains "// correct" or "// pass" → outcome=CORRECT
- Code contains "// ok" → outcome=ACCEPTABLE
- Otherwise → outcome=INCORRECT

---
## ⚠️ Project Purpose
This project is part of my Enterprise Java class.
The goal is to demonstrate building a full-stack Java web app with authentication, persistence, and real-world application value — while creating a tool that can actually help practice coding interviews.

---
## 🎫 License
MIT License — feel free to use, fork, and improve CodeForge.

---
## 🙏 Acknowledgments

- Inspired by platforms like LeetCode and HackerRank
- Built for students who want less confusion, more clarity, and stronger fundamentals.

---
## Authentication status (MVP)
- For MVP/class requirements, the app uses a servlet-based Cognito login flow (no Spring Security OIDC yet).
- The Cognito ID token is verified, and the authenticated user is stored in the HTTP session; controllers/JSPs read the session user.
- Route protection (MVP): `AuthGuardFilter` enforces login for challenge create/edit/delete and for all Drill routes (GET /drill, /drill/*, POST /drill/*/submit), redirecting unauthenticated users to `/logIn`.

### Environment Variables – Servlet-based Cognito
Set your Cognito client secret via an environment variable (do not commit it to source control). Non-secret values remain in `src/main/resources/cognito.properties`.

Required env var:
- `COGNITO_CLIENT_SECRET` — your Cognito App Client secret

Used properties (non-secret):
- `client.id`, `oAuthURL`, `loginURL`, `logoutURL`, `redirectURL.*`, `logoutRedirectURL.*`, `region`, `pool.id` (in `cognito.properties`)

Example (Windows cmd.exe):
```
set COGNITO_CLIENT_SECRET=your_client_secret
mvn spring-boot:run
```

On login, the app redirects to the Cognito Hosted UI; on callback (`/auth`) it exchanges the code for tokens using your `client.id` and `COGNITO_CLIENT_SECRET`, validates the ID token, stores the user in the session, and redirects to `/me`.

### Pagination status (MVP)
- Client-side pagination is implemented with jQuery DataTables via CDN on the challenges list.

---
## Week 7 — Status (Drill UI + Security)
- Drill submission flow implemented: queue, next redirect, solve page, submit; outcomes persisted and scheduling updated (Issue 27).
- Drill routes protected via AuthGuardFilter; unauthenticated users are redirected to `/logIn` (Issue 28). Added unit tests covering these paths.
- Local tests are green; CI pipeline remains a stretch goal for a later week.

---
## Week 6 — Status (Auth + Protected Routes + Pagination + Deploy)
- Authentication (MVP): Cognito Hosted UI login via servlet flow is live; ID token verified; user stored in HTTP session; /me displays user.
- Route protection: AuthGuardFilter enforces login for challenge create/edit/delete and edit form. JSP UI now hides Create/Edit/Delete when logged out.
- Pagination: Client-side via jQuery DataTables on the challenges list (no server-side Pageable).
- CI packaging: GitHub Actions workflow (`.github/workflows/ci.yml`) builds and uploads the WAR artifact. Latest run is green.
- Deployment: WAR deployed to Elastic Beanstalk Tomcat (Corretto 17). `/actuator/health` reports UP.
- Secret hygiene: Cognito client secret set via EB environment variable for now; a follow-up issue tracks moving to SSM/Secrets Manager.
- Deferred to Week 7: Drill submission UI/flow wiring to create Submissions and update DrillItem.

Evidence (screenshots/artifacts):
- Login flow success (placeholder): `docs/screenshots/week6-auth-login.png`
- Challenges list with client-side pagination (placeholder): `docs/screenshots/week6-challenges-list.png`
- EB health check UP (placeholder): `docs/screenshots/week6-eb-health-up.png`
- GitHub Actions artifact (WAR) — see latest CI run in Actions tab

---
## Week 6 Scope (Auth + Hosting + Drill UI)
- Authentication & Authorization: (Deferred) Spring Security OIDC route protection. For MVP, servlet-based Cognito login + session persistence is implemented.
- Drill Mode: Wire submit flow and UI to the existing Drill scheduling logic and stub ChallengeRunService. (pending)
- Pagination: Remove server-side Pageable and use jQuery pagination via CDN on listing pages. (done)
- Deployment (Elastic Beanstalk): Build a WAR and deploy to an EB Tomcat platform (Corretto 17). Verify /actuator/health returns UP. No Procfile required.

### Deployment Notes (Elastic Beanstalk)
- Platform: Elastic Beanstalk Tomcat (e.g., Tomcat running on Corretto 17).
- Artifact: Use the built WAR (e.g., target/codeforge-0.0.1-SNAPSHOT.war).
- CI: The CI workflow uploads the WAR as an artifact for manual deploys.
- Health: Ensure Spring Boot Actuator is on the classpath and /actuator/health returns UP.
- Local: The app defaults to port 5000 locally (server.port in application.yml); EB Tomcat manages its own port.

### Environment Variables – Cognito OIDC (deferred)
The Spring Security OIDC variables remain documented here for the future migration, but are not used in the current MVP.

```
# Spring Security OIDC (Cognito)
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_COGNITO_CLIENT_ID=your_client_id
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_COGNITO_CLIENT_SECRET=your_client_secret
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_COGNITO_SCOPE=openid,profile,email
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_COGNITO_REDIRECT_URI={baseUrl}/login/oauth2/code/cognito
SPRING_SECURITY_OAUTH2_CLIENT_REGISTRATION_COGNITO_AUTHORIZATION_GRANT_TYPE=authorization_code
SPRING_SECURITY_OAUTH2_CLIENT_PROVIDER_COGNITO_ISSUER_URI=https://cognito-idp.<REGION>.amazonaws.com/<USER_POOL_ID>

# Optional: Spring Boot server port fallback if running outside EB Tomcat
SERVER_PORT=5000
```

Notes:
- issuer-uri uses your AWS region and user pool id (e.g., https://cognito-idp.us-east-1.amazonaws.com/us-east-1_abc123).
- `{baseUrl}` is a Spring Security placeholder that will be automatically resolved at runtime to your application's base URL (e.g., http://localhost:5000). You do **not** need to replace it manually.
- Environment variables override application.yml properties in Spring Boot.

---
# Production setup (Elastic Beanstalk)

Use a Tomcat platform (Corretto 17) and the built WAR artifact. You must provide environment variables for production:

Required EB Environment properties:
- APP_ENV=prod
- COGNITO_CLIENT_SECRET=your_prod_cognito_client_secret

EB Console steps:
1) Elastic Beanstalk → Environments → your environment → Configuration → Software → Edit.
2) Under Environment properties add:
   - Name: APP_ENV, Value: prod
   - Name: COGNITO_CLIENT_SECRET, Value: <your secret>
3) Save. EB will restart the app.

Better secret handling (recommended):
- Instead of Plain text, store the secret in AWS Secrets Manager or SSM Parameter Store and select that as the Source.
- Ensure the EB instance role has permission to read the secret/parameter.
- Rotate the Cognito App Client secret if it was ever shared inadvertently.

Verification:
- Visit /actuator/health (should be UP).
- Start login from the home page; on success you should land on /me with your user.

---

# Local environment quickstart

We provide a `.env.example` you can copy and fill for local development. Spring Boot doesn’t autoload `.env`—use your shell/IDE to export variables or an EnvFile/direnv plugin.

1) Copy and edit the example
```
copy .env.example .env   # Windows cmd
# or
cp .env.example .env     # bash
```
Set your values in `.env` (do NOT commit this file).

2) Set env vars and run locally
- Windows cmd.exe
```
set COGNITO_CLIENT_SECRET=your_secret
set APP_ENV=dev
mvn spring-boot:run
```
- PowerShell
```
$env:COGNITO_CLIENT_SECRET="your_secret"
$env:APP_ENV="dev"
mvn spring-boot:run
```
- bash
```
export COGNITO_CLIENT_SECRET=your_secret
export APP_ENV=dev
mvn spring-boot:run
```
- IntelliJ IDEA
  - Run/Debug Configurations → Environment → Environment variables:
    - COGNITO_CLIENT_SECRET=your_secret; APP_ENV=dev

Notes:
- Non-secret Cognito values (client.id, URLs, region, pool id, redirect URLs) are in `src/main/resources/cognito.properties`.
- The Cognito client secret is only read from the environment (`COGNITO_CLIENT_SECRET`).
- For team collaboration, share secrets via a password manager or use separate per-developer Cognito App Clients for dev.

---
# CodeForge – Servlet/JSP + Hibernate (MVP)

This is the initial project setup for CodeForge, demonstrating a full-stack Java web app with a focus on coding practice challenges.

## Configuration required by QuoteService (non‑Spring)
QuoteService loads settings from `src/main/resources/application.properties` at runtime.

Required keys:
- `quote.api.url` — endpoint URL returning JSON array of quotes compatible with ZenQuotes shape.
- `quote.timeout.seconds` — HTTP connect timeout in seconds (default 5 if missing).

Example `application.properties` (already committed with sane defaults):
```
quote.api.url=https://zenquotes.io/api/random
quote.timeout.seconds=5
```

Response mapping (JSON → POJO):
The service maps each element to a Java record:
```
record ApiResponse(String q, String a, String h) {}
```
The endpoint returns an array like:
```
[ { "q": "Quote text", "a": "Author", "h": "<blockquote>…</blockquote>" } ]
```
The home page uses `HomeServlet` to call `QuoteService#getRandomQuote()` and forwards the `quote` string to `WEB-INF/jsp/home.jsp`.

## Running locally on Tomcat 9
- Build the WAR and deploy to Tomcat 9.
- Ensure `application.properties` is on the classpath (it is, under `src/main/resources`).
- If outbound HTTP is blocked, the home page will show a fallback quote.

---

# POST-MVP:

##  S3 Asset Storage

CodeForge stores static assets (images, JSON files, etc.) in an S3 bucket rather than bundling them inside the WAR.

- ### Why?
  - Faster deployments (no need to redeploy for content updates)
  - Versioning support (rollback assets independently)
  - Lower cost & cleaner architecture

- ### Bucket Structure
```
  codeforge-assets.nickhanson.me/
  ├── dev/
  │     └── images/
  └── prod/
        └── images/
```

- ### Public Access Policy
  Only specific prefixes are public:

```json
  {
    "Effect": "Allow",
    "Principal": "*",
    "Action": "s3:GetObject",
    "Resource": "arn:aws:s3:::codeforge-assets.nickhanson.me/prod/images/*"
  }
```

- ### Java Access Helper

  The app loads assets depending on environment:

  `AssetConfig.image("banner.png");`

- ### JSP Usage
  `<img src="${cf:asset('banner.png')}" />`
