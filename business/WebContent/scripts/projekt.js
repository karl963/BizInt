$(document).ready(function(){

	$("#projektiNimeMuutmiseNupp").click(function(){
		$("#projektiNimi").hide();
		$("#projektiNimeMuutmine").show();
		$("#projektiNimeMuutmiseNupp").hide();
	});

});

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
	
	var table = document.getElementById("kasutajateTabel");
	for(var i = 0, row; row = table.rows[i]; i++){

		if(i>0){
			var kasutaja = "";
			
			kasutaja += row.getElementsByTagName("td")[0].getElementsByTagName("input")[0].checked+"''"; // vastutaja
			kasutaja += row.getElementsByTagName("td")[1].getElementsByTagName("input")[0].checked+"''"; // aktiivne
			kasutaja += row.getElementsByTagName("td")[4].getElementsByTagName("input")[0].value+"''"; // kasutajaID
	     
	        kasutaja += valideeriOsalus(row.getElementsByTagName("td")[3].getElementsByTagName("input")[0].value.toString()); // osalus
			
			kasutajad += kasutaja;
			kasutajad += ";";

		}
		
	}
	
	salvestaProjektiKasutajad(pid,kasutajad);
}


function salvestaProjektiKasutajad(pid,kasutajadList){

	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektEsimene.htm",
	    data : {kasutajad: kasutajadList, projektID: pid},
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
    
    $('.staatuseNimi').click(function() {
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
    
    $('.tootajaNimi').click(function() {
    	// kõik ülejäänud muudame tagasi
    	$(".kasutajaVanaNimeDiv").show();
    	$(".kasutajaUueNimeDiv").hide();
    	
    	// muudame valitud lahtrit vastavalt
        $(this).closest(".tootajaNimi").children(".kasutajaVanaNimeDiv").hide();
        $(this).closest(".tootajaNimi").children(".kasutajaUueNimeDiv").show();
        
        // fokuseerib lahtri peale
        $(this).find("input.uueKasutajaNimeLahter").focus().val($(this).find("input.uueKasutajaNimeLahter").val());
    });
});

$(document).ready(function() {
		
    $('#salvestaTootajatePalgad').click(function() {
    	
    	var table = document.getElementById("tootajateTabel");
    	var list = "";

    	for (var i = 2, row; row = table.rows[i]; i++) {
    		
    		if(i==table.rows.length-1){ // viimast rida me ei taha
    			break;
    		}
    		
    		var sisemineList = "";
    		var nimi = row.getElementsByClassName("tootajaNimi")[0].getElementsByClassName("kasutajaVanaNimeDiv")[0].innerHTML;
    		
    		var cells = row.getElementsByClassName("tootajaPalk");

    		for (var j = 0; j<cells.length ; j++) {

    			var palk = cells[j].getElementsByClassName("tootajaPalkText")[0].innerHTML;
    		   	sisemineList += nimi + ";"+ (j+1) +";" + palk + "#";
    		   	
    		}
    		
    		list += sisemineList + "/";
    	}

    	var aasta = $('#aastateValikud').val();
    	
    	$.ajax({
    	    type : "POST",
    	    url : rakenduseNimi+"/vaadeTootajadTabel.htm",
    	    data : {tootajad: list, aastaNumber: aasta},
    	    success : function(response) {
    	    	document.location.href = "vaadeTootajadTabel.htm?aasta="+aasta;
    	    },
    	    error : function(e) {
    	    	document.location.href = "vaadeTootajadTabel.htm?aasta="+aasta;
    	    }
    	});
    	
    });
    
    $('#aastateValikud').change(function() {
    	document.location.href = "vaadeTootajadTabel.htm?aasta="+$(this).val();
    });

});


google.load("visualization", "1", {packages:["corechart"]});
google.setOnLoadCallback(tekitaTabel);

function tekitaTabel(sisendString){

	if(sisendString.target){ // kui on event, ehk ei laeta pipeline vaadet
		return;
	}
	
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
      width: 150 * (tabel.length-1),
      vAxis: {title: '€', titleTextStyle: {color: 'red'}},
      hAxis: {title: 'Staatus', titleTextStyle: {color: 'red'} }
    	
    };

    var chart = new google.visualization.ColumnChart(document.getElementById('chart_div'));
    chart.draw(data, options);
}

$(document).ready(function(){
	$("#pipelineAndmed").trigger("click");
});
