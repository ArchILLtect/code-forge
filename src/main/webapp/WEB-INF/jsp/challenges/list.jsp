<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:30 PM
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Challenges | CodeForge" scope="request" />

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
      <h1 class="cf-page-title">Challenges</h1>
      <p class="cf-page-subtitle">
        Browse, review, and refine coding challenges in the CodeForge library.
      </p>
    </div>

    <a class="cf-btn cf-btn-primary"
       href="${pageContext.request.contextPath}/challenges/new">
      + New Challenge
    </a>
  </section>

  <!-- Flash messages -->
  <c:if test="${not empty flash}">
    <div class="cf-alert cf-alert-success">
        ${flash}
    </div>
  </c:if>

  <c:choose>
    <c:when test="${empty challenges}">
      <div class="cf-empty-state">
        <h2>No challenges yet</h2>
        <p>Create your first challenge to kick things off.</p>
        <a class="cf-btn cf-btn-primary"
           href="${pageContext.request.contextPath}/challenges/new">
          Create a challenge
        </a>
      </div>
    </c:when>

    <c:otherwise>
      <div class="cf-card">
        <table class="cf-table">
          <thead>
          <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Difficulty</th>
            <th class="cf-actions">Actions</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach items="${challenges}" var="ch">
            <tr>
              <td>
                ${ch.id}
              </td>
              <td>
                <a class="cf-link-strong"
                   href="${pageContext.request.contextPath}/challenges/${ch.id}">
                    ${ch.title}
                </a>
                <c:if test="${not empty ch.blurb}">
                  <div class="cf-muted small">
                      ${ch.blurb}
                  </div>
                </c:if>
              </td>

              <td>
                <span class="cf-pill cf-pill-difficulty-${ch.difficulty}">
                    ${ch.difficulty}
                </span>
              </td>

              <td class="cf-actions">
                <a class="cf-link"
                   href="${pageContext.request.contextPath}/challenges/${ch.id}">
                  View
                </a>
                <span class="cf-divider">|</span>
                <a class="cf-link"
                   href="${pageContext.request.contextPath}/challenges/${ch.id}/edit">
                  Edit
                </a>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </div>
    </c:otherwise>
  </c:choose>
</main>

<c:import url="/WEB-INF/jsp/footer.jsp" />

</body>
</html>