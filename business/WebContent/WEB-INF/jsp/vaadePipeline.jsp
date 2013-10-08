<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:forEach items="${staatused}" var="staatus">
${staatus.nimi}
${staatus.kogutulu}
${staatus.kogukulu}
</c:forEach>

