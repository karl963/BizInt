<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<c:forEach items="${staatused}" var="staatus">
	${staatus.nimi}
	<br>
</c:forEach>
</body>
</html>