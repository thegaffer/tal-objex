<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Category</title>
</head>
<body>

<h3>Edit Category</h3>
<form action="/sampleApp/submitCategory">
	<input type="hidden" name="id" value="${category.id}" />
	<input type="hidden" name="parentId" value="${category.parentId}" />
	<div><label for="name" style="width: 8em">Name: </label><input type="text" name="name" value="${category.name}" /></div>
	<div><label for="description" style="width: 8em">Description: </label><input type="text" name="description" value="${category.description}" /></div>
	<hr />
	<input type="submit" name="Submit" />
</form>

</body>
</html>