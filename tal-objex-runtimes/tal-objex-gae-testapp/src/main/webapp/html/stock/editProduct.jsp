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
<title>Product</title>
</head>
<body>

<h3>Product</h3>
<form action="/sampleApp/submitProduct">
	<input type="hidden" name="id" value="${product.id}" />
	<input type="hidden" name="parentId" value="${product.parentId}" />
	<div><label for="name" style="width: 8em">Name: </label><input type="text" name="name" value="${product.name}" /></div>
	<div><label for="description" style="width: 8em">Description: </label><input type="text" name="description" value="${product.description}" /></div>
	
	<div>
		<label for="price" style="width: 8em">Price (Currency): </label>
		<input type="text" name="price" value="${product.price}" />
		<select name="currency">
			<option value="GBP" <c:if test="${product.currency eq 'GBP'}">selected="selected"</c:if> >British Pound</option>
			<option value="USD" <c:if test="${product.currency eq 'USD'}">selected="selected"</c:if> >US Dollar</option>
			<option value="EUR" <c:if test="${product.currency eq 'EUR'}">selected="selected"</c:if> >Euro</option>
		</select>
	</div>
	<div><label for="effectiveFrom" style="width: 8em">Effective From: </label><input type="text" name="effectiveFrom" value="${product.effectiveFrom}" /></div>
	<div><label for="effectiveTo" style="width: 8em">Effective To: </label><input type="text" name="effectiveTo" value="${product.effectiveTo}" /></div>
	<hr />
	<input type="submit" name="Submit" />
</form>

</body>
</html>