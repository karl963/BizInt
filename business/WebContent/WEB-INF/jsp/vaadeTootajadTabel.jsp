<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<br><br><br><br><br>

${teade}

<div>
	<table>
		<tr>
			<th class=activeTab ><a href="vaadeTootajadTabel.htm">Tabel</a></th>
			<th ><a href="vaadeTootajadGraaf.htm">Graaf</a></th>
		</tr>
	</table>
</div>

<br>

<table>
	<tr>
		<th>Töötajad</th>
	</tr>
	<c:forEach items="${kasutajad}" var="tootaja" >
	
		<form:form modelAttribute="kustutaTootaja">
			<form:input type="hidden" path="kasutajaID" value="${tootaja.kasutajaID}" />
		<tr>
			<td>${tootaja.kasutajaNimi}</td>
			<td><input type="submit" value="X" /></td>
		</tr>
		
		</form:form>
	</c:forEach>
	<tr>
		<td>
		<form:form modelAttribute="uusTootaja">
			<form:input class="looUusInputLahter" path="kasutajaNimi" value="uus töötaja" />
			<input type="submit" value="lisa töötaja">
		</form:form>
		</td>
	</tr>
</table>

