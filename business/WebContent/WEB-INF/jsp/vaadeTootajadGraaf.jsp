<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

<div class=menu_position>
	<img class=tootajad_selected_pos src="${pageContext.request.contextPath}/images/tootajadselect.png">
</div>

<div class=menu_position>
<div class=tootajad_selected_pos>
<div class=menu_button_text> Töötajad</div>
</div>
</div>

<button type="button" style= "display:none;" id = "pipelineAndmed"  onclick="javascript:tekitaPipelineGraaf('${andmedString}','tootajadGraaf')"></button>

<div id="tootajadGraafDiv">

	<br>
	
	<div>
		<table>
			<tr>
				<th ><a href="vaadeTootajadTabel.htm">Tabel</a></th>
				<th class=activeTab ><a href="vaadeTootajadGraaf.htm">Graaf</a></th>
			</tr>
		</table>
	</div>
	

	<select id="tootajadGraafAastaValik">
		<c:forEach items="${aastad}" var="aasta">
			<c:choose>
				<c:when test="${aasta == hetkeAasta}">
	    			<option value="${aasta}" selected>${aasta}</option>
	    		</c:when>
	    		<c:otherwise>
	    			<option value="${aasta}">${aasta}</option>
	    		</c:otherwise>
	    	</c:choose>
	    </c:forEach>
	</select>
	
	<select id="tootajadGraafKvartaliteValikud">
		<c:forEach items="${kvartalid}" var="kvartal">
			<c:choose>
				<c:when test="${kvartal == hetkeKvartal}">
	    			<option value="${kvartal}" selected>${kvartal}</option>
	    		</c:when>
	    		<c:otherwise>
	    			<option value="${kvartal}">${kvartal}</option>
	    		</c:otherwise>
	    	</c:choose>
	    </c:forEach>
	</select>
	
	<div class="pipelineDivSuurus">
	    <div id="chart_div" class="diagrammiKorgus"></div>
	    <div class="googleTekst">Kasutatud Google Visualization-it</div>
	</div>

</div>
