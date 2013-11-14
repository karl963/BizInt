<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<body class=darkBack>

<c:choose>
      <c:when test="${teade != null}">
      	<div class="teadeÜlevalProjektDetail teadeHajuvKadumine">${teade}</div>
      </c:when>
</c:choose>

<div class=detailViewDiv>

<div class=detailViewHeader >

		<div style="display:none" id="projektiNimeMuutmine" >
			<form:form modelAttribute="uusProjektiNimi">
				<form:input class="projektiNimeLahter" maxlength="45" path="uusNimi"/>
				<form:input path="projektID" type="hidden" />
				<input class="projektDetailNupp" type="submit" value="muuda nime" />
			</form:form>
		</div>
		

		<h1 id="projektiNimi" >${projekt.nimi}</h1>

		<div class="muudaKustuta muudaKustutaEsimene">
			<button class="projektDetailNupp" type="button" id="projektiNimeMuutmiseNupp" >Muuda nime</button>
			<button class="punaneProjektDetailNupp kustutaProjektNupp" type="button" >Kustuta projekt</button>
		</div>
		<div class="muudaKustuta muudaKustutaTeine" style="display:none;">
			<b>Kustuta Projekt ? </b>
			<button class="punaneProjektDetailNupp" type="button" onclick="javascript:document.kustutaProjekt.submit()" >Jah</button>
			<button class="projektDetailNupp katkestaProjektiKustutamine" type="button" >Katkesta</button>
		</div>


<form name="kustutaProjekt" method="POST" action="vaadeProjektArhiivisEsimene.htm">
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
<th><a href="vaadeProjektArhiivisTeine.htm?id=${projekt.id}">Tulud ja kulud</a></th>
</tr>
</table>

</div>
</div>

<div class=leftSideDiv>

<b>Projektiga seotud inimesed:</b>
<br>

<div class=describe>

<table class="table" id="kasutajateTabel" >
	<tr>
		<th class="vastutajaVeerg">Vastuaja</th>
		<th class="aktiivneVeerg">Aktiivne</th>
		<th class="nimeVeerg">Nimi</th>
		<th colspan=2 class="osalusVeerg">Osalus</th>
	</tr>

	<c:forEach items="${projekt.kasutajad}" var="kasutaja">
		<tr>

				<td class="vastutajaVeerg">
					<c:choose>
						<c:when test="${kasutaja.vastutaja=='true'}"><input type="checkbox" checked class="tootajaVastutajaCheckbox"/></c:when>
						<c:otherwise><input type="checkbox" class="tootajaVastutajaCheckbox"/></c:otherwise>
					</c:choose>
				</td>
				<td class="aktiivneVeerg">
					<c:choose>
						<c:when test="${kasutaja.aktiivne=='true'}"><input type="checkbox" checked /></c:when>
						<c:otherwise><input type="checkbox"/></c:otherwise>
					</c:choose>
				</td>
				<td class="nimeVeerg">
					${kasutaja.kasutajaNimi}
				</td>
				<td class="osalusVeerg">
					<input maxlength="5" class="osalusVeerg" value="${kasutaja.osalus}" />
				</td>
				<td style="display:none;max-width:10px;">
					<input value="${kasutaja.kasutajaID}" />
				</td>
		</tr>
	</c:forEach>
</table>
</div>
<br>
<b>Kirjeldus:</b>


<form:form modelAttribute="uusKirjeldus">

<table>
	<tr>
		<td><textarea class="kirjeldus" name="kirjeldus" id="kirjelduseVali" >${projekt.kirjeldus}</textarea></td>
	</tr>
	<form:input path="projektID" value="${projekt.id}" type="hidden" />
</table>


</form:form>

<button class="projektDetailNupp" id="logiAvamine">Ava logi</button>
<table class="logiTabel" id="logiTabel" style="display:none;">
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
<a href="vaadeArhiiv.htm">
<div class=closeView >X</div>
</a>
</body>
