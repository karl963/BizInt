<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="loginFormDiv">
	
	<b>${teade}</b>
	<c:choose>
  		<c:when test="${sessionScope.kasutajaNimi == null}">
  			<form:form modelAttribute="login">
				<form:input path="kasutajaNimi" />
				<form:password path="parool" />
				<input type="submit" value="Logi sisse" />
			</form:form>
  		</c:when>
  		<c:otherwise>
  			<p>Te olete juba sisse loginud kasutajaga: ${sessionScope.kasutajaNimi}</p>
  			<br>
  			<a href="vaadeProjektid.htm" >projektide vaatesse</a>
  		</c:otherwise>
	</c:choose>

</div>