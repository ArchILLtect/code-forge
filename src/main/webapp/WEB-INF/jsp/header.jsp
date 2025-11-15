<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 9:48 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<header class="cf-header">
  <div class="cf-header-inner">
    <!-- Left: Logo + app name -->
    <div class="cf-header-brand">
      <a href="${pageContext.request.contextPath}/home" class="cf-header-logo-link">
        <img
                src="${pageContext.request.contextPath}/images/CodeForge-logo.png"
                alt="CodeForge Logo"
                class="cf-header-logo"
        />
        <span class="cf-header-title">CodeForge</span>
      </a>
    </div>

    <!-- Center: Navigation -->
    <nav class="cf-nav" aria-label="Main navigation">
      <a href="${pageContext.request.contextPath}/home" class="cf-nav-link">Home</a>
      <a href="${pageContext.request.contextPath}/challenges" class="cf-nav-link">Challenges</a>

      <div class="cf-nav-dropdown">
        <button class="cf-nav-link cf-nav-dropdown-toggle" type="button">
          Coding
          <span class="cf-nav-caret">▾</span>
        </button>
        <div class="cf-nav-dropdown-menu">
          <a href="${pageContext.request.contextPath}/practice" class="cf-nav-dropdown-item">
            Practice
          </a>
          <a href="${pageContext.request.contextPath}/drill" class="cf-nav-dropdown-item">
            Drill Mode
          </a>
        </div>
      </div>

      <a href="${pageContext.request.contextPath}/about" class="cf-nav-link">About</a>
    </nav>

    <!-- Right: User info / login -->
    <div class="cf-user-area">
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <span class="cf-user-greeting">
            Hi, <strong>${sessionScope.user.userName}</strong>
          </span>
          <a href="${pageContext.request.contextPath}/logout" class="cf-btn cf-btn-outline">
            Log out
          </a>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/logIn" class="cf-btn cf-btn-primary">
            Log in
          </a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</header>