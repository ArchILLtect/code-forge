<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="${challenge.title} Details | CodeForge" scope="request" />

<!DOCTYPE html>
<html lang="en">
<head>
  <c:import url="/WEB-INF/jsp/head-meta.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>

<c:import url="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
  <section class="cf-page-header">
    <div>
      <h1 class="cf-page-title">
        ${challenge.title}
      </h1>
      <p class="cf-page-subtitle">
        Challenge <span class="cf-mono">#${challenge.id}</span>
      </p>
    </div>

    <a class="cf-btn cf-btn-primary"
       href="${pageContext.request.contextPath}/practice/${challenge.id}">
      Practice
    </a>

    <div class="cf-page-header-meta">
      <span class="cf-pill cf-pill-difficulty-${challenge.difficulty}">
        ${challenge.difficulty}
      </span>

      <span class="cf-pill cf-pill-neutral">
          <c:out value="${challenge.blurb}"/>
      </span>

      <a class="cf-btn cf-btn-secondary"
         href="${pageContext.request.contextPath}/challenges/${challenge.id}/edit">
        Edit
      </a>
    </div>
  </section>

  <section class="cf-grid cf-grid-2">
    <article class="cf-card">
      <h2 class="cf-section-title">Description</h2>
      <c:if test="${not empty challenge.blurb}">
        <p class="cf-muted"><c:out value="${challenge.blurb}"/></p>
        <hr class="cf-divider-horizontal" />
      </c:if>

      <!-- Escape Markdown source to avoid raw HTML injection; render as plain text for MVP -->
      <pre class="drill-prompt-text">
<c:out value="${challenge.promptMd}" />
      </pre>
    </article>

    <aside class="cf-card cf-meta-card">
      <h2 class="cf-section-title">Meta</h2>
      <dl class="cf-meta-list">
        <div>
          <dt>Difficulty</dt>
          <dd>${challenge.difficulty}</dd>
        </div>
        <div>
          <dt>Created</dt>
          <dd><c:out value="${challenge.createdAt}"/></dd>
        </div>
      </dl>

      <div class="cf-meta-actions">
        <a class="cf-link"
           href="${pageContext.request.contextPath}/challenges">
          ← Back to list
        </a>
      </div>
    </aside>
  </section>
</main>

<c:import url="/WEB-INF/jsp/footer.jsp" />

</body>
</html>