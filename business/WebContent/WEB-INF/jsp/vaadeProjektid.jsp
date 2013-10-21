<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class=menu_position>
	<img class=projekt_selected_pos src="${pageContext.request.contextPath}/images/projektselect.png">
</div>

<div class=menu_position>
<div class=menu_button_text> Projektid</div>
</div>

<div class=textontop>

<div class=message>
	<c:if test="${not empty teade}"><div class="message green">${teade}</div></c:if>
</div>

<div class=staatusteKonteiner>

<c:forEach items="${staatused}" var="staatus">

<div class=divNextToEachOther>
	
	<div class=statustext>
		<div class="staatuseNimi">
			<div class="staatuseNimeKiri">${staatus.nimi}</div>
			<div class="staatuseNimeMuutmine" style="display:none;">
			
				<form:form modelAttribute="staatuseNimeMuutmine">
					<form:input maxlength="45" class="staatuseNimeMuutmiseLahter" path="nimi" value="${staatus.nimi}" />
					<form:input type="hidden" path="id" value="${staatus.id}" />
					<input type="submit" class="projektDetailNupp" value="muuda" />
				</form:form>
				
				<button type="button" class="punaneProjektDetailNupp kustutaStaatusNupp" >kustuta staatus</button>
				
			</div>
			<div class="kustutaStaatusConfirmationDiv" style="display:none;">
				
				<form:form modelAttribute="staatuseKustutamine">
				
					<form:input type="hidden" path="id" value="${staatus.id}" />
					<form:input type="hidden" path="kustuta" value="jah" />
					
					<b>Kustuta staatus ?</b>
					<br>
					<input class="punaneProjektDetailNupp" type="submit" value="Jah" />
					<button class="projektDetailNupp katkestaStaatuseKustutamine" type="button" >Katkesta</button>
					
				
				</form:form>
			</div>
		</div>
		<br>
		<div class=tulutext>
			&euro; ${staatus.kogutulu}
		</div>

		<form:form modelAttribute="uusProjekt">
			<tr>
				<td>
					<form:input maxlength="45" class="uusProjekt looUusInputLahter" path="nimi" />
					<form:input path="staatusID" value="${staatus.id}" type="hidden"/>
					<input type="submit" class="projektDetailNupp" value="+" /><!--style="background:url(${pageContext.request.contextPath}/images/addbutton.png) no-repeat;" />-->
				</td>
			</tr>
		</form:form>
	</div>

	<div class="scrollwindow" ondragenter="return dragEnter(event,${staatus.id})" ondragover="return dragOver(event)" ondrop="return dragDrop(event)" >
	
		<c:forEach items="${staatus.projektid}" var="projekt">
		<div draggable="true" ondragstart="return dragStart(event,${projekt.id})">
			<a href="vaadeProjektEsimene.htm?id=${projekt.id}">
			<table class=project>
				<tr>
					<th COLSPAN=2 style="background-color:#74CEFF">${projekt.nimi}</th>
				</tr>
				<tr>
					<td>Vastutaja:</td>
					<td>${projekt.vastutaja.kasutajaNimi}</td>
				</tr>
				<tr>
					<td>Tulu:</td>
					<td>${projekt.kogutulu}</td>
				</tr>
			</table>
			</a>
			<p></p>
			</div>
		</c:forEach>
		
	</div>
	
</div>
</c:forEach>

	<div class="statustext divNextToEachOther newStatus">
		
			<form:form modelAttribute="uusStaatus">
				<form:input maxlength="45" class="uusProjekt looUusInputLahter" path="nimi" />
				<input type="submit" value="+" class="projektDetailNupp"/><!-- style="background:url(${pageContext.request.contextPath}/images/addbutton.png) no-repeat;" />-->
					<!-- <img class=addbutton src="${pageContext.request.contextPath}/images/addbutton.png">-->
			</form:form>
		
	</div>
	
	</div>
</div>


