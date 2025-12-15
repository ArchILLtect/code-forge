<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:30 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="New Challenge | CodeForge" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <%@ include file="/WEB-INF/jsp/head-meta.jspf" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
  <section class="cf-page-header">
    <div>
      <h1 class="cf-page-title">New Challenge</h1>
      <p class="cf-page-subtitle">
        Add a new coding exercise to the CodeForge library.
      </p>
    </div>

    <a class="cf-link"
       href="${pageContext.request.contextPath}/challenges">
      ← Back to challenges
    </a>
  </section>

  <section class="cf-card cf-form-card">
    <form action="${pageContext.request.contextPath}/challenges"
          method="post"
          class="cf-form">

      <div class="cf-form-row">
        <label for="title">Title</label>
        <input id="title"
               name="title"
               type="text"
               required
               placeholder="Two Sum, FizzBuzz, SQL Joins, …" />
      </div>

      <div class="cf-form-row">
        <label for="blurb">Short summary</label>
        <textarea id="blurb"
                  name="blurb"
                  rows="2"
                  placeholder="One–sentence description for the list view"></textarea>
      </div>

      <div class="cf-form-row cf-form-row-inline">
        <div>
          <label for="difficulty">Difficulty</label>
          <select id="difficulty" name="difficulty" required>
            <option value="EASY">Easy</option>
            <option value="MEDIUM">Medium</option>
            <option value="HARD">Hard</option>
          </select>
        </div>
      </div>

      <div class="cf-form-row">
        <label for="promptMd">
          Prompt (Markdown)
          <span class="cf-label-hint">
            Describe the problem, inputs, outputs, and example cases.
          </span>
        </label>
        <textarea id="promptMd"
                  name="promptMd"
                  rows="14"></textarea>
      </div>

      <div class="cf-form-row">
        <label for="expectedAnswer">Expected answer (MVP)</label>
        <input id="expectedAnswer"
               name="expectedAnswer"
               type="text"
               required
               placeholder="e.g., twoSum" />
      </div>

      <div class="cf-form-actions">
        <a class="cf-btn cf-btn-ghost"
           href="${pageContext.request.contextPath}/challenges">
          Cancel
        </a>
        <button class="cf-btn cf-btn-primary" type="submit">
          Create challenge
        </button>
      </div>
    </form>
  </section>
</main>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />

</body>
</html>