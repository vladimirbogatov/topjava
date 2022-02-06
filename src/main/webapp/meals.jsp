<%--
  Created by IntelliJ IDEA.
  User: vladi
  Date: 04.02.2022
  Time: 21:02
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<html lang="ru">
<head>
    <title>Meals</title>
    <style>
        table, th, td {
            border: 1px solid black;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<table>
    <thead>
    <tr>
        <th colspan="7">Meals</th>
    </tr>
    <tr>
        <th>ID</th>
        <th>Date&Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th>Excess</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach var="meal" items="${meals}">
            <tr style="${meal.excess == true ? "color: red" : "color: forestgreen"}">
                <td>${meal.mealToId}</td>
                <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy hh:mm"))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td>${meal.excess}</td>
                <td><a href="meals?action=edit&mealToId=<c:out value="${meal.mealToId}"/>">Update</a> </td>
                <td><a href="meals?action=delete&mealToId=<c:out value="${meal.mealToId}"/>">Delete</a> </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add User</a></p>
</body>
</html>
