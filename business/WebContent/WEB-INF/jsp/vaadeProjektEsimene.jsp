<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<p>${message}</p>
<body class=darkBack>
<div class=detailViewDiv>
<div class=detailViewHeader>
<h1>${projekt.nimi}</h1>
<div class=muudaKustuta><button type="button">Muuda nime</button><button type="button">Kustuta projekt</button> </div>
<div class=tuluKulu>
Tulu:${projekt.kogutulu} Kulu:${projekt.kogukulu}
</div>

<div class=reiting>
reiting: ${projekt.reiting}
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

<select>
<c:forEach items="${kasutajad}" var="kasutaja">
	<option value="${kasutaja.kasutajaNimi}">${kasutaja.kasutajaNimi}</option>
</c:forEach>
</select>
<input type="submit" value="lisa" />


<div class=describe>

<table class=table>
	<tr>
		<th class=smallCell>Vastuaja</th>
		<th class=smallCell>Aktiivne</th>
		<th class=nameCell>Nimi</th>
		<th class=smallCell>Osalus</th>
		<th></th>
	</tr>

	<c:forEach items="${projekt.kasutajad}" var="kasutaja">
		<tr>
			<td>${kasutaja.vastutaja}</td>
			<td>${kasutaja.aktiivne}</td>
			<td>${kasutaja.kasutajaNimi}</td>
			<td>${kasutaja.osalus}</td>
			<td><button type="button">Kustuta</button>
		</tr>
	</c:forEach>
</table>
</div>
<div class=buttonAlign>
<button type="button">Salvesta</button>
</div>
<br>
Kirjeldus:

<form:form modelAttribute="uusKirjeldus">
<div class=describe>
<table>
	<tr>
		<td><form:input path="kirjeldus" value="${projekt.kirjeldus}" /></td>
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
		<td>${logi.formaaditudAeg} - ${logi.sonum}</td>
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