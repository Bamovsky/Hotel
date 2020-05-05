<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
this is error page
<c:if test="${not empty requestScope.errorMessage}">
    ${requestScope.errorMessage}
</c:if>
</body>
</html>
