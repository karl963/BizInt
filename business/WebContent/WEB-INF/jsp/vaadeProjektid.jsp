<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<body>
<c:forEach items="${staatused}" var="staatus">
	${staatus.nimetus}
	<br>
</c:forEach>
</body>
</html>