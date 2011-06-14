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
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Stock</title>
</head>
<body>

<h4>Stock</h4>
<table>
	<tr>
		<th>Type</th>
		<th>Name</th>
		<th>Description</th>
		<th>Actions</th>
	</tr>
	
	<c:if test="${categories == null && products == null}">
		<tr><td colspan="4">There are not categories or stock to display</td></tr>
	</c:if>
	
	<c:forEach var="category" items="${categories}">
		<tr>
			<td>Category</td>
			<td>${category.name}</td>
			<td>${category.description}</td>
			<td>
				<a href="/sampleApp/viewCategory?category=${category.id}">View</a>&nbsp;|
				<a href="/sampleApp/editCategory?category=${category.id}">Edit</a>&nbsp;|
				<a href="/sampleApp/deleteCategory?category=${category.id}">Delete</a>
			</td>
		</tr>
	</c:forEach>
	
	<c:forEach var="product" items="${products}">
		<tr>
			<td>Product</td>
			<td>${product.name}</td>
			<td>${product.description}</td>
			<td>
				<a href="/sampleApp/editProduct?product=${product.id}">Edit</a>&nbsp;|
				<a href="/sampleApp/deleteProduct?product=${product.id}">Delete</a>
			</td>
		</tr>
	</c:forEach>
</table>

<div>
<c:if test="${currentCategory.parentId != null}"><span><a href="/sampleApp/viewCategory?category=${currentCategory.parentId}">Parent Category</a></span>&nbsp;|</c:if>
<c:if test="${currentCategory != null }"><span><a href="/sampleApp/newProduct">New Product</a></span>&nbsp;|</c:if>
<span><a href="/sampleApp/newCategory">New Category</a></span>

</div>


</body>
</html>