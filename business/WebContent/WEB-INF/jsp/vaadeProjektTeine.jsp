<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

${projekt.nimi}

${projekt.kogutulu}
${projekt.kogukulu}

<c:forEach items="${projekt.tulud}" var="tulu">
	${tulu.summa}
	${tulu.nimi}
	${tulu.formaaditudAeg}
</c:forEach>

<c:forEach items="${projekt.kulud}" var="kulu">
	- ${kulu.summa}
	${kulu.nimi}
	${kulu.formaaditudAeg}
</c:forEach>

<!-- sulud ajal on lihtsalt selleks et nad ongi sulgudes, mina vähemalt paneks nii -->
<c:forEach items="${projekt.kommentaarid}" var="kommentaar">
	${kommentaar.kasutaja} ${kommentaar.sonum} (${kommentaar.formaaditudAeg})
</c:forEach>
