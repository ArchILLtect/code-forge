<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>Not Found</title>
</head>
<body>
  <h1>404 — Not Found</h1>
  <p><strong>${status}</strong> ${error}</p>
  <p>${message}</p>
  <p><a href="${pageContext.request.contextPath}/">Back to Home</a></p>
</body>
</html>

