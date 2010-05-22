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
<form action="/sampleApp/submitOrder">
	<input type="hidden" name="id" value="${order.id}" />
	
	<div><label for="account">Account: </label><input type="text" name="account" value="${order.account}" /></div>

	<hr />
	<input type="submit" value="Submit" />
</form>

</body>
</html>