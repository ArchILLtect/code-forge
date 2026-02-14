<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 9:48 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

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
    <div class="cf-nav" aria-label="Main navigation">
      <ul class="cf-nav-list">
        <li class="cf-nav-item">
            <a href="${pageContext.request.contextPath}/challenges" class="cf-nav-link">Challenges</a>
        </li>

        <li class="cf-nav-item cf-nav-item-has-menu">
          <a class="cf-nav-link" href="#">
            Coding
            <span class="cf-nav-caret">▾</span>
          </a>

          <ul class="cf-nav-dropdown">
            <li>
              <a href="${pageContext.request.contextPath}/practice" class="cf-nav-dropdown-link">
                Practice
              </a>
            </li>
            <li>
              <a href="${pageContext.request.contextPath}/drill" class="cf-nav-dropdown-link">
                Drill
              </a>
            </li>
          </ul>
        </li>

        <li class="cf-nav-item">
          <a href="${pageContext.request.contextPath}/about" class="cf-nav-link">About</a>
        </li>
      </ul>
    </div>

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
