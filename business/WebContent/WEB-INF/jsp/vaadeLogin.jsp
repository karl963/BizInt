<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class="loginFormDiv">
	<b>${teade}</b>

	<c:choose>
  		<c:when test="${sessionScope.kasutajaNimi == null}">
  			<form:form modelAttribute="login">
				<form:input class="nimelahter looUusInputLahter" path="kasutajaNimi" />
				<form:password class="paroolilahter looUusInputLahter" path="parool" value="parool"/>
				<input class="loginupp projektDetailNupp" type="submit" value="Logi sisse" />
			</form:form>
  		</c:when>
  		<c:otherwise>
  		  
  			<p>Te olete juba sisse loginud kasutajaga: ${sessionScope.kasutajaNimi}</p>
  			<br>
  			Kui teid 5 sekundi jooksul automaatselt edasi ei suunata, siis vajutage <a href="vaadeProjektid.htm" >siia (projektide vaatesse)</a>
  			<br>
  			
  			<c:choose>
  				<c:when test="${sessionScope.suunatudLink == null}">
  					<head><meta http-equiv="refresh" content="0; url=vaadeProjektid.htm"></head> <!-- auto-suunamine -->
  				</c:when>
  				<c:otherwise>
  					<head><meta http-equiv="refresh" content="0; url=${sessionScope.suunatudLink}"></head> <!-- auto-suunamine -->
  				</c:otherwise>
  			</c:choose>
  			
  		</c:otherwise>
	</c:choose>
	<div style="color:red;">Külaliskasutaja andmed:
		<br>
		<b><big>user:</big></b> user &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; <b><big>parool:</big></b> pw
	</div>
	<a href="https://github.com/karl963/BizInt/blob/master/business/Release%20notes" target="_blank">vaata ka Release Notes</a>
</div>