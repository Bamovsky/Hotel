<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="hotel"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" type="text/css" href="<c:url value="css/index.css"></c:url>">
    <title>Hotel</title>
</head>

<body class="d-flex flex-column bg-light">
<div id="page-content">
    <header class="bg-light">
        <div class="container">
            <nav class="menu navbar navbar-expand-lg navbar-light bg-light">
                <a class="navbar-brand" href="#">Logo</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navigation navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="<c:url value="index.jsp"></c:url>"> <fmt:message key="main"/><br/><span
                                    class="sr-only">(current)</span></a>
                        </li>
                        <c:if test="${sessionScope.role == 'client'}">
                            <li class="nav-item">
                                <a class="nav-link" href="<c:url value="/controller?command=showApartments"></c:url>"><fmt:message key="apartments"/></a>
                            </li>
                        </c:if>
                        <c:if test="${not empty sessionScope.role}">
                            <c:if test="${sessionScope.role == 'manager'}">
                                <li class="nav-item">
                                    <a class="nav-link" href="#"> <fmt:message key="myOrders"/></a>
                                </li>
                            </c:if>
                        </c:if>
                        <li class="signIn nav-item">
                            <c:if test="${empty sessionScope.user}">
                                <a class="nav-link" href="<c:url value="login.jsp"></c:url>"><fmt:message
                                        key="signIn"/></a>
                            </c:if>
                            <c:if test="${not empty sessionScope.user}">
                                <a class="nav-link"
                                   href="<c:url value="/controller?command=logOut"></c:url>"><fmt:message
                                        key="logOut"/></a>
                            </c:if>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                               data-toggle="dropdown"
                               aria-haspopup="true" aria-expanded="false">
                                <fmt:message key="language"/>
                            </a>

                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item"
                                   href="<c:url value="/controller?command=changeLocale&newLocale=ru&linkToForward=orders"></c:url>"><fmt:message
                                        key="russian"/></a>
                                <a class="dropdown-item"
                                   href="<c:url value="/controller?command=changeLocale&newLocale=en&linkToForward=orders"></c:url>"><fmt:message
                                        key="english"/></a>
                            </div>
                        </li>

                    </ul>
                </div>
            </nav>
        </div>
    </header>

    <div class="mainWrapper">
        <section class="container">
            <table class="table">
                <thead>
                <tr>
                    <th scope="col">Почта пользователя</th>
                    <th scope="col">Дата заезда</th>
                    <th scope="col">Дата выезда</th>
                    <th scope="col">Клас</th>
                    <th scope="col">Кол-во мест</th>
                    <th scope="col">Выбор апартаментов</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="orders" items="${requestScope.orders}">
                    <tr>
                        <td><c:out value="${orders.getUserEmail()}"/></td>
                        <td><c:out value="${orders.getArrivalDate()}"/></td>
                        <td><c:out value="${orders.getDepartureDate()}"/></td>
                        <td><c:out value="${orders.getNumberOfRooms()}"/></td>
                        <td><c:out value="${orders.getClassInString()}"/></td>
                        <td><a href="<c:url value="/controller?command=showApartmentsAsManager&orderId=${orders.getId()}"></c:url>">Выбор
                            апартаментов</a></td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </section>
    </div>
</div>


<footer id="sticky-footer" class="py-4 bg-dark text-white-50">
    <div class="container text-center">
        <small>Copyright &copy; Cherkashyn EPAM TRAINING</small>
    </div>
</footer>


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

</body>
</html>