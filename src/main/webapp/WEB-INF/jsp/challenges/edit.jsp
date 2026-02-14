<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Edit ${challenge.title} | CodeForge" scope="request" />

<!DOCTYPE html>
<html lang="en">
<head>
  <c:import url="/WEB-INF/jsp/head-meta.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>
<c:import url="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
  <section class="cf-page-header">
    <div>
      <h1 class="cf-page-title">Edit Challenge</h1>
      <p class="cf-page-subtitle">
        Updating <span class="cf-mono">#${challenge.id}</span> – ${challenge.title}
      </p>
    </div>

    <a class="cf-link"
       href="${pageContext.request.contextPath}/challenges/${challenge.id}">
      View details
    </a>
  </section>

  <section class="cf-card cf-form-card">
    <form action="${pageContext.request.contextPath}/challenges/${challenge.id}"
          method="post"
          class="cf-form">

      <!-- ID: read-only but posted back -->
      <input type="hidden" name="id" value="${challenge.id}" />

      <div class="cf-form-row">
        <label for="title">Title</label>
        <input id="title"
               name="title"
               type="text"
               required
               value="${challenge.title}" />
      </div>

      <div class="cf-form-row">
        <label for="blurb">Short summary</label>
        <textarea id="blurb"
                  name="blurb"
                  rows="2"
                  placeholder="One–sentence description shown in lists">${challenge.blurb}</textarea>
      </div>

      <div class="cf-form-row cf-form-row-inline">
        <div>
          <label for="difficulty">Difficulty</label>
          <select id="difficulty" name="difficulty" required>
            <option value="EASY"   ${challenge.difficulty == 'EASY'   ? 'selected' : ''}>Easy</option>
            <option value="MEDIUM" ${challenge.difficulty == 'MEDIUM' ? 'selected' : ''}>Medium</option>
            <option value="HARD"   ${challenge.difficulty == 'HARD'   ? 'selected' : ''}>Hard</option>
          </select>
        </div>
      </div>

      <div class="cf-form-row">
        <label for="promptMd">
          Prompt (Markdown)
          <span class="cf-label-hint">Rendered on the detail page</span>
        </label>
        <textarea id="promptMd"
                  name="promptMd"
                  rows="14"
                  placeholder="Describe the problem, inputs, outputs, and example cases using Markdown.">${challenge.promptMd}</textarea>
      </div>

      <div class="cf-form-row">
        <label for="expectedAnswer">Expected answer (MVP)</label>
        <input id="expectedAnswer"
               name="expectedAnswer"
               type="text"
               required
               value="${challenge.expectedAnswer}" />
      </div>

      <div class="cf-form-actions">
        <a class="cf-btn cf-btn-ghost"
           href="${pageContext.request.contextPath}/challenges/${challenge.id}">
          Cancel
        </a>
        <button class="cf-btn cf-btn-primary" type="submit">
          Save changes
        </button>
      </div>
    </form>
  </section>
</main>

<c:import url="/WEB-INF/jsp/footer.jsp" />

</body>
</html>
