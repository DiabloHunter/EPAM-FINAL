<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="/WEB-INF/tlds/bodytag" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set value="${sessionScope.get(\"cart\")}" var="userCart" scope="page" />
<c:set value="${sessionScope.get(\"user\")}" var="user" scope="page" />
<c:set value="${pageScope.get(\"pageNum\")}" var="pageNum" scope="page" />
<c:set value="${sessionScope.get(\"cash\")}" var="cash" scope="page" />
<c:set value="${sessionScope.get(\"locale\").language}" var="lang" scope="page" />
<fmt:setLocale value="${sessionScope.get(\"locale\")}" />
<fmt:setBundle basename = "legend" var = "legend"/>
<fmt:setBundle basename = "menu" var = "menu"/>
<fmt:setBundle basename = "buttons" var = "buttons"/>

<html>
<head>
    <title><fmt:message key = "main.title" bundle = "${legend}"/></title>
</head>
<style>
    <%@include file='main_style.css' %>
</style>

<body>
    <h1><fmt:message key = "main.h1" bundle = "${legend}"/></h1>
    <div class="langmenu">
        <div class="langmenu_item"><c:out value="${user.name}, ${user.userRole}"/></div>
        <c:if test="${lang=='ru'}">
            <form class="langmenu_item" method="get" action="project">
                <input type="hidden" name="command" value="changeLang" />
                <input type="hidden" name="lang" value="ru" />
                <input type="hidden" name="returnPage" value="main" />
                <input type="hidden" name="pageNum" value="${pageNum}" />
                <button class="lang_button_active" type="submit">Ru</button>
            </form>
            <form class="langmenu_item" method="get" action="project">
                <input type="hidden" name="command" value="changeLang" />
                <input type="hidden" name="lang" value="en" />
                <input type="hidden" name="returnPage" value="main" />
                <input type="hidden" name="pageNum" value="${pageNum}" />
                <button class="lang_button" type="submit">En</button>
            </form>
        </c:if>
        <c:if test="${lang == 'en'}">
            <form class="langmenu_item" method="get" action="project">
                <input type="hidden" name="command" value="changeLang" />
                <input type="hidden" name="lang" value="ru" />
                <input type="hidden" name="returnPage" value="main" />
                <input type="hidden" name="pageNum" value="${pageNum}" />
                <button class="lang_button" type="submit">Ru</button>
            </form>
            <form class="langmenu_item" method="get" action="project">
                <input type="hidden" name="command" value="changeLang" />
                <input type="hidden" name="lang" value="en" />
                <input type="hidden" name="returnPage" value="main" />
                <input type="hidden" name="pageNum" value="${pageNum}" />
                <button class="lang_button_active" type="submit">En</button>
            </form>
        </c:if>
    </div>
    <div class="widemenu">
        <c:if test="${user.name == 'Guest'}">
            <form class="menuitem" name="addProductForm" method="get" action="project">
                <input type="hidden" name="command" value="addNewUser" />
                <button class="menubutton" type="submit">
                    <fmt:message key = "main.register" bundle = "${menu}"/>
                </button>
            </form>
            <form class="menuitem" name="addProductForm" method="get" action="project">
                <input type="hidden" name="command" value="enter" />
                <button class="menubutton" type="submit">
                    <fmt:message key = "main.login" bundle = "${menu}"/>
                </button>
            </form>
            <c:if test="${userCart.size > 0}">
                <form class="menuitem" name="addProductForm" method="get" action="project">
                    <input type="hidden" name="command" value="usersCart" />
                    <button class="menubutton" type="submit">
                        <fmt:message key="main.usersCart" bundle="${menu}"/><b>${userCart.size}</b>
                    </button>
                </form>
            </c:if>
        </c:if>
        <c:if test="${user.name != 'Guest'}">
            <c:if test="${user.name != 'Guest' && user.userRole != 'USER'}">
                <form class="menuitem" name="addProductForm" method="get" action="project">
                    <input type="hidden" name="command" value="administration" />
                    <button class="menubutton" type="submit">
                        <fmt:message key="main.administration" bundle="${menu}"/>
                    </button>
                </form>
            </c:if>
            <c:if test="${userCart.size > 0}">
                <form class="menuitem" name="addProductForm" method="get" action="project">
                    <input type="hidden" name="command" value="usersCart" />
                    <button class="menubutton" type="submit">
                        <fmt:message key="main.usersCart" bundle="${menu}"/><b>${userCart.size}</b>
                    </button>
                </form>
            </c:if>
            <form class="menuitem" name="addProductForm" method="get" action="project">
                <input type="hidden" name="command" value="showUserProfile" />
                <input type="hidden" name="edit" value="false" />
                <button class="menubutton" type="submit">
                    <fmt:message key="main.profile" bundle="${menu}"/>
                </button>
            </form>
            <form class="menuitem" name="addProductForm" method="post" action="project">
               <%-- <input type="hidden" name="command" value="logout" />--%>
                <input type="hidden" name="command" value="CommandGetCashAfterWork" />
                <button class="menubutton" type="submit">
                    <fmt:message key="main.logout" bundle="${menu}"/>
                </button>
            </form>
        </c:if>
    </div>
    <div>
        <table class="widetable">
            <tr>
                <th style="width: 5%;"><fmt:message key = "main.table.col1" bundle = "${legend}"/></th>
                <th style="width: 20%;"><fmt:message key = "main.table.col2" bundle = "${legend}"/></th>
                <th style="width: 20%;"><fmt:message key = "main.table.col3" bundle = "${legend}"/></th>
                <th style="width: 5%;"><fmt:message key = "main.table.col4" bundle = "${legend}"/></th>
                <th style="width: 5%;"><fmt:message key = "main.table.col5" bundle = "${legend}"/></th>
                <th style="width: 10%;"><fmt:message key = "main.table.col6" bundle = "${legend}"/></th>
                <th></th>
            </tr>
            <c:forEach items="${products}" var="product">
            <tr>
                <td class="tdc"><c:out value="${product.code}"/></td>
                <td class="tdl"><c:out value="${lang == 'ru' ? product.nameRu : product.nameEn}"/></td>
                <td class="tdl"><c:out value="${lang == 'ru' ? product.descriptionRu : product.descriptionEn}"/></td>
                <td class="tdl">
                    <fmt:formatNumber value="${product.cost}" maxFractionDigits="2" minFractionDigits="2"/>
                    <fmt:message key="currency" bundle="${legend}"/>
                </td>
                <c:choose>
                    <c:when test="${(product.quantity==0.0 || product.available == false)}">
                        <td class="tdc" style="color: darkred"><fmt:message key="main.outOfStock" bundle="${legend}"/></td>
                    </c:when>
                    <c:otherwise>
                        <td class="tdc" style="color: green"><fmt:message key="main.inStock" bundle="${legend}"/></td>
                    </c:otherwise>
                </c:choose>

                <td class="tdc"><c:out value="${lang == 'ru' ? product.notesRu : product.notesEn}"/></td>
                <td class="tdc">
                    <form name="addProductForm" method="post" action="project" >
                        <input type="hidden" name="command" value="addProductToCart" />
                        <input type="text" name="productQuantity" size="6" required/>
                        <input type="hidden" name="productQuantityFromDB" value="${(product.quantity)}" />
                        <c:if test="${user.name == 'Guest' || user.userRole == 'USER'}">
                            <button class="smallbutton" type="submit" name="productCode" value="${product.code}" disabled>
                                <fmt:message key="main.addToCart" bundle="${buttons}"/>
                            </button>
                        </c:if>
                        <c:if test="${user.name != 'Guest' && user.userRole != 'USER'}">
                            <button class="smallbutton" type="submit" name="productCode" value="${product.code}">
                                <fmt:message key="main.addToCart" bundle="${buttons}"/>
                            </button>
                        </c:if>
                    </form>
                    <form name="getProductDetailsForm" method="post" action="project" >
                        <input type="hidden" name="command" value="getProductDetails" />
                        <button class="smallbutton" type="submit" name="productCode" value="${product.code}">
                            <fmt:message key="main.productDetails" bundle="${buttons}"/>
                        </button>
                    </form>
                </td>
            </tr>
            </c:forEach>
        </table>
        <div class="pagination">
            <c:forEach var="i" begin="1" end="${totalPages}">
                <c:if test="${i==pageNum}">
                    <a class="active" href="/project?command=main&pageNum=${i}">${i}</a>
                </c:if>
                <c:if test="${i!=pageNum}">
                    <a href="/project?command=main&pageNum=${i}">${i}</a>
                </c:if>
            </c:forEach>
        </div>
    </div>
    <t:colontitle />

    <c:if test="${user.name != 'Guest' && user.userRole != 'USER' && user.userRole != 'MERCHANT' && !getCashBeforeWork}">
        <div id="overlay">
            <div class="popup">
                <center>
                    <h1>Enter the money in the cash machine:</h1>
                    <form name="getCashBeforeWork" method="post" action="project" >
                        <input type="hidden" name="command" value="CommandGetCashBeforeWork" />
                        <input type="text" name="cash" size="6" required/>
                        <input type="hidden" name="getCashBeforeWork" value="true"/>
                        <button class="smallbutton" type="submit" name="getCashBeforeWork">
                            <fmt:message key="main.getCashBeforeWork" bundle="${buttons}"/>
                        </button>
                    </form>
                </center>
            </div>
        </div>
    </c:if>
    <c:if test="${user.name != 'Guest' && user.userRole != 'USER' && user.userRole != 'MERCHANT' && finishWork}">
        <div id="overlay">
            <div class="popup">
                <center>
                    <h1>Enter the cash in the cash machine:</h1>
                    <form name="logout" method="post" action="project" >
                        <input type="hidden" name="command" value="logout" />
                        <input type="text" name="cash" size="6" required/>
                        <button class="smallbutton" type="submit" name="logout">
                            <fmt:message key="main.getCashBeforeWork" bundle="${buttons}"/>
                        </button>
                    </form>
                </center>
            </div>
        </div>
    </c:if>
    <script type="text/javascript">
        var delay_popup = 500;
        setTimeout("document.getElementById('overlay').style.display='block'", delay_popup);
    </script>
</body>
</html>