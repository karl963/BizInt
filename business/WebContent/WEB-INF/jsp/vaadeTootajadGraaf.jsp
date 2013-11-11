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

<button type="button" style= "display:none;" id = "pipelineAndmed" onclick="javascript:tekitaPipelineGraaf('${andmeString}','tootajadGraaf');drawChart()"></button>

<br>
<br>
<br>

<div class="pipelineDivSuurus">
    <div id="chart_div" class="diagrammiKorgus"></div>
</div>
