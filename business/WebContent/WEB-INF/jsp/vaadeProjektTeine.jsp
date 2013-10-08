<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<h1>${projekt.nimi}</h1>
<br>
${projekt.kogutulu}
<br>
${projekt.kogukulu}
<br>

<c:forEach items="${projekt.tulud}" var="tulu">
	${tulu.summa}
	${tulu.nimi}
	${tulu.formaaditudAeg}
</c:forEach>

<br>

<c:forEach items="${projekt.kulud}" var="kulu">
	- ${kulu.summa}
	${kulu.nimi}
	${kulu.formaaditudAeg}
</c:forEach>

<br>

<!-- sulud ajal on lihtsalt selleks et nad ongi sulgudes, mina v�hemalt paneks nii -->
<c:forEach items="${projekt.kommentaarid}" var="kommentaar">
	${kommentaar.kasutaja.nimi} ${kommentaar.sonum} (${kommentaar.formaaditudAeg})
</c:forEach>

<br>
<a href="vaadeProjektid.htm">tagasi</a>
<p></p>
<a href="vaadeProjektEsimene.htm?id=${projekt.id}">�ldine vaade</a>