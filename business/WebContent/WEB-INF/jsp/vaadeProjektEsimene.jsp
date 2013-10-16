<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<p>${teade}</p>
<body class=darkBack>
<div class=detailViewDiv>

<div class=detailViewHeader >

		<div style="display:none" id="projektiNimeMuutmine" >
			<form:form modelAttribute="uusProjektiNimi">
				<form:input maxlength="45" path="uusNimi" style="width:330px"/>
				<form:input path="projektID" type="hidden" />
				<input class="projektDetailNupp" type="submit" value="muuda nime" />
			</form:form>
		</div>
		

		<h1 id="projektiNimi" >${projekt.nimi}</h1>

		<div class=muudaKustuta>
		
			<button class="projektDetailNupp" type="button" id="projektiNimeMuutmiseNupp" >Muuda nime</button>
			<button class="projektDetailNupp" type="button" onclick="javascript:document.kustutaProjekt.submit()" >Kustuta projekt</button>
		</div>

<form name="kustutaProjekt" method="POST" action="vaadeProjektEsimene.htm">
	<input type="hidden" name="projektID" value="${projekt.id}">
	<input type="hidden" name="kustuta" value="jah">
</form>

<div class=tuluKulu >
Tulu: <b class="tuluSumma">${projekt.kogutulu}</b> Kulu: <b class="kuluSumma">${projekt.kogukulu}</b>
</div>

<div class=reiting>

Reiting: ${projekt.reitinguHTML}

</div>


<div class=viewTabs>
<table>
<tr>
<th class=activeTab>Üldine</th>
<th><a href="vaadeProjektTeine.htm?id=${projekt.id}">Tulud ja kulud</a></th>
</tr>
</table>

</div>
</div>

<div class=leftSideDiv>

<b>Projektiga seotud inimesed:</b>
<br>

<select id="uueKasutajaList">
<c:forEach items="${kasutajad}" var="kasutaja">
	<option value="${kasutaja.kasutajaNimi}">${kasutaja.kasutajaNimi}</option>
</c:forEach>
</select>
<input class="projektDetailNupp" type="button" onclick="lisaProjektiKasutaja(${projekt.id});" value="lisa" />


<div class=describe>

<table class="table" id="kasutajateTabel" >
	<tr>
		<th class="vastutajaVeerg">Vastuaja</th>
		<th class="aktiivneVeerg">Aktiivne</th>
		<th class="nimeVeerg">Nimi</th>
		<th class="osalusVeerg">Osalus</th>
		<th class="tyhiVeerg"></th>
	</tr>

	<c:forEach items="${projekt.kasutajad}" var="kasutaja">
		<tr>

				<td>
					<c:choose>
						<c:when test="${kasutaja.vastutaja=='true'}"><input type="checkbox" checked class="tootajaVastutajaCheckbox"/></c:when>
						<c:otherwise><input type="checkbox" class="tootajaVastutajaCheckbox"/></c:otherwise>
					</c:choose>
				</td>
				<td>
					<c:choose>
						<c:when test="${kasutaja.aktiivne=='true'}"><input type="checkbox" checked /></c:when>
						<c:otherwise><input type="checkbox"/></c:otherwise>
					</c:choose>
				<td>
					${kasutaja.kasutajaNimi}
				</td>
				<td>
					<input maxlength="5" value="${kasutaja.osalus}" />
				</td>
				<td style="display:none;"><input value="${kasutaja.kasutajaID}" /></td>
				
			<form:form modelAttribute="eemaldaKasutaja">
				<form:input type="hidden" path="kasutajaID" value="${kasutaja.kasutajaID}" />
				<form:input type="hidden" path="projektID" value="${projekt.id}" />
				<td><button class="projektDetailNupp" type="submit">Eemalda</button></td>
			</form:form>
		</tr>
	</c:forEach>
</table>
</div>
<div class=buttonAlign>
<button class="projektDetailNupp" type="button" onclick="listiProjektiKasutajadJaSalvesta(${projekt.id});" >Salvesta töötajad</button>
</div>
<br>
<b>Kirjeldus:</b>

<form:form modelAttribute="uusKirjeldus">

<table>
	<tr>
		<td><textarea name="kirjeldus" id="kirjelduseVali" >${projekt.kirjeldus}</textarea></td>
		<td class="viiAlla"><input class="projektDetailNupp addbutton" type="submit" value="muuda"/></td>
	</tr>
	<form:input path="projektID" value="${projekt.id}" type="hidden" />
</table>


</form:form>

<table class="logiTabel">
	<tr>
		<td>Logi</td>
	</tr>
	<c:forEach items="${projekt.logi}" var="logi">
	<tr>
		<td>${logi.sonum} <i><small>( ${logi.formaaditudAeg} )</small></i></td>
	</tr>
	</c:forEach>
</table>

</div>
<div class=rightSideDiv>

<form:form modelAttribute="uusKommentaar">
<table>
	<tr>
		<td>
			<textarea name="sonum" class="kommentaariVali"/></textarea>
			<form:input path="projektID" value="${projekt.id}" type="hidden"/>
		</td>
		<td>
			<input class="projektDetailNupp" type="submit" value="kommenteeri" />
		</td>
	</tr>
</table>
</form:form>

<table class="kommentaariTabel">
	<tr>
		<td>Kommentaarid</td>
	</tr>
	<c:forEach items="${projekt.kommentaarid}" var="kommentaar">
	<tr>
		<td>${kommentaar.sonum} <small><i>(${kommentaar.kasutaja.kasutajaNimi} - ${kommentaar.formaaditudAeg})</i></small></td>
	</tr>
	</c:forEach>
</table>

</div>
</div>
<a href="vaadeProjektid.htm">
<div class=closeView >X</div>
</a>
</body>