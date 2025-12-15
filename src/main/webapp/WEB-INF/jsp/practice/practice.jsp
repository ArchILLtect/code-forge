<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/03/2025
  Time: 6:30 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Practice | CodeForge" />

<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <%@ include file="/WEB-INF/jsp/head-meta.jspf" %>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/practice.css" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />
<main class="cf-main">
  <section class="cf-page-header">
    <div>
      <h1 class="cf-page-title">Practice</h1>
      <p class="cf-page-subtitle">Try solving challenges without affecting your drill schedule.</p>
    </div>
    <a class="cf-link" href="${pageContext.request.contextPath}/challenges">← Back to challenges</a>
  </section>

  <section class="cf-card">
    <p>Choose a challenge from the list and click its <strong>View</strong> link, then use the <strong>Practice</strong> action to attempt it here.</p>
    <p>Or go directly by visiting <code>/practice/{challengeId}</code>.</p>
    <div class="cf-actions">
      <a class="cf-btn cf-btn-primary" href="${pageContext.request.contextPath}/challenges">Browse challenges</a>
    </div>
  </section>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>
