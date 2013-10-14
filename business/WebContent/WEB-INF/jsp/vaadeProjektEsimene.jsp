<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<p>${teade}</p>
<body class=darkBack>
<div class=detailViewDiv>
<div class=detailViewHeader>

		<div style="display:none" id="projektiNimeMuutmine" >
			<form:form modelAttribute="uusProjektiNimi">
				<form:input path="uusNimi" />
				<form:input path="projektID" type="hidden" />
				<input type="submit" value="muuda nime" />
			</form:form>
		</div>
		
		<h1 id="projektiNimi" >${projekt.nimi}</h1>
		
		<div class=muudaKustuta>
		
			<button type="button" id="projektiNimeMuutmiseNupp" >Muuda nime</button>
			<button type="button" onclick="javascript:document.kustutaProjekt.submit()" >Kustuta projekt</button>
		</div>

<form name="kustutaProjekt" method="POST" action="vaadeProjektEsimene.htm">
	<input type="hidden" name="projektID" value="${projekt.id}">
	<input type="hidden" name="kustuta" value="jah">
</form>

<div class=tuluKulu>
Tulu: <b class="tuluSumma">${projekt.kogutulu}</b> Kulu: <b class="kuluSumma">${projekt.kogukulu}</b>
</div>

<div class=reiting>

<!--
<c:choose>
	<c:when test="${projekt.reiting==1}">  </c:when>
	<c:when test="${projekt.reiting==2}">  </c:when>
	<c:when test="${projekt.reiting==3}">  </c:when>
	<c:when test="${projekt.reiting==4}">  </c:when>
	<c:otherwise>  </c:otherwise>
</c:choose>
-->

reiting: ${projekt.reitinguHTML}

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

Projektiga seotud inimesed:
<br>

<select id="uueKasutajaList">
<c:forEach items="${kasutajad}" var="kasutaja">
	<option value="${kasutaja.kasutajaNimi}">${kasutaja.kasutajaNimi}</option>
</c:forEach>
</select>
<input type="button" onclick="lisaProjektiKasutaja('${pageContext.request.contextPath}',${projekt.id});" value="lisa" />


<div class=describe>

<table class=table id="kasutajateTabel" >
	<tr>
		<th class=smallCell>Vastuaja</th>
		<th class=smallCell>Aktiivne</th>
		<th class=nameCell>Nimi</th>
		<th class=smallCell>Osalus</th>
		<th></th>
	</tr>

	<c:forEach items="${projekt.kasutajad}" var="kasutaja">
		<tr>

				<td>
					<c:choose>
						<c:when test="${kasutaja.vastutaja=='true'}"><input type="checkbox" checked /></c:when>
						<c:otherwise><input type="checkbox"/></c:otherwise>
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
					<input value="${kasutaja.osalus}" />
				</td>
				<td style="display:none;"><input value="${kasutaja.kasutajaID}" /></td>
				
			<form:form modelAttribute="eemaldaKasutaja">
				<form:input type="hidden" path="kasutajaID" value="${kasutaja.kasutajaID}" />
				<form:input type="hidden" path="projektID" value="${projekt.id}" />
				<td><button type="submit">Kustuta</button></td>
			</form:form>
		</tr>
	</c:forEach>
</table>
</div>
<div class=buttonAlign>
<button type="button" onclick="listiProjektiKasutajadJaSalvesta(${projekt.id});" >Salvesta töötajad</button>
</div>
<br>
Kirjeldus:

<form:form modelAttribute="uusKirjeldus">
<div class=describe>
<table>
	<tr>
		<td><textarea name="kirjeldus" id="kirjelduseVali" >${projekt.kirjeldus}</textarea></td>
	</tr>
	<form:input path="projektID" value="${projekt.id}" type="hidden" />
</table>
</div>
<div class=buttonAlign>
	<input class=addbutton type="submit" value="muuda"/>
</div>
</form:form>

<table>
	<tr>
		<th>Logi</th>
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
	<tr>
		<td>
			<form:input path="sonum" />
			<form:input path="projektID" value="${projekt.id}" type="hidden"/>
			<input type="submit" value="lisa" />
		</td>
	</tr>
</form:form>

<table style="border:1px solid black">
	<tr>
		<th>Kommentaarid</th>
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