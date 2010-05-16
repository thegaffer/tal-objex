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
<title>Product</title>
</head>
<body>

<%
StockService stockService = StockServiceFactory.getInstance().getService();
StockRepository repository = stockService.getRepository();

GAEObjexSampleApp.SampleAppState state = (GAEObjexSampleApp.SampleAppState)request.getAttribute("state");

if( state == null || state.getCurrentProduct() == null ) {
    // TODO: Get the root category!!
}
else {
    pageContext.setAttribute("product", repository.findProduct(state.getCurrentProduct()));
}
%>

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