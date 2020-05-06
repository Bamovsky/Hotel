<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="hotel"/>

<c:if test="${empty requestScope.dataError}">
    <div class="row">

        <c:forEach var="apartment" items="${requestScope.apartments}">
            <div class="card col-md-4">
                <img class="card-img-top" src="<c:url value="${apartment.getImg()}"></c:url>" alt="Card image cap">
                <div class="card-body">
                    <h5 class="card-title"><c:out value="${apartment.getName()}"/></h5>
                    <h5 class="card-title">Цена в сутки: <c:out value="${apartment.getPrice()}"/> UAN</h5>
                    <h5 class="card-title">Статус: <c:out value="${apartment.getStatusI18N()}"/></h5>
                    <h5 class="card-title">Класс: <c:out value="${apartment.getClassI18N()}"/></h5>
                    <p class="card-text"><c:out value="${apartment.getShortDescription()}"/></p>
                    <c:if test="${apartment.getStatusId() == 0 && requestScope.role == 'client'}">
                        <a href="#" class="btn btn-primary Book" data-id="<c:out value="${apartment.getId()}"/>">Забронировать</a>
                    </c:if>
                    <c:if test="${apartment.getStatusId() == 0 && requestScope.role == 'manager'}">
                        <a href="#" class="btn btn-primary Book"
                           data-id="<c:out value="${apartment.getId()}"/>">Выбрать</a>
                    </c:if>
                </div>
            </div>
        </c:forEach>

    </div>

    <nav aria-label="Page navigation example">
        <ul class="pagination justify-content-center">

            <c:if test="${requestScope.quantityOfPages != 0}">
                <li class="page-item">
                    <c:if test="${requestScope.offset == 0}">
                        <a class="page-link" href="#" id="Prev" data-offset="${requestScope.offset}">Prev</a>
                    </c:if>
                    <c:if test="${requestScope.offset != 0}">
                        <a class="page-link" href="#" id="Prev" data-offset="${requestScope.offset - 6}">Prev</a>
                    </c:if>
                </li>
            </c:if>

            <c:if test="${requestScope.quantityOfPages > 3}">

                <c:if test="${requestScope.offset/6 < 2}">
                    <c:forEach var="i" begin="0" end="2">
                        <li class="page-item">
                            <a class="page-link pageNumber" href="#" data-offset="${i*6}"><c:out value="${i+1}"/></a>
                        </li>
                    </c:forEach>
                </c:if>

                <c:if test="${requestScope.offset/6 > (requestScope.quantityOfPages - 3)}">
                    <c:forEach var="i" begin="${requestScope.quantityOfPages-3}"
                               end="${requestScope.quantityOfPages-1}">
                        <li class="page-item">
                            <a class="page-link pageNumber" href="#" data-offset="${i*6}"><c:out value="${i+1}"/></a>
                        </li>
                    </c:forEach>
                </c:if>


                <c:if test="${requestScope.offset/6 >= 2 && requestScope.offset/6 <= (requestScope.quantityOfPages-3)}">
                    <c:forEach var="i" begin="${requestScope.offset/6 - 1}" end="${requestScope.offset/6 + 1}">
                        <li class="page-item">
                            <a class="page-link pageNumber" href="#" data-offset="${i*6}"><c:out value="${i+1}"/></a>
                        </li>
                    </c:forEach>
                </c:if>


            </c:if>

            <c:if test="${requestScope.quantityOfPages <= 3 && requestScope.quantityOfPages > 0}">
                <c:forEach var="i" begin="0" end="${requestScope.quantityOfPages-1}">
                    <li class="page-item">
                        <a class="page-link pageNumber" href="#" data-offset="${i*6}"><c:out value="${i+1}"/></a>
                    </li>
                </c:forEach>
            </c:if>


            <c:if test="${requestScope.quantityOfPages != 0}">
                <li class="page-item">
                    <c:if test="${(requestScope.quantityOfPages - 1)*6 == requestScope.offset}">
                        <a class="page-link" href="#" id="Next" data-offset="${requestScope.offset}">Next</a>
                    </c:if>
                    <c:if test="${(requestScope.quantityOfPages - 1)*6!= requestScope.offset}">
                        <a class="page-link" href="#" id="Next" data-offset="${requestScope.offset + 6}">Next</a>
                    </c:if>
                </li>
            </c:if>

        </ul>
    </nav>

</c:if>


<c:if test="${not empty requestScope.dataError}">
    <h2>${requestScope.dataError}</h2>
</c:if>
