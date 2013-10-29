<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class=menu_position>
	<img class=tootajad_selected_pos src="${pageContext.request.contextPath}/images/tootajadselect.png">
</div>

<div class=menu_position>
<div class=tootajad_selected_pos>
<div class=menu_button_text> Töötajad</div>
</div>
</div>

<div id="tootajadTabelDiv">
<br>

<div>
	<table>
		<tr>
			<th class=activeTab ><a href="vaadeTootajadTabel.htm">Tabel</a></th>
			<th ><a href="vaadeTootajadGraaf.htm">Graaf</a></th>
		</tr>
	</table>
</div>

<br>

<select id="aastateValikud">
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

<select id="kvartaliteValikud">
	<c:forEach items="${kvartalid}" var="kvartal">
		<c:choose>
			<c:when test="${kvartal == hetkeKvartal}">
    			<option value="${kvartal}" selected>${kvartal}</option>
    		</c:when>
    		<c:otherwise>
    			<option value="${kvartal}">${kvartal}</option>
    		</c:otherwise>
    	</c:choose>
    </c:forEach>
</select>
 
<button type="button" id="salvestaTootajatePalgad" class="projektDetailNupp" >Salvesta valitud aasta palgad</button>
 
<table id="tootajateTabel">
	<tr>
		<th rowspan=3 colspan=2 class="tootajaTabelHeaderVäli">Töötajad</th>
		<th colspan=${kogupikkus} class="tootajaTabelHeaderVäli" >${hetkeKvartal}</th>
	</tr>
	<tr>
		<th colspan=${esimeneKuuNumber} class="tootajaTabelHeaderVäli">${esimeneKuu}</th>
		<th colspan=${teineKuuNumber} class="tootajaTabelHeaderVäli">${teineKuu}</th>
		<th colspan=${kolmasKuuNumber} class="tootajaTabelHeaderVäli">${kolmasKuu}</th>
	</tr>
	<tr>
		<th class="tootajaPalk" id="tootajaPalgaKuupaev1">
			<div class="tootajaPalgaMuutmine" style="display:none;">
				<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${palgaKuupaev1}" />
			</div>
			<div class="tootajaPalkText">${palgaKuupaev1}</div>
		</th>
		
		<c:forEach items="${kuupaevad1}" var="kuupaev1">
			<th class="tuluLahter">${kuupaev1}</th>
		</c:forEach>
		
		<th class="tootajaPalk" id="tootajaPalgaKuupaev2">
			<div class="tootajaPalgaMuutmine" style="display:none;">
				<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${palgaKuupaev2}" />
			</div>
			<div class="tootajaPalkText">${palgaKuupaev2}</div>
		</th>
		
		<c:forEach items="${kuupaevad2}" var="kuupaev2">
			<th class="tuluLahter">${kuupaev2}</th>
		</c:forEach>
		
		<th class="tootajaPalk" id="tootajaPalgaKuupaev3">
			<div class="tootajaPalgaMuutmine" style="display:none;">
				<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${palgaKuupaev3}" />
			</div>
			<div class="tootajaPalkText">${palgaKuupaev3}</div>
		</th>
		
		<c:forEach items="${kuupaevad3}" var="kuupaev3">
			<th class="tuluLahter">${kuupaev3}</th>
		</c:forEach>
		
	</tr>
	<c:forEach items="${kasutajad}" var="tootaja" >
	
		<tr class="tootajaRida">
			<td class="tootajaNimi">
				<div class="tootajaNimiAlam">
					<div class="kasutajaVanaNimeDiv">${tootaja.kasutajaNimi}</div>
					<div class="kasutajaUueNimeDiv" style="display:none;">
						<input class="uueKasutajaNimeLahter" value="${tootaja.kasutajaNimi}" />
						<input class="kasutajaId" value="${tootaja.kasutajaID}" type="hidden"/>
						<button type="button" class="projektDetailNupp kasutjaNimeMuutmiseNupp">muuda</button>
					</div>
				</div>
				<div style="display:none;" class="tootajaKustutajaComfirmation">
					<form:form modelAttribute="kustutaTootaja">
						<form:input type="hidden" path="kasutajaID" value="${tootaja.kasutajaID}" />
						<input type="submit" class="punaneProjektDetailNupp" value="Jah" />
						<button class="projektDetailNupp katkestaTootajaKustutamine" type="button" >Katkesta</button>
					</form:form>
				</div>
			</td>
			<td><input class="punaneProjektDetailNupp tootajaKustutaNupp" type="button" value="X" /></td>
			
			<td class="tootajaPalk">
				<div class="tootajaPalgaMuutmine" style="display:none;">
					<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${tootaja.tabeliAndmed.palgad[0]}" />
				</div>
				<div class="tootajaPalkText">${tootaja.tabeliAndmed.palgad[0]}</div>
			</td>
			
			<c:forEach items="${tootaja.tabeliAndmed.tulud1}" var="tulu1">
				<td class="tuluLahter">${tulu1}</td>
			</c:forEach>
			
			<td class="tootajaPalk">
				<div class="tootajaPalgaMuutmine" style="display:none;">
					<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${tootaja.tabeliAndmed.palgad[1]}" />
				</div>
				<div class="tootajaPalkText">${tootaja.tabeliAndmed.palgad[1]}</div>
			</td>
			
			<c:forEach items="${tootaja.tabeliAndmed.tulud2}" var="tulu2">
				<td class="tuluLahter">${tulu2}</td>
			</c:forEach>
			
			<td class="tootajaPalk">
				<div class="tootajaPalgaMuutmine" style="display:none;">
					<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${tootaja.tabeliAndmed.palgad[2]}" />
				</div>
				<div class="tootajaPalkText">${tootaja.tabeliAndmed.palgad[2]}</div>
			</td>
			
			<c:forEach items="${tootaja.tabeliAndmed.tulud3}" var="tulu3">
				<td class="tuluLahter">${tulu3}</td>
			</c:forEach>
		</tr>

	</c:forEach>
	<tr>
		<td colspan=12>
		<form:form modelAttribute="uusTootaja">
			<form:input maxlength="45" class="looUusInputLahter" path="kasutajaNimi" value="uus töötaja" />
			<input class="projektDetailNupp" type="submit" value="lisa töötaja">
		</form:form>
		</td>
	</tr>
</table>

</div>