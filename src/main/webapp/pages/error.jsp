<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="t" uri="/WEB-INF/tlds/bodytag" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:set value="${sessionScope.get(\"locale\").language}" var="lang" scope="page" />
<fmt:setLocale value="${sessionScope.get(\"locale\")}" />
<fmt:setBundle basename = "errors" var = "errors"/>
<fmt:setBundle basename = "buttons" var = "buttons"/>


<html>
<head>
</head>
<style>
    <%@include file='main_style.css' %>
</style>
<body>
<div class = index_div>
    <form action="project" method="post" >
        <h2><fmt:message key = "generalErr" bundle = "${errors}"/></h2>
        <input type="submit" class="bigbutton" value="<fmt:message key = "main.back" bundle = "${buttons}"/>" onclick="history.back(); return false;"/>
    </form>
</div>
<t:colontitle/>
</body>
</html>