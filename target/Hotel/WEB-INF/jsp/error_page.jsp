<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
this is error page
${requestScope.approvedToken}
<c:if test="${not empty requestScope.approvedToken}">
    ${requestScope.approvedToken}
</c:if>
</body>
</html>
