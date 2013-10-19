<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="loginFormDiv">
	<b>${teade}</b>

	<c:choose>
  		<c:when test="${sessionScope.kasutajaNimi == null}">
  			<form:form modelAttribute="login">
				<form:input class="nimelahter looUusInputLahter" path="kasutajaNimi" />
				<form:password class="paroolilahter looUusInputLahter" path="parool" value="kasutajanimi"/>
				<input class="loginupp projektDetailNupp" type="submit" value="Logi sisse" />
			</form:form>
  		</c:when>
  		<c:otherwise>
  			<p>Te olete juba sisse loginud kasutajaga: ${sessionScope.kasutajaNimi}</p>
  			<br>
  			<a href="vaadeProjektid.htm" >projektide vaatesse</a>
  		</c:otherwise>
	</c:choose>
	<br>
	<a href="https://github.com/karl963/BizInt/blob/master/business/Release%20notes" target="_blank">vaata ka Release Notes</a>
	
</div>