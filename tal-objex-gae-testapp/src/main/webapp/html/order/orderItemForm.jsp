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
<form action="/sampleApp/submitOrderItem">
	<c:if test="${newOrderItem}">
	<input type="hidden" name="parentId" value="${order.id}" />
	</c:if>
	<c:if test="${!newOrderItem}">
	<input type="hidden" name="id" value="${item.id}" />
	<input type="hidden" name="parentId" value="${item.parentId}" />
	</c:if>
	
	<div><label for="ref">Ref: </label><input type="text" name="ref" value="${item.ref}" readonly="readonly"/></div>
	<div><label for="name">Name: </label><input type="text" name="name" value="${item.name}" /></div>
	<div><label for="description">Description: </label><input type="text" name="description" value="${item.description}" /></div>
	
	<div><label for="quantity">Quantity: </label><input type="text" name="quantity" value="${item.quantity}" /><input type="text" name="measure" value="${item.measure}" /></div>
	
	<div><label for="price">Price: </label><input type="text" name="price" value="${item.price}" /><input type="text" name="currency" value="${item.currency}" /></div>
	
	<hr />
	
	<c:if test="${item.stockItem != null}">
	<div><a href="/sampleApp/viewStockItem?item=${item.stockItem}">Stock Item</a></div>
	</c:if>
	
	<hr />
	<input type="submit" value="Submit" />
</form>

</body>
</html>