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
			<button class="projektDetailNupp" type="button" id="projektiArhiveerimiseNupp"> Arhiveeri</button>
			<button class="punaneProjektDetailNupp kustutaProjektNupp" type="button" >Kustuta projekt</button>
		</div>
		<div class="muudaKustuta muudaKustutaTeine" style="display:none;">
			<b>Kustuta Projekt ? </b>
			<button class="punaneProjektDetailNupp" type="button" onclick="javascript:document.kustutaProjekt.submit()" >Jah</button>
			<button class="projektDetailNupp katkestaProjektiKustutamine" type="button" >Katkesta</button>
		</div>
		
			<div class="muudaKustuta muudaArhiveeri" style="display:none;">
			<table>
			<tr>
			<th style="vertical-align:top;"><b>Arhiveeri projekt ? </b><th>
			<th><form:form modelAttribute="paneProjektArhiivi">
				<form:input path="projektID" type="hidden" />
				<input class="punaneProjektDetailNupp" type="submit" value="Jah" />
			</form:form></th>
			<th style="vertical-align:top;"><button class="projektDetailNupp katkestaProjektiKustutamine" type="button" >Katkesta</button><th>
			<tr>
			</table>
			</div>
		
<form name="kustutaProjekt" method="POST" action="vaadeProjektTeine.htm">
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
<th><a href="vaadeProjektEsimene.htm?id=${projekt.id}">Üldine</a></th>
<th class=activeTab>Tulud ja kulud</th>
</tr>
</table>

</div>
</div>
<div class=leftSideDiv>


<form:form modelAttribute="uusTulu">
	Tulu:      <form:input class="smallInput" path="summa" value="0.0" />
	Kuupäev:   <form:input maxlength="10" class="smallInput" path="stringAeg" />
<br>
	Kirjeldus: <form:input maxlength="100" class="bigInput" path="tuluNimi" value="" />
			   <form:input path="projektID" type="hidden" value="${projekt.id}" />
			   <form:input id="tuluArvestaKaibemaksuInput" path="käibemaksuArvestatakse" type="hidden"/>
	           <form:input path="aeg.time" type="hidden"/>
	           <input class="projektDetailNupp" type="submit" value="lisa">
</form:form>
<div id="tuluArvestaKaibemaksuDiv"><input type=checkbox id="tuluArvestaKaibemaksuBox" checked/> Arvesta Käibemaksu</div>

<br>

<form:form modelAttribute="uusKulu">
	Kulu:      <form:input class="smallInput" path="summa" value="0.0" />
	Kuupäev:   <form:input maxlength="10" class="smallInput" path="stringAeg" />
<br>
	<div id="kuluKirjeldus">Kirjeldus: <form:input maxlength="100" class="bigInput" path="kuluNimi" value="" /></div>
	<div style="display:none;" id="tootajadList">
	<select id="tootajaKuluValimine">
		<option selected disabled value="Valige töötaja">Valige töötaja</option>
		<c:forEach items="${tootajad}" var="tootaja">
			<option value="${tootaja}">${tootaja}</option>
		</c:forEach>
	</select>
	</div>		
		<form:input id="palkKasutajaNimi" path="kasutajaNimi" type="hidden"/>
		<form:input id="kuluArvestaKaibemaksuInput" path="käibemaksuArvestatakse" type="hidden"/>
		<form:input path="projektID" type="hidden" value="${projekt.id}" />
	    <form:input path="aeg.time" type="hidden" />
	   	<input class="kuluLisamiseNupp projektDetailNupp" type="submit" value="lisa">
</form:form>
<div id="kuluArvestaKaibemaksuDiv"><input type=checkbox id="kuluArvestaKaibemaksuBox" checked/> Arvesta Käibemaksu</div>
<div class="kuluPalkCheck" ><input type="checkbox" name="kuluPalk" id="kuluPalk">Töötaja Palk</div>

<br>

<b>Tulud ja kulud:</b>

<div class=describe>
<table>
<c:forEach items="${projekt.tulud}" var="tulu">
<tr>
	<form:form modelAttribute="kustutaTulu">
	<form:input path="tuluNimi" type="hidden" value="${tulu.tuluNimi}" />
	<form:input path="summa" type="hidden" value="${tulu.summa}" />
	<form:input path="stringAeg" type="hidden" value="${tulu.formaaditudAeg}" />
	<form:input path="projektID" type="hidden" value="${projekt.id}" />

	<td><b class="tuluSumma">+ ${tulu.summa}</b></td>
	<td><small>
		<c:choose>
			<c:when test="${tulu.kasArvestaKaibemaksu==false}">
				${tulu.tuluNimi}
			</c:when>
			<c:otherwise>
				${tulu.tuluNimi} (+käibemaks ${tulu.kaibemaks})
			</c:otherwise>
		</c:choose>
	</small></td>
	<td><small><i><b>${tulu.formaaditudAeg}</b></i></small></td>
	<td><input class="punaneProjektDetailNupp" type="submit" value="X" /></td>
	</form:form>
</tr>
</c:forEach>

<c:forEach items="${projekt.kulud}" var="kulu">
<tr>
	<form:form modelAttribute="kustutaKulu">
		<form:input path="kuluNimi" type="hidden" value="${kulu.kuluNimi}" />
		<form:input path="summa" type="hidden" value="${kulu.summa}" />
		<form:input path="stringAeg" type="hidden" value="${kulu.formaaditudAeg}" />
		<form:input path="projektID" type="hidden" value="${projekt.id}" />


		<td><b class="kuluSumma">- ${kulu.summa}</b></td>
		<td><small>	
			<c:choose>
			<c:when test="${kulu.kasArvestaKaibemaksu==false}">
				${kulu.kuluNimi}
			</c:when>
			<c:otherwise>
				${kulu.kuluNimi} (+käibemaks ${kulu.kaibemaks})
			</c:otherwise>
			</c:choose>
		</small></td>
		<td><small><i><b>${kulu.formaaditudAeg}</b></i></small></td>
		<td><input class="punaneProjektDetailNupp" type="submit" value="X" /></td>
		
	</form:form>
</tr>
</c:forEach>
</table>

</div>

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
<a class=closeView href="vaadeProjektid.htm">
X
</a>
</body>

