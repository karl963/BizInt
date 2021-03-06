<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class=menu_position>
	<img class=projekt_selected_pos src="${pageContext.request.contextPath}/images/projektselect.png">
</div>

<div class=menu_position>
<div class=menu_button_text> Projektid</div>
</div>

<div class=textontop>

<div class="staatusteKonteiner staatuseDrag">

<c:forEach items="${staatused}" var="staatus">

 	<div id="${staatus.id}+${staatus.järjekorraNumber}" class="dropZoneStaatused divNextToEachOther">
 	
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
	
	<div id="${staatus.id}" class="scrollwindow">
	<c:forEach items="${staatus.projektid}" var="projekt">

		<div id="${staatus.id}+${projekt.id}+${projekt.projektiJärjekorraNumber}" class="dropZoneProjekt projektdiv">
			<div class="projektIdDiv" style="display:none;">${projekt.id}</div>
			
			<table class="project">
				<tr class="projektLink">
					<th COLSPAN=2 style="background-color:#74CEFF">${projekt.nimi}</th>
				</tr>
				<tr>
					<td >Vastutaja:</td>
					<td class="staatusVastutajaTd" >
						<div class="staatusVastutajaText" >${projekt.vastutaja.kasutajaNimi}</div>
						<div class="staatusVastutajaDiv" style="display:none;">
							<div class="vastutajaProjektID" style="display:none;">${projekt.id}</div>
							<select class="staatusVastutajaValimine">
								<option selected value="valige töötaja" disabled>valige töötaja</option>
								<c:forEach items="${kasutajad}" var="kasutaja">
									<c:choose>
										<c:when test="${kasutaja == projekt.vastutaja.kasutajaNimi}">
											<option selected value="${kasutaja}">${kasutaja}</option>
										</c:when>
										<c:otherwise>
											<option value="${kasutaja}">${kasutaja}</option>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</select>
						</div>
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

	<div id="lastStaatusDiv" class="divNextToEachOther" ></div>
	<div class="statustext divNextToEachOther newStatus UueStaatuseDiv">
		
			<form:form modelAttribute="uusStaatus">
				<form:input maxlength="45" class="uusProjekt looUusInputLahter" path="nimi" />
				<input type="submit" value="+" class="projektDetailNupp"/><!-- style="background:url(${pageContext.request.contextPath}/images/addbutton.png) no-repeat;" />-->
					<!-- <img class=addbutton src="${pageContext.request.contextPath}/images/addbutton.png">-->
			</form:form>
		
	</div>
	
	</div>
</div>


