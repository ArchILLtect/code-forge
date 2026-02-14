<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 9:49 PM
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<footer class="cf-footer">
  <div class="cf-footer-inner">
    <p class="cf-footer-text">
      &copy; <c:out value="${currentYear}" />
      CodeForge. Sharpen your skills, one challenge at a time.
    </p>

    <!-- TODO: Add "created by: Nick Hanson (as link to showcase)" -->
    <div class="cf-footer-actions">
      <c:choose>
        <c:when test="${not empty sessionScope.user}">
          <a href="${pageContext.request.contextPath}/logout" class="cf-footer-link">
            Log out
          </a>
        </c:when>
        <c:otherwise>
          <a href="${pageContext.request.contextPath}/logIn" class="cf-footer-link">
            Log in
          </a>
        </c:otherwise>
      </c:choose>
    </div>
  </div>
</footer>
