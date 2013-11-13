<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class=menu_position>
	<img class=rahavoog_selected_pos src="${pageContext.request.contextPath}/images/rahavoogselect.png">
</div>

<div class=menu_position>
<div class=rahavoog_selected_pos>
<div class=menu_button_text> Rahavoog</div>
</div>
</div>

<button type="button" style= "display:none;" id = "pipelineAndmed" onclick="javascript:tekitaPipelineGraaf('${andmedString}','tootajadGraaf')"></button>

<br>
<br>
<br>

<select id="rahaVoogAastaValik">
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

<div class="pipelineDivSuurus">
    <div id="chart_div" class="diagrammiKorgus"></div>
</div>