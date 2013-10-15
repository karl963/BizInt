<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<br><br><br><br><br>

${teade}

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
 
<table border=1 id="tootajateTabel">
	<tr>
		<th>Töötajad</th>
		<th> </th>
		<c:forEach items="${kuupaevad}" var="kuupaev">
			<th COLSPAN=2>${kuupaev}</th>
		</c:forEach>
	</tr>
	<tr>
		<th></th>
		<th></th>
		<c:forEach items="${kuupaevad}" var="kuupaev">
			<th>Palk</th>
			<th>Tulu</th>
		</c:forEach>
	</tr>
	<c:forEach items="${kasutajad}" var="tootaja" >
	
		<form:form modelAttribute="kustutaTootaja">
			<form:input type="hidden" path="kasutajaID" value="${tootaja.kasutajaID}" />
		<tr>
			<td class="tootajaNimi">${tootaja.kasutajaNimi}</td>
			<td><input type="submit" value="X" /></td>
			<c:forEach items="${tootaja.tabeliAndmed}" var="yhik">
				<td class="tootajaPalk" style="width:40px">
					<div class="tootajaPalgaMuutmine" style="display:none;">
						<input style="width:40px" class="tootajaPalgaMuutmiseLahter" value="${yhik.palk}" />
					</div>
					<div class="tootajaPalkText">${yhik.palk}</div>
				</td>
				<td>${yhik.tulu}</td>
			</c:forEach>
		</tr>
		
		</form:form>
	</c:forEach>
	<tr>
		<td>
		<form:form modelAttribute="uusTootaja">
			<form:input maxlength="45" class="looUusInputLahter" path="kasutajaNimi" value="uus töötaja" />
			<input type="submit" value="lisa töötaja">
		</form:form>
		</td>
	</tr>
</table>

<br>

<button type="button" id="salvestaTootajatePalgad" >Salvesta palgad</button>
