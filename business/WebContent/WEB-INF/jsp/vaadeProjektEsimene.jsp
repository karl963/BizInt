<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${projekt.nimi}

${projekt.reiting}

<c:forEach items="${projekt.kasutajad}" var="kasutaja">
	${kasutaja.vastutaja}
	${kasutaja.aktiivne}
	${kasutaja.nimi}
	${kasutaja.osalus}
</c:forEach>

${projekt.kirjeldus}

<c:forEach items="${projekt.logi}" var="logi">
	${logi.formaaditudAeg} ${logi.sonum}
</c:forEach>

<!-- sulud ajal on lihtsalt selleks et nad ongi sulgudes, mina vähemalt paneks nii -->
<c:forEach items="${projekt.kommentaarid}" var="kommentaar">
	${kommentaar.kasutaja} ${kommentaar.sonum} (${kommentaar.formaaditudAeg})
</c:forEach>
