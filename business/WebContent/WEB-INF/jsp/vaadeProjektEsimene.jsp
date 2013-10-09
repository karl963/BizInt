<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<option value="Kasutaja I">Kasutaja I</option>
</select>
<button type="button">Lisa</button>
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
			<td>${kasutaja.nimi}</td>
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
<div class=describe>
<table>
	<tr>
		<td>${projekt.kirjeldus}</td>
	</tr>
</table>
</div>
<div class=buttonAlign>
<button type="button">Muuda</button>
</div>

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

<!-- sulud ajal on lihtsalt selleks et nad ongi sulgudes, mina vähemalt paneks nii -->
<table style="border:1px solid black">
	<tr>
		<th>Kommentaarid</th>
	</tr>
	<c:forEach items="${projekt.kommentaarid}" var="kommentaar">
	<tr>
		<td>${kommentaar.kasutaja.nimi} : ${kommentaar.sonum} ("${kommentaar.formaaditudAeg}")</td>
	</tr>
	</c:forEach>
</table>

</div>
</div>
<a href="vaadeProjektid.htm">
<div class=closeView >X</div>
</a>
</body>