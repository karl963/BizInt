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

<div id="rahavooDiv">

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

<input type="button" value="Salvesta yldkulude muudatused" class="projektDetailNupp" id="yldkuludeSalvestamiseNupp">

<table id="yldKuludeTabel">
	<tr>
		<th class="tootajaTabelHeaderVäli">Üldkulu nimetus</th>
		<th class="tootajaTabelHeaderVäli">Summa</th>
		<th class="tootajaTabelHeaderVäli">Kuupäev</th>
		<th class="tootajaTabelHeaderVäli" colspan=2>Korduv (iga kuu)</th>
	</tr>
	
	<c:forEach items="${yldKulud}" var="kulu">
	<tr class="yldKuluTabeliRida">
	
		<td class="yldKuluNimetusCell">
			
			<div style="display:none" class="yldKuluIdDiv">${kulu.kuluID}</div>
			<div class="yldKuluNimetusTextDiv yldTextDiv">${kulu.kuluNimi}</div>
			
			<div style="display:none;" class="yldKuluNimetusInputDiv yldInputDiv">
				<input class="yldKuluNimetusInput" value="${kulu.kuluNimi}">
			</div>
			
		</td>
		<td class="yldKuluSummaCell">
		
			<div class="yldKuluSummaTextDiv yldTextDiv">${kulu.summa}</div>
			
			<div style="display:none;" class="yldKuluSummaInputDiv yldInputDiv">
				<input class="yldKuluSummaInput" value="${kulu.summa}">
			</div>
			
		</td>
		<td class="yldKuluKuupäevCell">
		
			<div class="yldKuluKuupäevTextDiv yldTextDiv">${kulu.formaaditudAeg}</div>
			
			<div style="display:none;" class="yldKuluKuupäevInputDiv yldInputDiv">
				<input class="yldKuluKuupäevInput" value="${kulu.formaaditudAeg}">
			</div>
			
		</td>
		<td class="yldKuluKorduvCell">
			<c:choose>
				<c:when test="${kulu.korduv==true}">
					<input class="korduvYldKuluInput" type="checkbox" checked>
				</c:when>
				<c:otherwise>
					<input class="korduvYldKuluInput" type="checkbox">
				</c:otherwise>
			</c:choose>
		</td>
		<td class="yldKuluKorduvCell">
			<input type="button" value="X" class="punaneProjektDetailNupp yldKuluKustutamiseNupp">
		</td>
		
	</tr>
	</c:forEach>
	
	<tr>
		<td colspan=5 >
			<input id="uusYldKuluNimi" class="looUusInputLahter yldKuluNimetusInput" value="kulu nimi">
			<input id="uusYldKuluSumma" class="yldKuluSummaInput" value="0.0">
			<input id="uusYldKuluKuupäev" class="yldKuluKuupäevInput" value="${hetkeKuupaev}">
			<input id="kasKorduvYldkulu" type="checkbox" >Korduv (iga kuu)
			<input id="lisaYldKuluNupp" type="button" value="Lisa üldkulu" class="projektDetailNupp">
		</td>
	</tr>
</table>

</div>