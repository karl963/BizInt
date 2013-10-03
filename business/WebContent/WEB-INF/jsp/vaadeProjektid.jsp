<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class=textontop>
<c:forEach items="${staatused}" var="staatus">

	<div class=statustext>
		${staatus.nimi}<br>
		${staatus.summa}
	</div>

	<div class=scrollwindow>
		<c:forEach items="${staatus.projektid}" var="projekt">
			<a href="vaadeProjektid.htm">
			<table class=project>
				<tr>
					<th>Nimi: </th>
					<th>${projekt.nimi}</th>
				</tr>
				<tr>
					<th>Vastutaja:</th>
					<th>${projekt.vastutaja.nimi}</th>
				</tr>
				<tr>
					<th>Tulu:</th>
					<th>${projekt.kogutulu}</th>
				</tr>
			</table>
			</a>
			<p></p>
		</c:forEach>
		
		<table class=project>
			<tr>
				<th>Uus projekt</th>
				<th><a href="vaadeProjektid.htm"><img src="${pageContext.request.contextPath}/images/addbutton.png"></a></th>
			</tr>
		</table>
	</div>

</c:forEach>

	<div class=statustext>
		<div class=statusdivider>
			Uus staatus
			<div>
				<a href="vaadeProjektid.htm">
					<img class=addbutton src="${pageContext.request.contextPath}/images/addbutton.png">
				</a>
			</div>
		</div>
	</div>
</div>
