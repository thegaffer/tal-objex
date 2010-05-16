<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="org.tpspencer.tal.objexj.sample.api.stock.Category"%>
<%@page import="org.tpspencer.tal.gaetest.GAEObjexSampleApp"%>
<%@page import="org.tpspencer.tal.objexj.sample.api.repository.StockRepository"%>
<%@page import="org.tpspencer.tal.objexj.sample.api.repository.StockService"%>
<%@page import="org.tpspencer.tal.gaetest.StockServiceFactory"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>New Product</title>
</head>
<body>

<h3>New Product</h3>
<form action="/sampleApp/submitProduct">
	<input type="hidden" name="parentId" value="${state.currentCategory}" />
	<div><label for="name" style="width: 8em">Name: </label><input type="text" name="name" value="" /></div>
	<div><label for="description" style="width: 8em">Description: </label><input type="text" name="description" value="" /></div>
	
	<div>
		<label for="price" style="width: 8em">Price (Currency): </label>
		<input type="text" name="price" value="" />
		<select name="currency">
			<option value="GBP">British Pound</option>
			<option value="USD">US Dollar</option>
			<option value="EUR">Euro</option>
		</select>
	</div>
	<div><label for="effectiveFrom" style="width: 8em">Effective From: </label><input type="text" name="effectiveFrom" value="" /></div>
	<div><label for="effectiveTo" style="width: 8em">Effective To: </label><input type="text" name="effectiveTo" value="" /></div>
	<hr />
	<input type="submit" name="Submit" />
</form>

</body>
</html>