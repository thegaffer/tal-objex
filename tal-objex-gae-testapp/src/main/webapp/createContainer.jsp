<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.tpspencer.tal.objexj.locator.SimpleContainerLocator"%>
<%@page import="org.tpspencer.tal.gaetest.config.GAETestAppConfig"%>
<%@page import="org.tpspencer.tal.gaetest.model.BookingBean"%>
<%@page import="org.tpspencer.tal.objexj.EditableContainer"%>
<%@page import="org.tpspencer.tal.objexj.Container"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Container Test</title>
</head>
<body>

<!-- Setup/Initialisation Code -->
<%
SimpleContainerLocator containerLocator = GAETestAppConfig.getInstance().getContainerLocator();

BookingBean booking = new BookingBean();
booking.setAccount("123");

EditableContainer store = containerLocator.create("Booking", booking);
String id = store.getId();
store.saveContainer();
%>

<div>Container Created</div>
<div>Container ID: <%= id%></div>
<div><a href="viewContainer.jsp?id=<%= id%>">View Container</a></div>

</body>
</html>