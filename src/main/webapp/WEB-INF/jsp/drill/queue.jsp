<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/03/2025
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
  <title>Drill Queue</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/drill.css" />
</head>
<body>

<jsp:include page="/WEB-INF/jsp/header.jsp" />

  <main class="cf-main">
    <section class="cf-page-header">
      <div>
        <h1 class="cf-page-title">Drill Queue</h1>
        <p class="cf-page-subtitle">
          Spaced-repetition practice for your challenges. The ones due soonest
          are always at the top of the queue.
        </p>
      </div>

      <a class="cf-btn cf-btn-primary"
         href="${pageContext.request.contextPath}/drill/next">
        ▶ Start / Continue Drill
      </a>
    </section>

    <!-- Flash messages -->
    <c:if test="${not empty success}">
      <div class="cf-alert cf-alert-success">${success}</div>
    </c:if>
    <c:if test="${not empty error}">
      <div class="cf-alert cf-alert-error">${error}</div>
    </c:if>
    <c:if test="${not empty info}">
      <div class="cf-alert cf-alert-info">${info}</div>
    </c:if>

    <!-- Enrollment banner -->
    <c:if test="${enrolledCreated gt 0}">
      <div class="cf-alert cf-alert-info">
        Enrolled <strong><c:out value="${enrolledCreated}"/></strong> challenge(s) into your drill queue.
      </div>
    </c:if>

    <c:choose>
      <c:when test="${empty rows}">
        <div class="cf-empty-state">
          <h2>Nothing due yet</h2>
          <p>
            Great job staying ahead of the curve. Hit
            <strong>Start / Continue Drill</strong> to pull in the next upcoming item.
          </p>
        </div>
      </c:when>

      <c:otherwise>
        <section class="cf-card">
          <header class="drill-queue-header">
            <div>
              <p class="cf-muted">
                Total items in this view:
                <strong><c:out value="${count}" /></strong>
              </p>
            </div>
          </header>

          <table class="cf-table drill-queue-table">
            <thead>
            <tr>
              <th>Challenge</th>
              <th>Difficulty</th>
              <th>Next due</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${rows}" var="r">
              <tr>
                <td>
                  <a class="cf-link-strong"
                     href="${pageContext.request.contextPath}/drill/${r.challengeId}">
                    <c:out value="${r.title}" />
                  </a>
                </td>
                <td>
                  <span class="cf-pill cf-pill-difficulty-${r.difficulty}">
                    <c:out value="${r.difficulty}" />
                  </span>
                </td>
                <td class="cf-nowrap">
                  <c:out value="${r.nextDueAt}" />
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
        </section>
      </c:otherwise>
    </c:choose>
  </main>

</body>
</html>
