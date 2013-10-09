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
<th><a href="vaadeProjektEsimene.htm?id=${projekt.id}">Üldine</a></th>
<th class=activeTab>Tulud ja kulud</th>
</tr>
</table>

</div>
</div>
<div class=leftSideDiv>

<form>
Tulu: <input type="text" class=smallInput> Kuupäev: <input type="text" class=smallInput> <br> Kirjeldus: <input type="text" class=bigInput> <button type="button">Lisa</button> <br>
<br>
Kulu: <input type="text" class=smallInput> Kuupäev: <input type="text" class=smallInput> <br> Kirjeldus: <input type="text" class=bigInput> <button type="button">Lisa</button> <br>
</form>

Tulud ja kulud:

<div class=describe>
<c:forEach items="${projekt.tulud}" var="tulu">
	+ ${tulu.summa}
	${tulu.nimi}
	${tulu.formaaditudAeg}
	<button type="button">Kustuta</button>
	<br>
</c:forEach>

<c:forEach items="${projekt.kulud}" var="kulu">
	- ${kulu.summa}
	${kulu.nimi}
	${kulu.formaaditudAeg}
	<button type="button">Kustuta</button>
	<br>
</c:forEach>


</div>

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

