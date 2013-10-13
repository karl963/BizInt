<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${staatused}" var="staatus">
	<p>NIMI: ${staatus.nimi} <br> TULU: ${staatus.kogutulu} <br> KULU: ${staatus.kogukulu} <br> BILANSS/KASUM: ${staatus.bilanss}</p>
</c:forEach>

