<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class=textontop>

<div class=message>
	<c:if test="${not empty message}"><div class="message green">${message}</div></c:if>
</div>

<div class=staatusteKonteiner>

<c:forEach items="${staatused}" var="staatus">

<div class=divNextToEachOther>
	
	<div class=statustext>
		${staatus.nimi}<br>
		<div class=tulutext>
			&euro; ${staatus.kogutulu}
		</div>
	</div>
	

	<div class=scrollwindow>
	
		<table class=project>
			<form:form modelAttribute="uusProjekt">
				<tr>
					<td>
						<form:input class="uusProjekt" path="nimi" />
						<form:input path="staatusID" value="${staatus.id}" type="hidden"/>
						<input class=addbutton type="submit" value="+" style="background:url(${pageContext.request.contextPath}/images/addbutton.png) no-repeat;" />
					</td>
				</tr>
			</form:form>
		</table>
		<br>
		<c:forEach items="${staatus.projektid}" var="projekt">
			<a href="vaadeProjektEsimene.htm?id=${projekt.id}">
			<table class=project>
				<tr>
					<th COLSPAN=2>${projekt.nimi}</th>
				</tr>
				<tr>
					<td>Vastutaja:</td>
					<td>${projekt.vastutaja.nimi}</td>
				</tr>
				<tr>
					<td>Tulu:</td>
					<td>${projekt.kogutulu}</td>
				</tr>
			</table>
			</a>
			<p></p>
		</c:forEach>
		
	</div>
	
</div>
</c:forEach>

	<div class="statustext divNextToEachOther">
		<div class=statusdivider>
			<form:form modelAttribute="uusStaatus">
				<form:input class="uusProjekt" path="nimi" />
				<input class=addbutton type="submit" value="+" style="background:url(${pageContext.request.contextPath}/images/addbutton.png) no-repeat;" />
					<!-- <img class=addbutton src="${pageContext.request.contextPath}/images/addbutton.png">-->
			</form:form>
		</div>
	</div>
	
	</div>
</div>
