<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="hotel" />

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="<c:url value="css/style.min.css"></c:url>">
    <title>Authentication</title>
</head>

<body>
<div class="container">
    <div class="Language">
        <div class="dropdown show">
            <a class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink"
               data-toggle="dropdown"
               aria-haspopup="true" aria-expanded="false">
                <fmt:message key = "language"/>
            </a>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuLink">
                <a class="dropdown-item" href="<c:url value="/controller?command=changeLocale&newLocale=ru&linkToForward=login"></c:url>"><fmt:message key = "russian"/></a>
                <a class="dropdown-item" href="<c:url value="/controller?command=changeLocale&newLocale=en&linkToForward=login"></c:url>"><fmt:message key = "english"/></a>
            </div>
        </div>
    </div>
    <div class="FormWrapper">
        <div class="FormNav">
            <button type="button" class="btn btn-active" id="SignIn"><fmt:message key = "signIn"/></button>
            <button type="button" class="btn btn-info" id="SignUp"><fmt:message key = "signUp"/></button>
            <button type="button" class="btn btn-info" id="ForgetPassword"><fmt:message key = "forgetPassword"/></button>
        </div>
        <div class="SignInForm FormActive">
            <form>
                <div class="form-group row">
                    <label for="SingInEmail" class="col-sm-12 col-form-label"><fmt:message key = "email"/></label>
                    <div class="col-sm-12">
                        <input type="email" class="form-control" id="SingInEmail" placeholder="<fmt:message key = "email"/>">
                    </div>
                    <div class="valid-feedback col-sm-12" id="emailSingInFeedback">
                        Looks good!
                    </div>
                </div>
                <div class="form-group row">
                    <label for="SingInPassword" class="col-sm-12 col-form-label"><fmt:message key = "password"/></label>
                    <div class="col-sm-12">
                        <input type="password" class="form-control" id="SingInPassword" placeholder="<fmt:message key = "password"/>">
                    </div>
                    <div class="valid-feedback" id="passwordSingInFeedback">
                        Looks good!
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-12 d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary" id="SignInBtn"><fmt:message key = "signIn"/></button>
                    </div>
                </div>
            </form>
        </div>
        <div class="SignUpForm">
            <form>
                <div class="form-group row">
                    <label for="SingUpEmail" class="col-sm-12 col-form-label"><fmt:message key = "email"/></label>
                    <div class="col-sm-12">
                        <input type="email" class="form-control" id="SingUpEmail" placeholder="<fmt:message key = "email"/>">
                    </div>
                    <div class="valid-feedback" id="emailSingUpFeedback">
                        Looks good!
                    </div>
                </div>
                <div class="form-group row">
                    <label for="SingUpPassword" class="col-sm-12 col-form-label"><fmt:message key = "password"/></label>
                    <div class="col-sm-12">
                        <input type="password" class="form-control" id="SingUpPassword" placeholder="<fmt:message key = "password"/>">
                    </div>
                    <div class="valid-feedback" id="passwordSingUpFeedback">
                        Looks good!
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-12 d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary" id="SignUpBtn"><fmt:message key = "signUp"/></button>
                    </div>
                </div>
            </form>
        </div>
        <div class="ForgetPasswordForm">
            <form>
                <div class="form-group row">
                    <label for="ForgetPasswordEmail" class="col-sm-12 col-form-label"><fmt:message key = "email"/></label>
                    <div class="col-sm-12">
                        <input type="email" class="form-control" id="ForgetPasswordEmail" placeholder="<fmt:message key = "email"/>">
                    </div>
                    <div class="valid-feedback" id="forgetPasswordFeedback">
                        Looks good!
                    </div>
                </div>
                <div class="form-group row">
                    <div class="col-sm-12 d-flex justify-content-center">
                        <button type="submit" class="btn btn-primary" id="ForgetPasswordBtn"><fmt:message key = "send"/></button>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>

<div class="modal fade" id="Modal" tabindex="-1" role="dialog" aria-labelledby="ModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="ModalLongTitle">
                    <c:if test="${not empty requestScope.title}">
                        ${requestScope.title}
                    </c:if>
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <c:if test="${not empty requestScope.message}">
                    ${requestScope.message}
                </c:if>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>


<script src="<c:url value="js/libs.min.js"></c:url>"></script>
<script src="<c:url value="js/main.min.js"></c:url>"></script>

<c:if test="${not empty requestScope.approved}">
    <script>$('#Modal').modal()</script>
</c:if>


</body>

</html>