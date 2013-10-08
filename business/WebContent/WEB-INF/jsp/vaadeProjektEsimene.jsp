<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<p>${message}</p>

<h1>${projekt.nimi}</h1>

<p>reiting: ${projekt.reiting}</p>

<table style="border:1px solid black">
	<tr>
		<th colspan=4 style="text-align:middle">Kasutajad</th>
	</tr>
	<tr>
		<td style="border:1px solid black">vastuaja</td>
		<td style="border:1px solid black">aktiivne</td>
		<td style="border:1px solid black">nimi</td>
		<td style="border:1px solid black">osalus</td>
	</tr>
	<c:forEach items="${projekt.kasutajad}" var="kasutaja">
		<tr>
			<td style="border:1px solid black">${kasutaja.vastutaja}</td>
			<td style="border:1px solid black">${kasutaja.aktiivne}</td>
			<td style="border:1px solid black">${kasutaja.nimi}</td>
			<td style="border:1px solid black">${kasutaja.osalus}</td>
		</tr>
	</c:forEach>
</table>

<table style="border:1px solid black">
	<tr>
		<th>Projekti kirjeldus</th>
	</tr>
	<tr>
		<td>${projekt.kirjeldus}</td>
	</tr>
</table>

<table style="border:1px solid black">
	<tr>
		<th>Logi</th>
	</tr>
	<c:forEach items="${projekt.logi}" var="logi">
	<tr>
		<td>${logi.formaaditudAeg} - ${logi.sonum}</td>
	</tr>
	</c:forEach>
</table>

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

<br>
<a href="vaadeProjektid.htm">tagasi</a>
<p></p>
<a href="vaadeProjektTeine.htm?id=${projekt.id}">Tulud ja kulud vaade</a>
