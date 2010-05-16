<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Order</title>
</head>
<body>

<h3>Order</h3>

<div><b>Account:</b> ${order.account}</div>

<table width="100%">
	<tr>
		<th>Ref</th>
		<th>Name</th>
		<th>Description</th>
		<th>Quantity</th>
		<th>Price</th>
		<c:if test="${not empty state.currentTransactionId}"><th>Actions</th></c:if>
	</tr>
	
	<c:forEach var="item" items="${items}">
		<tr>
			<td>${item.ref}</td>
			<td>${item.name}</td>
			<td>${item.description}</td>
			<td>${item.quantity}&nbsp;${item.measure}</td>
			<td>${item.price}&nbsp;${item.currency}</td>
			<c:if test="${not empty state.currentTransactionId}"><td>
				<a href="/sampleApp/removeItem?item=${item.id}">Remove</a>&nbsp;|
				<a href="/sampleApp/moveItem?item=${item.id}&dir=up">Up</a>&nbsp;|
				<a href="/sampleApp/moveItem?item=${item.id}&dir=down">Down</a>
			</td></c:if>
		</tr>
	</c:forEach>
</table>

<div>
<c:choose>
<c:when test="${not empty state.currentTransactionId}">
<a href="/sampleApp/newOrderItem">New Item</a>&nbsp;|
<a href="/sampleApp/saveOrder">Save</a>
</c:when>
<c:otherwise>
<a href="/sampleApp/openOrder">Open</a>
</c:otherwise>
</c:choose>
</div>

</body>
</html>