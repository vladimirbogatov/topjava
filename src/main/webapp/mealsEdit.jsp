<%--
  Created by IntelliJ IDEA.
  User: vladi
  Date: 06.02.2022
  Time: 1:54
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    <input type="hidden" readonly="readonly" name="mealToId"
                value="<c:out value="${meal.mealId}"/>"/><br/>
    DOB : <input
        type="datetime-local" name="dob"
        value="<c:out value="${meal.dateTime}" />"/> <br/>
    Description : <input
        type="text" name="description"
        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <td><input type="submit" name="submit" value="Submit"/></td>
    <td><input type="submit" name="submit" value="Cancel"/></td>
</form>
</body>
</html>
