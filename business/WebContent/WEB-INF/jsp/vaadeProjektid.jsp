<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>


<div class=textontop>

<div class=staatusteKonteiner>

<c:if test="${not empty message}"><div class="message green">${message}</div></c:if>

<c:forEach items="${staatused}" var="staatus">

<div class=divNextToEachOther>
	
	<div class=statustext>
		${staatus.nimi}<br>
		${staatus.kogutulu}
	</div>

	<div class=scrollwindow>
		<c:forEach items="${staatus.projektid}" var="projekt">
			<a href="vaadeProjektEsimene.htm">
			<table class=project>
				<tr>
					<th COLSPAN=2>${projekt.nimi}</th>
				</tr>
				<tr>
					<th>Vastutaja:</th>
					<th>${projekt.vastutaja.nimi}</th>
				</tr>
				<tr>
					<th>Tulu:</th>
					<th>${projekt.kogutulu}</th>
				</tr>
			</table>
			</a>
			<p></p>
		</c:forEach>
		
		<table class=project>
			<form:form modelAttribute="uusProjekt">
				<tr>
					<form:input path="nimi" />
					<form:input path="staatusID" value="${staatus.id}" type="hidden"/>
					<th><input class=addbutton type="submit" value="Lisa" src="${pageContext.request.contextPath}/images/addbutton.png" /></th>
				</tr>
			</form:form>
		</table>
	</div>
	
</div>
</c:forEach>



	<div class="statustext">
		<div class=statusdivider>
			<form:form modelAttribute="uusStaatus">
				<form:input path="nimi" />
					<input class=addbutton type="submit" value="Lisa" src="${pageContext.request.contextPath}/images/addbutton.png" />
					<!-- <img class=addbutton src="${pageContext.request.contextPath}/images/addbutton.png">-->
			</form:form>
		</div>
	</div>
	
	</div>
</div>
