<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" isELIgnored="false" %>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@page import="org.tpspencer.tal.objexj.sample.api.repository.StockService"%>
<%@page import="org.tpspencer.tal.gaetest.StockServiceFactory"%>
<%@page import="org.tpspencer.tal.objexj.sample.api.repository.StockRepository"%>
<%@page import="org.tpspencer.tal.objexj.sample.api.stock.Category"%>
<%@page import="org.tpspencer.tal.gaetest.GAEObjexSampleApp"%>
<%@page import="org.tpspencer.tal.objexj.sample.api.stock.Product"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Stock</title>
</head>
<body>

<%
StockService stockService = StockServiceFactory.getInstance().getService();
StockRepository repository = stockService.getRepository();

GAEObjexSampleApp.SampleAppState state = (GAEObjexSampleApp.SampleAppState)request.getAttribute("state");

if( state == null || state.getCurrentCategory() == null ) {
	Category[] categories = repository.getRootCategories();
	pageContext.setAttribute("categories", categories);
}
else {
    Category cat = repository.findCategory(state.getCurrentCategory());
    pageContext.setAttribute("currentCategory", cat);
    pageContext.setAttribute("categories", cat.getCategories());
    // pageContext.setAttribute("categories", repository.findCategoriesByCategory(state.getCurrentCategory()));
	pageContext.setAttribute("products", repository.findProductsByCategory(state.getCurrentCategory()));
}
%>

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
	
	<c:forEach var="catId" items="${categories}">
		<% pageContext.setAttribute("category", repository.findCategory((String)pageContext.getAttribute("catId"))); %>
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