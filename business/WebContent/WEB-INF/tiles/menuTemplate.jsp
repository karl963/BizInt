<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@include file="/WEB-INF/jsp/include.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

</head>  
<body>
<c:choose>
      <c:when test="${teade != null}">
      	<div class="teadeÜlevalProjektid teadeHajuvKadumine">${teade}</div>
      </c:when>
</c:choose>
	<tiles:insertAttribute name="header"/>
	<tiles:insertAttribute name="menu" />
	<tiles:insertAttribute name="body" />
</body>  
</html>
