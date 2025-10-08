<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <title>New Challenge</title>
</head>
<body>
  <p><a href="${pageContext.request.contextPath}/">Home</a> | <a href="${pageContext.request.contextPath}/challenges">All Challenges</a></p>
  <h1>Create New Challenge</h1>

  <form:form method="post" modelAttribute="form" action="${pageContext.request.contextPath}/challenges">
    <div>
      <label for="title">Title</label><br/>
      <form:input path="title" id="title" />
      <div style="color: #b00;"><form:errors path="title"/></div>
    </div>

    <div>
      <label for="difficulty">Difficulty</label><br/>
      <form:select path="difficulty" id="difficulty">
        <form:options items="${difficulties}" />
      </form:select>
      <div style="color: #b00;"><form:errors path="difficulty"/></div>
    </div>

    <div>
      <label for="blurb">Summary</label><br/>
      <form:textarea path="blurb" id="blurb" rows="3" cols="60"/>
      <div style="color: #b00;"><form:errors path="blurb"/></div>
    </div>

    <div>
      <label for="promptMd">Prompt (Markdown)</label><br/>
      <form:textarea path="promptMd" id="promptMd" rows="10" cols="80"/>
      <div style="color: #b00;"><form:errors path="promptMd"/></div>
    </div>

    <div style="margin-top: 1rem;">
      <button type="submit">Create</button>
    </div>
  </form:form>
</body>
</html>

