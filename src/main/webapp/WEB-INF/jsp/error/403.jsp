<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 12/15/2025
  Time: 9:51 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Forbidden | CodeForge" scope="request" />

<!DOCTYPE html>
<html lang="en">
<head>
  <c:import url="/WEB-INF/jsp/head-meta.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>
<c:import url="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
  <section class="cf-card cf-error-card cf-error-card-warning">
    <div class="cf-error-code">403</div>

    <h1 class="cf-page-title">Access Forbidden</h1>
    <p class="cf-page-subtitle">
      You don’t have permission to access this resource.
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

    <c:if test="${not empty requestScope['javax.servlet.error.request_uri']}">
      <p class="cf-muted">
        While requesting:
        <span class="cf-mono">
          <c:out value="${requestScope['javax.servlet.error.request_uri']}" />
        </span>
      </p>
    </c:if>

    <div class="cf-error-actions">
      <a class="cf-btn cf-btn-primary"
         href="${pageContext.request.contextPath}/">
        ← Back to home
      </a>

      <a class="cf-btn cf-btn-ghost"
         href="${pageContext.request.contextPath}/challenges">
        Browse challenges
      </a>

      <c:if test="${empty sessionScope.user}">
        <a class="cf-btn cf-btn-outline"
           href="${pageContext.request.contextPath}/logIn">
          Log in
        </a>
      </c:if>

    </div>

    <c:if test="${not empty timestamp}">
      <p class="cf-tiny cf-muted">Timestamp: <c:out value="${timestamp}" /></p>
    </c:if>

  </section>
</main>

<c:import url="/WEB-INF/jsp/footer.jsp" />

</body>
</html>
