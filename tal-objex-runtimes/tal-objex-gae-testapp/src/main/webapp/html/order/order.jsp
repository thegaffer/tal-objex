<%--

    Copyright (C) 2011 Tom Spencer <thegaffer@tpspencer.com>

    This file is part of Objex <http://www.tpspencer.com/site/objexj/>

    Objex is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Objex is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Objex. If not, see <http://www.gnu.org/licenses/>.

    Note on dates: Objex was first conceived in 1997. The Java version
    first started in 2004. Year in copyright notice is the year this
    version was built. Code was created at various points between these
    two years.

--%>
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
		<c:if test="${state.inTransaction}"><th>Actions</th></c:if>
	</tr>
	
	<c:forEach var="item" items="${order.items}">
		<tr>
			<td>${item.ref}</td>
			<td>${item.name}</td>
			<td>${item.description}</td>
			<td>${item.quantity}&nbsp;${item.measure}</td>
			<td>${item.price}&nbsp;${item.currency}</td>
			<c:if test="${state.inTransaction}"><td>
				<a href="/sampleApp/removeItem?item=${item.id}">Remove</a>&nbsp;|
				<a href="/sampleApp/moveItem?item=${item.id}&dir=up">Up</a>&nbsp;|
				<a href="/sampleApp/moveItem?item=${item.id}&dir=down">Down</a>
			</td></c:if>
		</tr>
	</c:forEach>
</table>

<div>
<c:choose>
<c:when test="${state.inTransaction}">
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