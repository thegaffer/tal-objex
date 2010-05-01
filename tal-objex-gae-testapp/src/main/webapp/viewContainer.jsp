<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<%@page import="org.tpspencer.tal.objexj.locator.SimpleContainerLocator"%>
<%@page import="org.tpspencer.tal.gaetest.config.GAETestAppConfig"%>
<%@page import="org.tpspencer.tal.gaetest.model.BookingBean"%>
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
Container bookingDoc = containerLocator.get(request.getParameter("id"));
BookingBean booking = bookingDoc.getObject("BookingBean|1").getStateObject(BookingBean.class);
%>

<span>Persisted Account: <%= booking.getAccount()%></span>

</body>
</html>