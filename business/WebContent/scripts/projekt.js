$(document).ready(function(){

	$("#projektiNimeMuutmiseNupp").click(function(){
		$("#projektiNimi").hide();
		$("#projektiNimeMuutmine").show();
		$("#projektiNimeMuutmiseNupp").hide();
	});

});

$(document).ready(function(){
	
	$(".teadeHajuvKadumine").ready(function() {
		  $(".teadeHajuvKadumine").animate({
		    opacity: 0.0,
		  }, 6000 );
	});
	
    $(".yldKuluNimetusTextDiv").click(function(){
		$(".yldInputDiv").hide();
		$(".yldTextDiv").show();
        $(this).closest(".yldKuluNimetusCell").children(".yldKuluNimetusInputDiv").show();
        $(this).closest(".yldKuluNimetusCell").children(".yldKuluNimetusInputDiv").find(".yldKuluNimetusInput").focus().val($(this).closest(".yldKuluNimetusCell").children(".yldKuluNimetusInputDiv").find(".yldKuluNimetusInput").val());
        $(this).hide();
	});
	$(".yldKuluSummaTextDiv").click(function(){
		$(".yldInputDiv").hide();
		$(".yldTextDiv").show();
        $(this).closest(".yldKuluSummaCell").children(".yldKuluSummaInputDiv").show();
        $(this).closest(".yldKuluSummaCell").children(".yldKuluSummaInputDiv").find(".yldKuluSummaInput").focus().val($(this).closest(".yldKuluSummaCell").children(".yldKuluSummaInputDiv").find(".yldKuluSummaInput").val());
        $(this).hide();
	});
	$(".yldKuluKuupäevTextDiv").click(function(){
		$(".yldInputDiv").hide();
		$(".yldTextDiv").show();
        $(this).closest(".yldKuluKuupäevCell").children(".yldKuluKuupäevInputDiv").show();
        $(this).closest(".yldKuluKuupäevCell").children(".yldKuluKuupäevInputDiv").find(".yldKuluKuupäevInput").focus().val($(this).closest(".yldKuluKuupäevCell").children(".yldKuluKuupäevInputDiv").find(".yldKuluKuupäevInput").val());
        $(this).hide();
	});
	
	$(".yldKuluNimetusInput").blur(function(){
        $(this).closest(".yldKuluNimetusCell").children(".yldKuluNimetusTextDiv").html($(this).val());
        $(".yldKuluNimetusInputDiv").hide();
        $(".yldKuluNimetusTextDiv").show();
	});
	$(".yldKuluSummaInput").blur(function(){
        $(this).closest(".yldKuluSummaCell").children(".yldKuluSummaTextDiv").html($(this).val());
        $(".yldKuluSummaInputDiv").hide();
        $(".yldKuluSummaTextDiv").show();
	});
	$(".yldKuluKuupäevInput").blur(function(){
        $(this).closest(".yldKuluKuupäevCell").children(".yldKuluKuupäevTextDiv").html($(this).val());
        $(".yldKuluKuupäevInputDiv").hide();
        $(".yldKuluKuupäevTextDiv").show();
	});
	
	$("#lisaYldKuluNupp").click(function() {
		
	        var kuluSumma = $("#uusYldKuluSumma").val();
	        var kuluKuupaev = $("#uusYldKuluKuupäev").val();
	        var kuluNimetus = $("#uusYldKuluNimi").val();
	        var korduv = $("#kasKorduvYldkulu").is(':checked');

	        $.ajax({
	            type : "POST",
	            url : rakenduseNimi+"/vaadeRahavoog.htm",
	            data : {summa: kuluSumma, nimetus: kuluNimetus, kuupaev: kuluKuupaev, korduv : korduv},
	            success : function(response) {
	                    document.location.href = "vaadeRahavoog.htm";
	            },
	            error : function(e) {
	                    document.location.href = "vaadeRahavoog.htm";
	            }
	        });
	});
	
	$("#yldkuludeSalvestamiseNupp").click(function() {
		
		var kulud = "";
		
		var table = document.getElementById("yldKuludeTabel");
		for(var x = 1, row;row = table.rows[x], x < table.rows.length; x++){

			if(x != table.rows.length-1){

				var id = row.getElementsByClassName("yldKuluNimetusCell")[0].getElementsByClassName("yldKuluIdDiv")[0].innerHTML;
				var nimi = row.getElementsByClassName("yldKuluNimetusCell")[0].getElementsByClassName("yldKuluNimetusInputDiv")[0].getElementsByClassName("yldKuluNimetusInput")[0].value;
				var summa = row.getElementsByClassName("yldKuluSummaCell")[0].getElementsByClassName("yldKuluSummaInputDiv")[0].getElementsByClassName("yldKuluSummaInput")[0].value;
				var kuupäev = row.getElementsByClassName("yldKuluKuupäevCell")[0].getElementsByClassName("yldKuluKuupäevInputDiv")[0].getElementsByClassName("yldKuluKuupäevInput")[0].value;
				var korduv = row.getElementsByClassName("yldKuluKorduvCell")[0].getElementsByClassName("korduvYldKuluInput")[0].checked;
				
			    for(var i = 0; i < nimi.length; i++) {
					if(nimi.charAt(i) == ";"){
						nimi = nimi.replaceAt(i,"");
					}
					else if(nimi.charAt(i) == "#"){
						nimi = nimi.replaceAt(i,"");
					}
			    }
			    for(var i = 0; i < summa.length; i++) {
					if(summa.charAt(i) == ";"){
						summa = summa.replaceAt(i,"");
					}
					else if(summa.charAt(i) == "#"){
						summa = summa.replaceAt(i,"");
					}
			    }
			    for(var i = 0; i < kuupäev.length; i++) {
					if(kuupäev.charAt(i) == ";"){
						kuupäev = kuupäev.replaceAt(i,"");
					}
					else if(kuupäev.charAt(i) == "#"){
						kuupäev = kuupäev.replaceAt(i,"");
					}
			    }

				kulud += (id+";"+nimi+";"+summa+";"+kuupäev+";"+korduv+"#");
				
			}
		}
		
		salvestaYldKulud(kulud);
	});
	
	$(".yldKuluKustutamiseNupp").click(function() {
		
		var id = $(this).closest(".yldKuluTabeliRida").children(".yldKuluNimetusCell").children(".yldKuluIdDiv").html();
		
	    $.ajax({
	        type : "POST",
	        url : rakenduseNimi+"/vaadeRahavoog.htm",
	        data : {kuluID : id},
	        success : function(response) {
	        	document.location.href = "vaadeRahavoog.htm";
	        },
	        error : function(e) {
	        	document.location.href = "vaadeRahavoog.htm";
	        }
	    });
		
	});
	
	////////////////////////////////////////////

	$(".kustutaProjektNupp").click(function(){
		$(".muudaKustutaEsimene").hide();
		$(".muudaKustutaTeine").show();
	});
	$(".katkestaProjektiKustutamine").click(function(){
		$(".muudaKustutaEsimene").show();
		$(".muudaKustutaTeine").hide();
		$(".muudaArhiveeri").hide();
	});
	
	$("#projektiArhiveerimiseNupp").click(function(){
		$(".muudaKustutaEsimene").hide();
		$(".muudaArhiveeri").show();
	});
	
	$(".tootajaKustutaNupp").click(function(){
		$(this).closest(".tootajaRida").children(".tootajaNimi").children(".tootajaKustutajaComfirmation").show();
		$(this).closest(".tootajaRida").children(".tootajaNimi").children(".tootajaNimiAlam").hide();
	});
	$(".katkestaTootajaKustutamine").click(function(){
		$(".tootajaKustutajaComfirmation").hide();
		$(".tootajaNimiAlam").show();
	});
	
	$(".kustutaStaatusNupp").click(function(){
		$(this).closest(".staatuseNimi").children(".staatuseNimeKiri").hide();
		$(this).closest(".staatuseNimi").children(".staatuseNimeMuutmine").hide();
		$(this).closest(".staatuseNimi").children(".kustutaStaatusConfirmationDiv").show();
	});
	$(".katkestaStaatuseKustutamine").click(function(){
		$(this).closest(".staatuseNimi").children(".staatuseNimeKiri").show();
		$(this).closest(".staatuseNimi").children(".staatuseNimeMuutmine").hide();
		$(this).closest(".staatuseNimi").children(".kustutaStaatusConfirmationDiv").hide();
	});
	
	$("#logiAvamine").click(function () {
		if($(this).html()=="Ava logi"){
			$("#logiTabel").show();
			$(this).html("Sulge logi");
		}
		else{
			$("#logiTabel").hide();
			$(this).html("Ava logi");
		}
	});
	
	$("#kuluPalk").click(function () {
		if($(this).is(':checked')){
			$("#tootajadList").show();
			$("#kuluKirjeldus").hide();
		}
		else{
			$("#palkKasutajaNimi").val("");
			$("#tootajadList").hide();
			$("#kuluKirjeldus").show();
		}
	});
	
	$("#tootajaKuluValimine").change(function () {
		$("#palkKasutajaNimi").val($("#tootajaKuluValimine").val());
	});
	
	
	$(".staatusVastutajaValimine").change(function () {
		
		var projektID = $(this).closest(".staatusVastutajaTd").children(".staatusVastutajaDiv").children(".vastutajaProjektID").html();
		
		muudaVastutajaProjektid(projektID,$(this).val());
		
	});
	
	$(".projektLink").click(function (e) {
		avaProjektiDetiliLeht($(this).closest(".projektdiv").children(".projektIdDiv").html());
	});
	
	$(".projektArhiivisLink").click(function (e) {
		avaArhiivisProjektiDetiliLeht($(this).closest(".projektdiv").children(".projektIdDiv").html());
	});
	
	$(".staatusVastutajaTd").click(function () {
		$(".staatusVastutajaDiv").hide();
		$(".staatusVastutajaText").show();

		$(this).children(".staatusVastutajaDiv").show();
		$(this).children(".staatusVastutajaText").hide();
	});
	
	
});

function avaProjektiDetiliLeht(pid){
	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
}

function avaArhiivisProjektiDetiliLeht(pid){
	document.location.href = "vaadeProjektArhiivisEsimene.htm?id="+pid;
}

function salvestaYldKulud(kulud){

    $.ajax({
        type : "POST",
        url : rakenduseNimi+"/vaadeRahavoog.htm",
        data : {yldKulud : kulud},
        success : function(response) {
        	document.location.href = "vaadeRahavoog.htm";
        },
        error : function(e) {
        	document.location.href = "vaadeRahavoog.htm";
        }
    });
}

function muudaVastutajaProjektid(pid,nimi){
	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektid.htm",
	    data : {vastutajaProjektid : pid, vastutajaNimi: nimi},
	    success : function(response) {
	    	document.location.href = "vaadeProjektid.htm";
	    },
	    error : function(err) {
	    	document.location.href = "vaadeProjektid.htm";
	    }
	});
}

function lisaProjektiKasutaja(pid){

	var e = document.getElementById("uueKasutajaList");
	var nimi = e.options[e.selectedIndex].value;
	
	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektEsimene.htm",
	    data : {kasutajaNimi: nimi, projektID: pid},
	    success : function(response) {
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    },
	    error : function(err) {
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    }
	});
	
}

function listiProjektiKasutajadJaSalvesta(pid){

	var kasutajad = "";
	var osalus = 0;
	
	var table = document.getElementById("kasutajateTabel");
	for(var i = 0, row; row = table.rows[i]; i++){

		if(i>0){
			var kasutaja = "";
			
			kasutaja += row.getElementsByTagName("td")[0].getElementsByTagName("input")[0].checked+"''"; // vastutaja
			kasutaja += row.getElementsByTagName("td")[1].getElementsByTagName("input")[0].checked+"''"; // aktiivne
			kasutaja += row.getElementsByTagName("td")[4].getElementsByTagName("input")[0].value+"''"; // kasutajaID
	     
	        kasutaja += valideeriOsalus(row.getElementsByTagName("td")[3].getElementsByTagName("input")[0].value.toString()); // osalus
			osalus += parseFloat(row.getElementsByTagName("td")[3].getElementsByTagName("input")[0].value.toString());
	        
			kasutajad += kasutaja;
			kasutajad += ";";

		}
		
	}
	
	salvestaProjektiKasutajad(pid,kasutajad,osalus);

}


function salvestaProjektiKasutajad(pid,kasutajadList,osalus){

	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektEsimene.htm",
	    data : {kasutajad: kasutajadList, projektID: pid, osalus: osalus},
	    success : function(response) {
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    },
	    error : function(e) {
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    }
	});
};

$(document).ready(function(){

	$(".kasutjaNimeMuutmiseNupp").click(function(){

		var id = $(this).closest(".kasutajaUueNimeDiv").children("input.kasutajaId").val();
		var nimi = $(this).closest(".kasutajaUueNimeDiv").children(".uueKasutajaNimeLahter").val();

		salvestaKasutajaNimi(id,nimi);
	});

});

function salvestaKasutajaNimi(id,nimi){

	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeTootajadTabel.htm",
	    data : {uusNimi: nimi, kid: id},
	    success : function(response) {
	    	document.location.href = "vaadeTootajadTabel.htm";
	    },
	    error : function(e) {
	    	document.location.href = "vaadeTootajadTabel.htm";
	    }
	});
	
	
};

function valideeriOsalus(osalus){
	var oliJubaPunkt = false;
	
    for(var i = 0; i < osalus.length; i++) {
		if(osalus.charAt(i) == "." && oliJubaPunkt){
    		osalus = osalus.replaceAt(i," ");
		}
		else if(osalus.charAt(i) == "." && !oliJubaPunkt){
			oliJubaPunkt = true;
		}
		else if(isNaN(osalus.charAt(i))){
			osalus = osalus.replaceAt(i," ");
		}
    };

    return osalus;
}

String.prototype.replaceAt=function(index, character) {
    return this.substr(0, index) + character + this.substr(index+character.length);
};

$(document).ready(function() {
    $('.looUusInputLahter').focus(function() {
        if($(this).val() == $(this).data('default_val') || !$(this).data('default_val')) {
            $(this).data('default_val', $(this).val());
            $(this).val('');
        }
    });
    
    $('.looUusInputLahter').blur(function() {
        if ($(this).val() == '') $(this).val($(this).data('default_val'));
    });
    
    $('.tootajaVastutajaCheckbox').change(function() {
    	
    	// kõik ülejäänud muudame mitte valituks
        $(".tootajaVastutajaCheckbox").attr('checked', false);
        this.checked = true;
    });
    
    $('.staatuseNimi').children(".staatuseNimeKiri").click(function() {
    	// kõik ülejäänud muudame tagasi
    	$(".staatuseNimeKiri").show();
    	$(".staatuseNimeMuutmine").hide();
    	
    	// muudame valitud staatust vastavalt
        $(this).closest(".staatuseNimi").children(".staatuseNimeKiri").hide();
        $(this).closest(".staatuseNimi").children(".staatuseNimeMuutmine").show();
        
        // fokuseerib lahtri peale, aga nii et ta ei selectiks tervet tema väärtust
        $(this).find("input.staatuseNimeMuutmiseLahter").focus().val($(this).find("input.staatuseNimeMuutmiseLahter").val());

    });
    
    $('.tootajaPalk').click(function() {
    	// kõik ülejäänud muudame tagasi
    	$(".tootajaPalkText").show();
    	$(".tootajaPalgaMuutmine").hide();
    	
    	// muudame valitud lahtrit vastavalt
        $(this).closest(".tootajaPalk").children(".tootajaPalkText").hide();
        $(this).closest(".tootajaPalk").children(".tootajaPalgaMuutmine").show();
        
        // fokuseerib lahtri peale
        $(this).find("input.tootajaPalgaMuutmiseLahter").focus();

    });
    
    $('.tootajaPalgaMuutmiseLahter').blur(function() {
    	$(this).closest(".tootajaPalk").children(".tootajaPalkText").html($(this).val());
    });
    
    $('.tootajaNimi').children(".tootajaNimiAlam").children(".kasutajaVanaNimeDiv").click(function() {
    	// kõik ülejäänud muudame tagasi
    	$(".kasutajaVanaNimeDiv").show();
    	$(".kasutajaUueNimeDiv").hide();
    	
    	// muudame valitud lahtrit vastavalt
        $(this).closest(".tootajaNimi").children(".tootajaNimiAlam").children(".kasutajaVanaNimeDiv").hide();
        $(this).closest(".tootajaNimi").children(".tootajaNimiAlam").children(".kasutajaUueNimeDiv").show();
        
        // fokuseerib lahtri peale
        $(this).closest(".tootajaNimi").find("input.uueKasutajaNimeLahter").focus().val($(this).closest(".tootajaNimi").find("input.uueKasutajaNimeLahter").val());
    });
});

$(document).ready(function() {
		
    $('#salvestaTootajatePalgad').click(function() {
    	
    	var table = document.getElementById("tootajateTabel");
    	var list = "";
    	
    	var päevad = new Array();

    	päevad[0] = table.rows[2].getElementsByClassName("tootajaPalk")[0].getElementsByClassName("tootajaPalkText")[0].innerHTML;
    	päevad[1] = table.rows[2].getElementsByClassName("tootajaPalk")[1].getElementsByClassName("tootajaPalkText")[0].innerHTML;
    	päevad[2] = table.rows[2].getElementsByClassName("tootajaPalk")[2].getElementsByClassName("tootajaPalkText")[0].innerHTML;
    	
    	for (var i = 3, row; row = table.rows[i]; i++) {
    		
    		if(i==table.rows.length-1){ // viimast rida me ei taha
    			break;
    		}
    		
    		var sisemineList = "";
    		var nimi = row.getElementsByClassName("tootajaNimi")[0].getElementsByClassName("tootajaNimiAlam")[0].getElementsByClassName("kasutajaVanaNimeDiv")[0].innerHTML;
    		
    		var cells = row.getElementsByClassName("tootajaPalk");
    		
    		sisemineList += nimi+="#";
    		
    		for (var j = 0; j<cells.length ; j++) {

    			var palk = cells[j].getElementsByClassName("tootajaPalkText")[0].innerHTML;
    		   	sisemineList += (päevad[j] +";" + palk + "#");
    		   	
    		}
    		
    		list += sisemineList + "/";
    	}

    	var aasta = $('#aastateValikud').val();
    	var kvartal = $('#kvartaliteValikud').val();
    	
    	$.ajax({
    	    type : "POST",
    	    url : rakenduseNimi+"/vaadeTootajadTabel.htm",
    	    data : {tootajad: list, aastaNumber: aasta,kvartal : kvartal},
    	    success : function(response) {
    	    	document.location.href = "vaadeTootajadTabel.htm?aasta="+aasta+"&kvartal="+kvartal;
    	    },
    	    error : function(e) {
    	    	document.location.href = "vaadeTootajadTabel.htm?aasta="+aasta+"&kvartal="+kvartal;
    	    }
    	});
    	
    });
    
    $('#aastateValikud').change(function() {
    	document.location.href = "vaadeTootajadTabel.htm?aasta="+$(this).val()+"&kvartal="+$("#kvartaliteValikud").val();
    });
    $('#kvartaliteValikud').change(function() {
    	document.location.href = "vaadeTootajadTabel.htm?aasta="+$("#aastateValikud").val()+"&kvartal="+$(this).val();
    });
	 $('#rahaVoogAastaValik').change(function() {
		 document.location.href = "vaadeRahavoog.htm?aasta="+$(this).val()+"&kvartal="+$("#rahaVoogKvartaliteValikud").val();
	 });
	 $('#rahaVoogKvartaliteValikud').change(function() {
	     document.location.href = "vaadeRahavoog.htm?aasta="+$("#rahaVoogAastaValik").val()+"&kvartal="+$(this).val();
	 });

});


google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(tekitaPipelineGraaf);
var andmedString = "";

function tekitaPipelineGraaf(sisendString,kaart){
	
	if(sisendString.target){ // kui on event, ehk ei laeta pipeline vaadet
		return;
	}
	if(sisendString == ""){
		return;
	}
	
	if(kaart == "pipeline"){
		
	    var andmed = sisendString.split("/");
	    var tabel = new Array();
	    tabel[0]= ["Staatus", "Tulu", "Kulu" , "Bilanss"] ;
	    
	    for(var i = 1;i < andmed.length ; i++){
	    	var vaheTabel = andmed[i-1].split(";");
	    		tabel[i] = [vaheTabel[0], parseFloat(vaheTabel[1]),parseFloat(vaheTabel[2]),parseFloat(vaheTabel[3])];	
	    }
	    var data = google.visualization.arrayToDataTable(tabel);
	
	    var options = {
	      legend: {position: 'top'},
	      colors: ['green','red', 'blue'],
	      width: 500 + (150 * (tabel.length-1)),
	      vAxis: {title: '€', titleTextStyle: {color: 'red'}},
	      hAxis: {title: 'Staatus', titleTextStyle: {color: 'red'} }
	    	
	    };
	
	    var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
	    chart.draw(data, options);
    
	}
	else if(kaart == "tootajadGraaf"){
		
		andmedString = sisendString;
		
	    var andmed = sisendString.split("/");
	    var tabel = new Array();
	    tabel[0]= ["Aeg", "Kogu Tulu", "Kogu Kulu" , "Bilanss"] ;
	    
	    for(var i = 1;i < andmed.length ; i++){
	    	var vaheTabel = andmed[i-1].split(";");
	    		tabel[i] = [vaheTabel[0], parseFloat(vaheTabel[1]),parseFloat(vaheTabel[2]),parseFloat(vaheTabel[3])];	
	    }
	    var data = google.visualization.arrayToDataTable(tabel);
	
	    var options = {
	      //curveType: "function",
	      legend: {position: 'top'},
	      colors: ['green','red', 'blue'],
	      width: 500 + (50 * (tabel.length-1)),
	      vAxis: {title: '€', titleTextStyle: {color: 'red'}},
	      hAxis: {title: 'Kuupäev', titleTextStyle: {color: 'red'} }
	    };

	    var chart = new google.visualization.LineChart(document.getElementById('chart_div'));
	    chart.draw(data, options);
	    
	}
}

/*
google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(tekitaTootajateGraaf);

function tekitaTootajateGraaf(sisendString){

	if(sisendString.target){ // kui on event, ehk ei laeta töötajate graafi vaadet
		return;
	}
	
    var andmed = sisendString.split("/");
    var tabel = new Array();
    tabel[0]= ["Staatus", "Tulu", "Palgad" , "Kasum"] ;
    
    for(var i = 1;i < andmed.length ; i++){
    	var vaheTabel = andmed[i-1].split(";");
    		tabel[i] = [vaheTabel[0], parseFloat(vaheTabel[1]),parseFloat(vaheTabel[2]),parseFloat(vaheTabel[3])];	
    }
    
    var data = google.visualization.LineChart(tabel);

    var options = {
      legend: {position: 'top'},
      colors: ['green','red', 'blue'],
      width: 150 * (tabel.length-1),
      vAxis: {title: '€', titleTextStyle: {color: 'red'}},
      hAxis: {title: 'Töötaja', titleTextStyle: {color: 'red'} }
    	
    };

    var chart = new google.visualization.ColumnChart(document.getElementById('tootajateGraafDiv'));
    chart.draw(data, options);
}
*/
$(document).ready(function(){
	$("#pipelineAndmed").trigger("click");
	//$("#tootajateGraafAndmed").trigger("click");
});

//projekti tõstmisel vajalikud variables
var draggableProjektID;
var draggableStaatusID;
var draggableProjektiJNR;
var draggableVanaStaatusID;
var draggableVanaProjektiJNR;

//staatuse tõstmisel vajalikud variables
var draggableSStaatusID;
var draggableStaatusJNR;
var draggableVanaSStaatusID;
var draggableVanaStaatusJNR;

var dropToimus = true;

$(document).ready(function(){
	
	//projekti tõstmise drag and drop
	
	$('.dropZoneProjekt').draggable({
		connectToSortable: ".scrollwindow",
	    cursor: 'move',
	    revert: 'invalid',
	    opacity: 0.85,
	    helper: 'clone', 
	    appendTo: 'body',
	    revertDuration: 900,
	    scroll: false,
	    start: function(){
	    	$(this).hide(); 
	        //$(this).css({opacity:0});
	    },
	    stop: function(){
	    	$(this).show();
	        //$(this).css({opacity:1});
	    },
	    drag: function(event, ui) {	
	    	dropToimus = false;
	        var allID = $(this).attr("id");
	        var splitID = allID.split("+");
	        draggableVanaStaatusID = splitID[0];
	        draggableProjektID = splitID[1];
	        draggableVanaProjektiJNR = splitID[2];    
	    }
	});
	
	$(".scrollwindow").sortable({
		placeholder: 'ajutineDiv',
	    connectWith: ".scrollwindow",
	    stop: function(event, ui) {
	    	draggableProjektiJNR = ui.item.index() + 1;
	    	muudaProjektiStaatust();
	    },	
	    receive: function (event, ui) {
	        ui.item.remove(); 
	    }
	});
	  
	$('.scrollwindow').droppable({
		  accept: '.dropZoneProjekt',
		  drop: function(event, ui) {
		   	if(!dropToimus){
		        var allID = $(this).attr("id");
		        var splitID = allID.split("+");
		        draggableStaatusID = splitID[0];
		        dropToimus = true;	        
		    }
		  }
	  });
	  
	  //staatuse tõstmise drag and drop
	  $(".staatusteKonteiner").sortable({
		//placeholder: 'ajutineStaatusDiv',
	    connectWith: ".staatusteKonteiner",
	    stop: function(event, ui) {
	    	draggableStaatusJNR = ui.item.index() + 1;
	    	muudaStaatuseJNR();
	    },
	    receive: function (event, ui) {
	        ui.item.remove(); 
	    }
	  });

	  $('.dropZoneStaatused').draggable({
		connectToSortable: ".staatusteKonteiner",
		cursor: 'move',
	    helper: 'clone',
	    appendTo: 'body',
		revert: 'invalid',
		opacity: 0.85,
	    revertDuration: 900,
	    start: function(){
	    	$(this).hide(); 
	        //$(this).css({opacity:0});
	    },
	    stop: function(){
	    	$(this).show();
	        //$(this).css({opacity:1});
	    },
	    drag: function(event, ui) {
	    	dropToimus = false;
	        var allID = $(this).attr("id");
	        var splitID = allID.split("+");
	        draggableVanaSStaatusID = splitID[0];
	        draggableVanaStaatusJNR = splitID[1];
	    }
	  });
		  
	$('.staatusteKonteiner').droppable({
		  accept: '.dropZoneStaatused',
		  drop: function(event, ui) {
		   	if(!dropToimus){
		        dropToimus = true;	        
		    }
		  }
	});
	
	//arhiivi projekti tõstmise drag and drop
	
	$('.arhiiviProjektDrag').draggable({
		connectToSortable: ".dropZoneArhiiv",
	    cursor: 'move',
	    revert: 'invalid',
	    opacity: 0.85,
	    helper: 'clone', 
	    appendTo: 'body',
	    revertDuration: 900,
	    scroll: true,
	    start: function(){
	    	$(this).hide(); 
	        //$(this).css({opacity:0});;
	    },
	    stop: function(){
	    	$(this).show();
	        //$(this).css({opacity:1});
	    },
	    drag: function(event, ui) {	
	    	dropToimus = false;
	        var allID = $(this).attr("id");
	        var splitID = allID.split("+");
	        draggableVanaStaatusID = splitID[0];
	        draggableProjektID = splitID[1];
	        draggableVanaProjektiJNR = splitID[2];    
	    }
	});
	
	$(".dropZoneArhiiv").sortable({
		placeholder: 'ajutineDiv',
	    connectWith: ".dropZoneArhiiv",
	    stop: function(event, ui) {
	    	draggableProjektiJNR = ui.item.index() + 1;
	    	muudaProjektiStaatustArhiivis();
	    },	
	    receive: function (event, ui) {
	        ui.item.remove(); 
	    }
	});
	  
	$('.dropZoneArhiiv').droppable({
		  accept: '.arhiiviProjektDrag',
		  drop: function(event, ui) {
		   	if(!dropToimus){
		        var allID = $(this).attr("id");
		        var splitID = allID.split("+");
		        draggableStaatusID = splitID[0];
		        dropToimus = true;	        
		    }
		  }
	  });
});

function muudaProjektiStaatust(){

	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektid.htm",
	    data : {staatusDragId: draggableStaatusID, projektDragId: draggableProjektID,
	    	projektiDragJNR: draggableProjektiJNR, staatusVanaDragId: draggableVanaStaatusID, 
	    	projektiVanaDragJNR: draggableVanaProjektiJNR},
	    success : function(response) {
	    	document.location.href = "vaadeProjektid.htm";
	    },
	    error : function(e) {
	    	document.location.href = "vaadeProjektid.htm";
	    }
	});
};

function muudaStaatuseJNR(){

	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektid.htm",
	    data : {staatuseSVanaDragId: draggableVanaSStaatusID, staatuseSJNR: draggableStaatusJNR,
	    	staatuseSVanaJNR: draggableVanaStaatusJNR},
	    success : function(response) {
	    	document.location.href = "vaadeProjektid.htm";
	    },
	    error : function(e) {
	    	document.location.href = "vaadeProjektid.htm";
	    }
	});
};

function muudaProjektiStaatustArhiivis(){

	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeArhiiv.htm",
	    data : {staatusDragId: draggableStaatusID, projektDragId: draggableProjektID,
	    	projektiDragJNR: draggableProjektiJNR, staatusVanaDragId: draggableVanaStaatusID, 
	    	projektiVanaDragJNR: draggableVanaProjektiJNR},
	    success : function(response) {
	    	document.location.href = "vaadeArhiiv.htm";
	    },
	    error : function(e) {
	    	document.location.href = "vaadeArhiiv.htm";
	    }
	});
};
