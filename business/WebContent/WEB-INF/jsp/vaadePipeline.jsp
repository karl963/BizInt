<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<div class=menu_position>
	<img class=pipeline_selected_pos src="${pageContext.request.contextPath}/images/pipelineselect.png">
</div>

<div class=menu_position>
<div class=pipeline_selected_pos>
<div class=menu_button_text> Pipeline</div>
</div>
</div>

<button type="button" style= "display:none;" id = "pipelineAndmed" onclick="javascript:tekitaPipelineGraaf('${staatused}','pipeline')"></button>

<br>
<br>
<br>

<div class="pipelineDivSuurus">
    <div id="chart_div" class="diagrammiKorgus"></div>
</div>