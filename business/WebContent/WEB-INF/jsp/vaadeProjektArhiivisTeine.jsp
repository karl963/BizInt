<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<body class=darkBack>

<c:choose>
      <c:when test="${teade != null}">
      	<div class="teadeÜlevalProjektDetail teadeHajuvKadumine">${teade}</div>
      </c:when>
</c:choose>

<div class=detailViewDiv>
<div class=detailViewHeader>

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
		
<form name="kustutaProjekt" method="POST" action="vaadeProjektArhiivisTeine.htm">
	<input type="hidden" name="projektID" value="${projekt.id}">
	<input type="hidden" name="kustuta" value="jah">
</form>
		
<div class=tuluKulu>
Tulu: <b class="tuluSumma">${projekt.kogutulu}</b> Kulu: <b class="kuluSumma">${projekt.kogukulu}</b>
</div>

<div class=reiting>
Reiting: ${projekt.reitinguHTML}
</div>


<div class=viewTabs>
<table>
<tr>
<th><a href="vaadeProjektArhiivisEsimene.htm?id=${projekt.id}">Üldine</a></th>
<th class=activeTab>Tulud ja kulud</th>
</tr>
</table>

</div>
</div>
<div class=leftSideDiv>

<br>

<b>Tulud ja kulud:</b>

<div class=describe>
<table>
<c:forEach items="${projekt.tulud}" var="tulu">
</c:forEach>

<c:forEach items="${projekt.kulud}" var="kulu">
<tr>
	<form:form modelAttribute="kustutaKulu">
		<form:input path="kuluNimi" type="hidden" value="${kulu.kuluNimi}" />
		<form:input path="summa" type="hidden" value="${kulu.summa}" />
		<form:input path="stringAeg" type="hidden" value="${tulu.formaaditudAeg}" />
		<form:input path="projektID" type="hidden" value="${projekt.id}" />


		<td><b class="kuluSumma">- ${kulu.summa}</b></td>
		<td><small>${kulu.kuluNimi}</small></td>
		<td><small><i><b>${kulu.formaaditudAeg}</b></i></small></td>
		<td><input class="punaneProjektDetailNupp" type="submit" value="X" /></td>
		
	</form:form>
</tr>
</c:forEach>
</table>

</div>

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

