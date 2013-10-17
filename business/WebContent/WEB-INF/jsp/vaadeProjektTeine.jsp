<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<p class="teade">${teade}</p>
<body class=darkBack>
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
		
		<div class=muudaKustuta>
			<button class="projektDetailNupp" type="button" id="projektiNimeMuutmiseNupp">Muuda nime</button>
			<button class="punaneProjektDetailNupp" type="button" onclick="javascript:document.kustutaProjekt.submit()" >Kustuta projekt</button>
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
<th><a href="vaadeProjektEsimene.htm?id=${projekt.id}">�ldine</a></th>
<th class=activeTab>Tulud ja kulud</th>
</tr>
</table>

</div>
</div>
<div class=leftSideDiv>


<form:form modelAttribute="uusTulu">
	Tulu:      <form:input class="smallInput" path="summa" value="0.0" />
	Kuup�ev:   <form:input maxlength="10" class="smallInput" path="stringAeg" />
<br>
	Kirjeldus: <form:input maxlength="100" class="bigInput" path="tuluNimi" value="" />
			   <form:input path="projektID" type="hidden" value="${projekt.id}" />
	           <form:input path="aeg.time" type="hidden"/>
	           <input class="projektDetailNupp" type="submit" value="lisa">
</form:form>

<br>

<form:form modelAttribute="uusKulu">
	Kulu:      <form:input class="smallInput" path="summa" value="0.0" />
	Kuup�ev:   <form:input maxlength="10" class="smallInput" path="stringAeg" />
<br>
	Kirjeldus: <form:input maxlength="100" class="bigInput" path="kuluNimi" value="" />
			   <form:input path="projektID" type="hidden" value="${projekt.id}" />
	           <form:input path="aeg.time" type="hidden" />
	           <input class="projektDetailNupp" type="submit" value="lisa">
</form:form>

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
	<td><small>${tulu.tuluNimi}</small></td>
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

