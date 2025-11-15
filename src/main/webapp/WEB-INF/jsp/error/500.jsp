<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/23/2025
  Time: 6:30 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Server Error</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp" />

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
            ${errorMessage}
        </span>
      </p>
    </c:if>

    <c:if test="${not empty requestScope['javax.servlet.error.request_uri']}">
      <p class="cf-muted">
        While handling:
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
         href="${pageContext.request.contextPath}/drill">
        Go to Drill queue
      </a>
    </div>

    <p class="cf-tiny cf-muted">
      If this keeps happening, grab the timestamp from this page and check
      the logs in Elastic Beanstalk.
    </p>
  </section>
</main>

<jsp:include page="/WEB-INF/jsp/footer.jsp" />
</body>
</html>
