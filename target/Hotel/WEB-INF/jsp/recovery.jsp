<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>



<c:if test="${not empty requestScope.language}">
    <c:set var="language" value="${param.language}" scope="session"/>
    <fmt:setLocale value="${language}" />
</c:if>



<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="<c:url value="css/style.min.css"></c:url>">
    <title>Recovery</title>
</head>

<body>
<div class="container">
    <div class="Language">
        <div class="dropdown show">
            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                Language
            </a>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                <a class="dropdown-item" href="#">Russian</a>
                <a class="dropdown-item" href="#">English</a>
            </div>
        </div>
    </div>
    <div class="FormWrapper">
        <div class="RecoveryForm FormActive">
            <form >
                <div class="form-group row">
                    <label for="RecoveryPassword" class="col-sm-12 col-form-label">Password</label>
                    <div class="col-sm-12">
                        <input type="password" class="form-control" id="RecoveryPassword" placeholder="Password">
                    </div>
                    <div class="valid-feedback col-sm-12" id="RecoveryPasswordFeedback"></div>
                </div>
                <div class="form-group row">
                    <label for="RepeatPassword" class="col-sm-12 col-form-label">Repeat Password</label>
                    <div class="col-sm-12">
                        <input type="password" class="form-control" id="RepeatPassword" placeholder="Repeat password">
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-12 d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary" id="RecoveryBtn">Recover</button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<script src="<c:url value="js/libs.min.js"></c:url>"></script>
<script src="<c:url value="js/recovery.js"></c:url>"></script>

</body>
</html>
