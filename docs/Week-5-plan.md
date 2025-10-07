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
  - [ ] Add DAO/CRUD for `Submission` entity.  
  - [ ] Add DAO/CRUD for `DrillItem` entity.  
  - [ ] Write JUnit tests for new DAOs (happy path + edge cases).  

- **Service Layer**
  - [ ] Implement service wiring between DAOs and controllers.  
  - [ ] Create early `ChallengeRunService` (stub execution for now).  

- **Web Tier (MVP User Stories 1–2)**  
  - [ ] Build JSP for **Challenge List** page (with search/filter placeholders).  
  - [ ] Build JSP for **Challenge Detail/Solve** page (prompt, code editor stub, submit button).  
  - [ ] Controller methods to fetch data via DAO and serve to JSP.  

- **User Experience**
  - [ ] Add basic form validation (nulls, required fields).  
  - [ ] Add error handling (friendly error JSP, log actual exceptions).  

- **Testing**
  - [ ] Expand integration tests (controller ↔ service ↔ DAO).  
  - [ ] Verify persistence works across multiple entities (`User`, `Challenge`, `Submission`, `DrillItem`).  

- **Documentation**
  - [ ] Journal entry for Week 5 (what worked, what didn’t, hours logged).  
  - [ ] Update **Time Log** with CodeForge + Enterprise Java hours.  
  - [ ] Keep ProjectPlan.md current with any shifts in schedule.  

---

### 🏁 Goal by End of Week 5
A learner should be able to:
- View the **Challenge List**.
- Click into a **Challenge Detail**.
- Submit a solution (processed by a stub run service).

This establishes a working end-to-end MVP foundation connecting the database, service layer, and JSP interface.
