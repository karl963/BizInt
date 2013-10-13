<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<br><br><br><br>

<div>
	<table>
		<tr>
			<th><a href="vaadeTootajadTabel.htm">Tabel</a></th>
			<th class=activeTab ><a href="vaadeTootajadGraaf.htm">Graaf</a></th>
		</tr>
	</table>
</div>

<br>

<table>
	<tr>
		<th>Töötajad</th>
	</tr>
	<c:forEach items="${kasutajad}" var="tootaja" >
		<tr>
			<td>${tootaja.kasutajaNimi}</td>
		</tr>
	</c:forEach>

</table>