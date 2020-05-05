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
    <link rel="stylesheet" type="text/css" href="<c:url value="css/apartment.css"></c:url>">
    <title>Apartments</title>
</head>

<body class="d-flex flex-column bg-light">
<div id="page-content">
    <header class="bg-light">
        <div class="container">
            <nav class="menu navbar navbar-expand-lg navbar-light bg-light">
                <a class="navbar-brand" href="#">Logo</a>
                <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>

                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navigation navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="<c:url value="index.jsp"></c:url>"> <fmt:message key = "main"/><br/><span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="<c:url value="/controller?command=showApartments"></c:url>"> <fmt:message key = "apartments"/></a>
                        </li>
                        <c:if test="${not empty sessionScope.user}">
                        <li class="nav-item">
                            <a class="nav-link" href="#"> <fmt:message key = "myOrders"/></a>
                        </li>
                        </c:if>
                        <c:if test="${not empty sessionScope.user}">
                            <li class="nav-item">
                                <a class="nav-link" href="#"> <fmt:message key = "myOrders"/></a>
                            </li>
                        </c:if>
                        <li class="signIn nav-item">
                            <c:if test="${empty sessionScope.user}">
                                <a class="nav-link" href="<c:url value="login.jsp"></c:url>"><fmt:message key = "signIn"/></a>
                            </c:if>
                            <c:if test="${not empty sessionScope.user}">
                                <a class="nav-link" href="<c:url value="/controller?command=logOut"></c:url>"><fmt:message key = "logOut"/></a>
                            </c:if>
                        </li>
                        <li class="nav-item dropdown">
                            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown"
                               aria-haspopup="true" aria-expanded="false">
                                <fmt:message key = "language"/>
                            </a>

                            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
                                <a class="dropdown-item" href="<c:url value="/controller?command=changeLocale&newLocale=ru&linkToForward=apartments"></c:url>"><fmt:message key = "russian"/></a>
                                <a class="dropdown-item" href="<c:url value="/controller?command=changeLocale&newLocale=en&linkToForward=apartments"></c:url>"><fmt:message key = "english"/></a>
                            </div>
                        </li>

                    </ul>
                </div>

            </nav>
        </div>
    </header>

    <div class="container">
        <div class="row">
            <div class="col-md-2">
                <form>

                    <p>Диапазон цен</p>
                    <div class="range-slider">
                        <span class="rangeValues"></span>
                        <input value="${requestScope.minPrice}" min="${requestScope.minPrice}" max="${requestScope.maxPrice}" step="1" type="range">
                        <input value="${requestScope.maxPrice}" min="${requestScope.minPrice}" max="${requestScope.maxPrice}" step="1" type="range">
                    </div>

                    <div class="form-group">
                        <span>Цена по:</span>
                        <select class="form-control" id="PriceSelect">
                            <option value="true">По возрастанию</option>
                            <option value="false">По убыванию</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <span>Класс</span>
                        <select class="form-control" id="ClassSelect">
                            <option value="0">STANDARD</option>
                            <option value="1">IMPROVED</option>
                            <option value="2">LUXURY</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <span>Кол-во мест</span>
                        <select class="form-control" id="QuantityOfRoomsSelect">
                            <option value="1">1</option>
                            <option value="2">2</option>
                            <option value="3">3</option>
                            <option value="4">4</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <span>Статус</span>
                        <select class="form-control" id="StatusSelect">
                            <option value="0">FREE</option>
                            <option value="1">BOOKED</option>
                            <option value="2">OCCUPIED</option>
                            <option value="3">INACCESSIBLE</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="ArrivalDate">дата 1</label>
                        <div>
                            <input class="form-control"  type="date" id="ArrivalDate">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="DepartureDate">дата 2</label>
                        <div>
                            <input class="form-control" type="date" id="DepartureDate">
                        </div>
                    </div>


                </form>

            </div>

            <div class="col-md-10" id="ApartmentContent">

            </div>

        </div>
    </div>
</div>

<footer id="sticky-footer" class="py-4 bg-dark text-white-50">
    <div class="container text-center">
        <small>Copyright &copy; Your Website</small>
    </div>
</footer>


<div class="modal fade" id="Modal" tabindex="-1" role="dialog" aria-labelledby="ModalCenterTitle" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="ModalLongTitle">
                </h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body" id="ModalBody">
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            </div>
        </div>
    </div>
</div>

<script src="<c:url value="js/libs.min.js"></c:url>"></script>
<script src="<c:url value="js/apartment.js"></c:url>"></script>

</body>
</html>