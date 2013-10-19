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
<div class="teade">${teade}</div>

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
 
<button type="button" id="salvestaTootajatePalgad" class="projektDetailNupp" >Salvesta valitud aasta palgad</button>
 
<table id="tootajateTabel">
	<tr>
		<th rowspan=2 colspan=2 class="tootajaTabelHeaderVäli">Töötajad</th>
		<c:forEach items="${kuupaevad}" var="kuupaev">
			<th COLSPAN=2 class="tootajaTabelHeaderVäli">${kuupaev}</th>
		</c:forEach>
	</tr>
	<tr>
		<c:forEach items="${kuupaevad}" var="kuupaev">
			<th class="tootajaPalk">Palk</th>
			<th class="tuluLahter">Tulu</th>
		</c:forEach>
	</tr>
	<c:forEach items="${kasutajad}" var="tootaja" >
	
		<tr>
			<td class="tootajaNimi">
				<div class="kasutajaVanaNimeDiv">${tootaja.kasutajaNimi}</div>
				<div class="kasutajaUueNimeDiv" style="display:none;">
					<input class="uueKasutajaNimeLahter" value="${tootaja.kasutajaNimi}" />
					<input class="kasutajaId" value="${tootaja.kasutajaID}" type="hidden"/>
					<button type="button" class="projektDetailNupp kasutjaNimeMuutmiseNupp">muuda</button>
				</div>
			</td>
				<form:form modelAttribute="kustutaTootaja">
					<form:input type="hidden" path="kasutajaID" value="${tootaja.kasutajaID}" />
			<td>
					<input class="punaneProjektDetailNupp" type="submit" value="X" />
			</td>
				</form:form>
			<c:forEach items="${tootaja.tabeliAndmed}" var="yhik">
				<td class="tootajaPalk">
					<div class="tootajaPalgaMuutmine" style="display:none;">
						<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${yhik.palk}" />
					</div>
					<div class="tootajaPalkText">${yhik.palk}</div>
				</td>
				<td class="tuluLahter">${yhik.tulu}</td>
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