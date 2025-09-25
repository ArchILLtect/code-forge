# CodeForge ⚒️

**CodeForge** is a Java-based coding practice platform that helps students and developers strengthen their algorithm and problem-solving skills.  
Think of it as a simpler, friendlier alternative to LeetCode — focused on clarity, fundamentals, and mastery through repetition.

---

## 🚩 Milestone
- **Project 1 checkpoint complete!**
- App template is running and accessible locally (localhost:8080)
- H2 console enabled
- Challenge entity mapped, Spring Data repository with CRUD, JPA tests passing, Log4J logging (no System.out)
- Next: Add Submission/DrillItem DAOs, wire simple controllers/views, API spike

---

## 🗃️ Project Docs
- ✅ [Problem Statement](docs/problem-statement.md)
- ✅ [User Stories (MVP)](docs/user-stories.md)
- ✅ [Project Plan](docs/project-plan.md)
- ✅ [Screen Designs (Low-Fi)](docs/screen-designs.md)
- ✅ [Reflections](docs/reflections/WeeklyJournal.md)
- ✅ [Time Log](docs/reflections/TimeLog.md)


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

## 📚 Tech Stack
- **Java** (Enterprise Java focus)
- **Servlets/JSP** or **Spring Boot** (depending on class requirements)
- **JUnit** for automated solution testing
- **MySQL/PostgreSQL** for user data and challenge storage
- **IntelliJ IDEA** as the development environment

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
## 🗺️ Roadmap
- [ ] Initial project setup
- [ ] User auth & progress tracking
- [ ] Problem database + admin interface
- [ ] Practice mode (basic submission → test feedback)
- [ ] Drill mode with spaced-repetition queue
- [ ] Polished UI + expanded problem library

---
## 🎫 License
MIT License — feel free to use, fork, and improve CodeForge.

---
## 🙏 Acknowledgments

- Inspired by platforms like LeetCode and HackerRank
- Built for students who want less confusion, more clarity, and stronger fundamentals.

---