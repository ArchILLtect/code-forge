<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/23/2025
  Time: 6:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Server Error | CodeForge" scope="request" />

<!DOCTYPE html>
<html lang="en">
<head>
  <c:import url="/WEB-INF/jsp/head-meta.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>
<c:import url="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
  <section class="cf-card cf-error-card cf-error-card-critical">
    <div class="cf-error-code">500</div>

    <h1 class="cf-page-title">Something went wrong</h1>
    <p class="cf-page-subtitle">
      The server hit an unexpected error while processing your request.
    </p>

    <c:if test="${not empty errorMessage}">
      <p class="cf-muted">
        Details:
        <br />
        <span class="cf-mono">
          <c:out value="${errorMessage}" />
        </span>
      </p>
    </c:if>

    <c:if test="${not empty requestScope['jakarta.servlet.error.request_uri']}">
      <p class="cf-muted">
        While handling:
        <span class="cf-mono">
          <c:out value="${requestScope['jakarta.servlet.error.request_uri']}" />
        </span>
      </p>
    </c:if>

    <div class="cf-error-actions">
      <a class="cf-btn cf-btn-primary"
         href="${pageContext.request.contextPath}/">
        ← Back to home
      </a>

      <a class="cf-btn cf-btn-ghost"
         href="${pageContext.request.contextPath}/home">
        Go to Homepage
      </a>

    </div>

    <c:if test="${not empty timestamp}">
      <p class="cf-tiny cf-muted">Timestamp: <c:out value="${timestamp}" /></p>
    </c:if>

    <%-- TODO: Update this message post-MVP --%>
    <p class="cf-tiny cf-muted">
      If this keeps happening, grab the timestamp from this page and check
      the application logs in your deployment environment.
    </p>

  </section>
</main>

<c:import url="/WEB-INF/jsp/footer.jsp" />

</body>
</html>

