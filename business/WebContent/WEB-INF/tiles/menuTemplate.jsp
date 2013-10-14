<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@include file="/WEB-INF/jsp/include.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  
"http://www.w3.org/TR/html4/loose.dtd">

<html>  
<head>  
	<script>var rakenduseNimi = "${pageContext.request.contextPath}"</script>
	<script src="${pageContext.request.contextPath}/scripts/menu.js" type="text/javascript" ></script>
	<script src="${pageContext.request.contextPath}/scripts/jquery-1.10.2.js" type="text/javascript" ></script>
	<script type="text/javascript" src="https://www.google.com/jsapi"></script>
	<script src="${pageContext.request.contextPath}/scripts/projekt.js" type="text/javascript" ></script>
	
	<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/bizint2.ico">

</head>  
<body>
	<tiles:insertAttribute name="header"/>
	<tiles:insertAttribute name="menu" />
	<tiles:insertAttribute name="body" />
	<tiles:insertAttribute name="footer" />
</body>  
</html>
