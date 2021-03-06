<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class=menu_position>
	<img class=arhiiv_selected_pos src="${pageContext.request.contextPath}/images/arhiivselect.png">
</div>

<div class=menu_position>
<div class=arhiiv_selected_pos>
<div class=menu_button_text>Arhiiv</div>
</div>
</div>

<div class=textontop>

<div class=message>
	<c:if test="${not empty teade}"><div class="message green">${teade}</div></c:if>
</div>

<select id="arhiivAastaValik">
	<c:forEach items="${aastad}" var="aasta">
		<c:choose>
			<c:when test="${aasta == hetkeAasta}">
    			<option value="${aasta}" selected>${aasta}</option>
    		</c:when>
    		<c:otherwise>
    			<option value="${aasta}">${aasta}</option>
    		</c:otherwise>
    	</c:choose>
    </c:forEach>
</select>

<div class=staatusteKonteiner>

<c:forEach items="${staatused}" var="staatus">

<div class="divNextToEachOther">
	
	<div class=statustext>
		<div class="staatuseNimiArhiivis">
			<div class="staatuseNimeKiri">${staatus.nimi}</div>
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
	</div>

	<div id="${staatus.id}" class="dropZoneArhiiv scrollwindow">
	<c:forEach items="${staatus.projektid}" var="projekt">

		<div id="${staatus.id}+${projekt.id}+${projekt.projektiJärjekorraNumber}" class="arhiiviProjektDrag projektdiv">
			
			<div class="projektIdDiv" style="display:none;">${projekt.id}</div>
			
			<table class="project">
				<tr class="projektArhiivisLink">
					<th COLSPAN=2 style="background-color:#74CEFF">${projekt.nimi}</th>
				</tr>
				<tr>
					<td >Vastutaja:</td>
					<td class="staatusVastutajaTd2" >
						<div class="staatusVastutajaText" >${projekt.vastutaja.kasutajaNimi}</div>
					</td>
				</tr>
				<tr class="projektLink">
					<td>Tulu:</td>
					<td>${projekt.kogutulu}</td>
				</tr>
			</table>
			<p></p>
			</div>
		</c:forEach>

	</div>
	
</div>
</c:forEach>


	
	</div>
</div>


