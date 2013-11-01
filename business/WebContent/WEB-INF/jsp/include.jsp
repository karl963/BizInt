<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page session="false"%>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles"%> 

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"  
"http://www.w3.org/TR/html4/loose.dtd">

<html>  
<head>  

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">  

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/projektiStiil.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/menuStiil.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/pipelineStiil.css" type="text/css" />
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginStiil.css" type="text/css" />

<script>var rakenduseNimi = "${pageContext.request.contextPath}"</script>
<script src="${pageContext.request.contextPath}/scripts/jquery-1.10.2.js" type="text/javascript" ></script>
<script src="${pageContext.request.contextPath}/scripts/jquery-ui-1.10.3.custom.js" type="text/javascript" ></script>
<script src="https://www.google.com/jsapi" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/scripts/projekt.js" type="text/javascript" ></script>

<link rel="shortcut icon" href="${pageContext.request.contextPath}/images/bizint2.ico">

<title><tiles:insertAttribute name="title" ignore="true" /> </title>  
