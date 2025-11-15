<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 11/14/2025
  Time: 9:49 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<footer class="cf-footer">
  <div class="cf-footer-inner">
    <p class="cf-footer-text">
      &copy; <%= java.time.Year.now() %> CodeForge. Sharpen your skills, one challenge at a time.
    </p>

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