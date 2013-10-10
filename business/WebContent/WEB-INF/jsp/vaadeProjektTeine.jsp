<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<p>${message}</p>
<body class=darkBack>
<div class=detailViewDiv>
<div class=detailViewHeader>

		<div style="display:none" id="projektiNimeMuutmine" >
			<form:form modelAttribute="uusProjektiNimi">
				<form:input path="uusNimi"/>
				<form:input path="projektID" type="hidden" />
				<input type="submit" value="muuda nime" />
			</form:form>
		</div>
		
		<h1 id="projektiNimi" >${projekt.nimi}</h1>
		
		<div class=muudaKustuta>
			<button type="button" id="projektiNimeMuutmiseNupp">Muuda nime</button>
			<button type="button">Kustuta projekt</button>
		</div>
		
<div class=tuluKulu>
Tulu:${projekt.kogutulu} Kulu:${projekt.kogukulu}
</div>

<div class=reiting>
reiting: ${projekt.reiting}
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
	Kuupäev:   <form:input class="smallInput" path="stringAeg" />
<br>
	Kirjeldus: <form:input class="bigInput" path="tuluNimi" value="" />
			   <form:input path="projektID" type="hidden" value="${projekt.id}" />
	           <form:input path="aeg.time" type="hidden"/>
	           <input type="submit" value="lisa">
</form:form>

<br>

<form:form modelAttribute="uusKulu">
	Kulu:      <form:input class="smallInput" path="summa" value="0.0" />
	Kuupäev:   <form:input class="smallInput" path="stringAeg" />
<br>
	Kirjeldus: <form:input class="bigInput" path="kuluNimi" value="" />
			   <form:input path="projektID" type="hidden" value="${projekt.id}" />
	           <form:input path="aeg.time" type="hidden" />
	           <input type="submit" value="lisa">
</form:form>

Tulud ja kulud:

<div class=describe>
<c:forEach items="${projekt.tulud}" var="tulu">
	<form:form modelAttribute="kustutaTulu">
	<form:input path="tuluNimi" type="hidden" value="${tulu.tuluNimi}" />
	<form:input path="summa" type="hidden" value="${tulu.summa}" />
	<form:input path="stringAeg" type="hidden" value="${tulu.formaaditudAeg}" />
	<form:input path="projektID" type="hidden" value="${projekt.id}" />

	<b class="tuluSumma">+ ${tulu.summa}</b>
	<small>
		${tulu.tuluNimi}
		<i><b>${tulu.formaaditudAeg}</b></i>
	</small>
	<input type="submit" value="kustuta" />
	<br>
	
	</form:form>
</c:forEach>

<c:forEach items="${projekt.kulud}" var="kulu">
	<form:form modelAttribute="kustutaKulu">
		<form:input path="kuluNimi" type="hidden" value="${kulu.kuluNimi}" />
		<form:input path="summa" type="hidden" value="${kulu.summa}" />
		<form:input path="stringAeg" type="hidden" value="${tulu.formaaditudAeg}" />
		<form:input path="projektID" type="hidden" value="${projekt.id}" />


		<b class="kuluSumma">- ${kulu.summa}</b>
		<small>
			${kulu.kuluNimi}
			<i><b>${kulu.formaaditudAeg}</b></i>
		</small>
		<input type="submit" value="kustuta" />
		<br>
		
	</form:form>
</c:forEach>


</div>

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

