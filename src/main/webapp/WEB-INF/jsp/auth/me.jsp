<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 10/24/2025
  Time: 2:10 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<c:choose>
    <c:when test="${empty user}">
        <a href="${pageContext.request.contextPath}/logIn">Log in</a>
    </c:when>
    <c:otherwise>
        <h3>Welcome ${user.userName}</h3>
        <h4>eMail:</h4><p>${user.email}</p>
        <h4>userId:</h4><p>${user.sub}</p>

        <a href="${pageContext.request.contextPath}/">Home</a>

        <a href="${pageContext.request.contextPath}/logout">Log out</a>
    </c:otherwise>
</c:choose>
</body>
</html>
