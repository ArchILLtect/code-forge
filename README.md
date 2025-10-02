<h2 align="center">CodeForge ⚒️ README</h2>
  
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

- **Drill Mode**  
  A “flashcard for code” system. Missed or skipped problems come back in future cycles until you solve them correctly — with some solved ones mixed in to re-check your mastery.

- **Progress Tracking**
  Track your problem status (`Correct`, `Acceptable`, `Incorrect`, `Skipped`) and see your overall improvement over time.

- **Expandable Challenge Set**  
  Admins/instructors can easily add new problems, test cases, and starter code to grow the platform.

---
## 📚 Tech Stack

The CodeForge project leverages a modern **Enterprise Java** stack alongside supporting tools for development, testing, and deployment.

- ### Backend
  - **Java 17+** — Core language
  - **Spring Boot** — Primary framework for web tier, REST controllers, and DI
  - **JPA / Hibernate** — ORM layer for database persistence
  - **Project Lombok** — Reduces boilerplate (getters, setters, builders, etc.)
  - **Log4J** — Centralized logging framework (replaces `System.out.println`)

- ### Frontend (Server-Side)
  - **JSP / Servlets** — Required for class demonstrations and some views
  - **JSTL** — Tag library for dynamic rendering in JSPs

- ### Database
  - **H2 (local/dev)** — Lightweight in-memory DB for rapid testing
  - **MySQL / PostgreSQL (prod)** — Relational databases for persistence
  - **AWS RDS** — Cloud-hosted DB for deployment

- ### Authentication & Security
  - **AWS Cognito** — Authentication & authorization service (user registration, login, tokens)
  - **Spring Security** (integration) — Protects endpoints and enforces role-based access

- ### Testing
  - **JUnit 5** — Unit and integration testing
  - **Mockito** — (Optional/Stretch) Mocking framework for service/DAO testing
  - **Log4J Test Appenders** — Capture and assert logs during test runs
  - **JaCoCo** — (Optional/Stretch) Test coverage reporting

- ### Build & Deployment
  - **Maven** — Dependency management and build tool
  - **GitHub Actions** — (Optional/Stretch) CI/CD pipeline (build, test, deploy)
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

---
## 🗺️ Roadmap
- [ ] Initial project setup
- [ ] User auth & progress tracking
- [ ] Problem database + admin interface
- [ ] Practice mode (basic submission → test feedback)
- [ ] Drill mode with spaced-repetition queue
- [ ] Polished UI + expanded problem library

---
## 🚩 Milestone
- **Project 1 checkpoint complete!**
- App template is running and accessible locally (localhost:8080)
- H2 console enabled
- Challenge entity mapped, Spring Data repository with CRUD, JPA tests passing, Log4J logging (no System.out)
- Next: Add Submission/DrillItem DAOs, wire simple controllers/views, API spike

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

---
## 🚀 Getting Started
Clone the repository:
```bash
git clone https://github.com/ArchILLtect/code-forge.git
cd code-forge
```
Build and run the project (example with Maven):
```bash
mvn clean install
mvn spring-boot:run
```

Open in your browser at:
```bash
http://localhost:8080
```

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