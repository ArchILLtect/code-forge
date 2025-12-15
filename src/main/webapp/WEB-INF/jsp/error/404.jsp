<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/23/2025
  Time: 6:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Not Found | CodeForge" />

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
  <section class="cf-card cf-error-card">
    <div class="cf-error-code">404</div>

    <h1 class="cf-page-title">Page not found</h1>
    <p class="cf-page-subtitle">
      The page you’re looking for doesn’t exist, has moved, or never compiled
      in the first place.
    </p>

    <c:if test="${not empty requestScope['javax.servlet.error.request_uri']}">
      <p class="cf-muted">
        Requested path:
        <span class="cf-mono">
            ${requestScope['javax.servlet.error.request_uri']}
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
        View challenges
      </a>
    </div>
  </section>
</main>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>
