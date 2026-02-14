# Week 5 – Enterprise Java & CodeForge Progress Overview

## 📅 Enterprise Java – Week 5 Focus

According to the course calendar and project plan:

### 🎓 Class Topics
- Hibernate (continued): deeper ORM work, entity relationships, and queries.
- Web Tier Introduction: JSP, Servlets, JSTL, and request lifecycle.
- DAO integration patterns connecting data access layer to the web layer.
- Deliverable: **Week 5 Exercise (due Sunday, 10/5, 9 p.m.)**.

### 🎯 Learning Objectives
- Understand the servlet lifecycle and how it connects to controller patterns.
- Integrate Hibernate entities and DAOs into servlet-based apps.
- Practice structuring MVC: DAO ↔ Service ↔ Controller ↔ JSP.
- Prepare foundation for authentication and AWS deployment in upcoming weeks.

---

## ✅ Week 5 Progress Checklist

### 🎓 Enterprise Java – Class Tasks
- [ ] Review Hibernate advanced concepts:  
  - Entity relationships (one-to-many, many-to-many).  
  - Queries (HQL, Criteria API).  
- [ ] Practice web tier basics:  
  - JSP syntax and JSTL.  
  - Servlets and controllers.  
  - Request lifecycle (request, response, session, context).  
- [ ] Study DAO integration patterns for connecting data layer ↔ web layer.  
- [ ] Complete **Week 5 Exercise** (due Sunday, 10/5, 9 p.m.).  
- [ ] Take notes on how Hibernate integrates into servlet/JSP apps (important for Checkpoint 2).  

---

### 🔥 CodeForge – Indie Project Tasks
- **DAO & Entities**
  - [x] Add DAO/CRUD for `Submission` entity.  
  - [x] Add DAO/CRUD for `DrillItem` entity.  
  - [x] Write JUnit tests for new DAOs (happy path + edge cases).  

- **Service Layer**
  - [x] Implement service wiring between DAOs and controllers.  
  - [x] Create early `ChallengeRunService` (stub execution for now).  

- **Web Tier (MVP User Stories 1–2)**  
  - [x] Build JSP for **Challenge List** page (pagination, sorting, filter by difficulty).  
  - [x] Build JSP for **Challenge Detail** page.  
  - [x] Controller methods to fetch data via DAO and serve to JSP.  

- **User Experience**
  - [x] Add basic form validation (required fields, unique title).  
  - [x] Add error handling (friendly error JSPs for 404/500; log exceptions).  

- **Testing**
  - [x] Expand tests (WebMvc + Repo) for list, detail 404, create (ok/duplicate), update (ok/duplicate), delete (ok/not-found), and 500 handler.  
  - [x] Verify persistence across multiple entities (`User`, `Challenge`, `Submission`, `DrillItem`).  

- **Documentation**
  - [ ] Journal entry for Week 5 (what worked, what didn’t, hours logged).  
  - [ ] Update **Time Log** with CodeForge + Enterprise Java hours.  
  - [x] Keep ProjectPlan.md current with any shifts in schedule.  

---

### 🏁 Goal by End of Week 5
A learner should be able to:
- View the **Challenge List** with pagination/sorting and filter by difficulty. ✅
- Click into a **Challenge Detail**. ✅
- Submit a solution (processed by a stub run service). ⏩ Deferred to Week 6.

This establishes a working web tier foundation (DAO ↔ Service ↔ Controller ↔ JSP) with validation, error handling, and tests. Submission flow and additional entities (Submission/DrillItem) are planned next.
