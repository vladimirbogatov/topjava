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
        <th colspan="5">Meals</th>
    </tr>
    <tr>
        <th>Date&Time</th>
        <th>Description</th>
        <th>Calories</th>
        <th colspan=2>Action</th>
    </tr>
    </thead>
    <tbody>
        <c:forEach var="meal" items="${meals}">
            <tr style="${meal.excess ? "color: red" : "color: forestgreen"}">
                <td>${meal.dateTime.format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm"))}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=edit&id=${meal.id}">Update</a> </td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a> </td>
            </tr>
        </c:forEach>
    </tbody>
</table>
<p><a href="meals?action=insert">Add meal</a></p>
</body>
</html>
