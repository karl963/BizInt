<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>

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

<select id="rahaVoogKvartaliteValikud">
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
</div>

<table id="yldKuludeTabel">
	<tr>
		<th>Üldkulu nimetus</th>
		<th>Summa</th>
		<th colspan=2>Kuupäev</th>
	</tr>
	<tr>
		<c:forEach items="${yldKulud}" var="kulu">
		
		<form:form>
		<td class="yldKuluNimetusCell">
		
			<div class="yldKuluNimetusTextDiv">${kulu.kuluNimi}</div>
			
			<div style="display:none;" class="yldKuluNimetusInputDiv">
				<input class="yldKuluNimetusInput" value="${kulu.kuluNimi}">
			</div>
			
		</td>
		<td class="yldKuluSummaCell">
		
			<div class="yldKuluSummaTextDiv">${kulu.summa}</div>
			
			<div style="display:none;" class="yldKuluSummaInputDiv">
				<input class="yldKuluSummaInput" value="${kulu.summa}">
			</div>
			
		</td>
		<td class="yldKuluKuupäevCell">
		
			<div class="yldKuluKuupäevTextDiv">${kulu.formaaditudAeg}</div>
			
			<div style="display:none;" class="yldKuluKuupäevInputDiv">
				<input class="yldKuluKuupäevInput" value="${kulu.formaaditudAeg}">
			</div>
			
		</td>
		<td>
			<input type="submit" value="X" class="punaneProjektDetailNupp">
		</td>
		</form:form>
		
		</c:forEach>
	</tr>
	<tr>
		<td colspan=3 >
			<input id="uusYldKuluNimi" class="looUusInputLahter" value="kulu nimi">
			<input id="uusYldKuluSumma" value="0.0">
			<input id="uusYldKuluKuupäev" value="${hetkeKuupäev}">
			<input id="lisaYldKuluNupp" type="button" value="Lisa üldkulu" class="projektDetailNupp">
		</td>
	</tr>
</table>