<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:10 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="pageTitle" value="Me | CodeForge" scope="request" />

<!DOCTYPE html>
<html lang="en">
<head>
  <c:import url="/WEB-INF/jsp/head-meta.jsp" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/home.css" />
</head>
<body>

<c:import url="/WEB-INF/jsp/header.jsp" />

<main class="cf-main">
    <section class="cf-section">
        <div class="cf-section-inner">
            <c:choose>
                <c:when test="${empty user}">
                    <div class="cf-quote-card">
                        <h2 class="cf-section-title">You’re not logged in</h2>
                        <p class="cf-quote-text">
                            To view your profile details, please log in first.
                        </p>
                        <p>
                            <a href="${pageContext.request.contextPath}/logIn"
                               class="cf-btn cf-btn-primary">
                                Log in
                            </a>
                        </p>
                    </div>
                </c:when>

                <c:otherwise>
                    <div class="cf-profile-card">
                        <h1 class="cf-section-title">Welcome, ${user.userName}</h1>

                        <p class="cf-profile-subtitle">
                            👻Here’s what CodeForge knows about you from Cognito.👻
                        </p>

                        <dl class="cf-profile-details">
                            <div class="cf-profile-row">
                                <dt>Email</dt>
                                <dd>${user.email}</dd>
                            </div>
                            <div class="cf-profile-row">
                                <dt>User ID</dt>
                                <dd>${user.sub}</dd>
                            </div>
                        </dl>

                        <div class="cf-profile-actions">
                            <a href="${pageContext.request.contextPath}/home"
                               class="cf-btn cf-btn-outline">
                                Back to Home
                            </a>
                            <a href="${pageContext.request.contextPath}/logout"
                               class="cf-btn cf-btn-primary">
                                Log out
                            </a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>
</main>

<c:import url="/WEB-INF/jsp/footer.jsp" />

</body>
</html>
