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
                value="${meal.id}"/> <br/>
    DOB : <input
        type="datetime-local" name="dateTime"
        value="${meal.dateTime}" /> <br/>
    Description : <input
        type="text" name="description"
        value="${meal.description}" /> <br/>
    Calories : <input
        type="number" name="calories"
        value="${meal.calories}" /> <br/>
    <td><input type="submit" name="submit" value="Submit"/></td>
    <td><input type="submit" name="submit" value="Cancel"/></td>
</form>
</body>
</html>
