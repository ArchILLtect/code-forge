<%--
  Created by IntelliJ IDEA.
  User: nickh
  Date: 12/11/2025
  Time: 9:11 AM
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
    <c:when test="${not empty pageTitle}">
        <title><c:out value="${pageTitle}" /></title>
    </c:when>
    <c:otherwise>
        <title>CodeForge</title>
    </c:otherwise>
</c:choose>

<!-- Meta tags -->
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />

<!-- Global Stylesheets-- always needed -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/layout.css" />

<!-- Standard ICO favicon -->
<link rel="icon" type="image/x-icon" href="${pageContext.request.contextPath}/favicon.ico" />

<!-- PNG fallbacks / explicit sizes -->
<link rel="icon" type="image/png" sizes="16x16" href="${pageContext.request.contextPath}/favicon-16.png" />
<link rel="icon" type="image/png" sizes="32x32" href="${pageContext.request.contextPath}/favicon-32.png" />
<link rel="icon" type="image/png" sizes="40x40" href="${pageContext.request.contextPath}/favicon-40.png" />
<link rel="icon" type="image/png" sizes="64x64" href="${pageContext.request.contextPath}/favicon-64.png" />