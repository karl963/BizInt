$(document).ready(function(){

	$("#projektiNimeMuutmiseNupp").click(function(){
		$("#projektiNimi").hide();
		$("#projektiNimeMuutmine").show();
		$("#projektiNimeMuutmiseNupp").hide();
	});

});

function lisaProjektiKasutaja(rakenduseNimi,pid){

	var e = document.getElementById("uueKasutajaList");
	var nimi = e.options[e.selectedIndex].value;
	
	$.ajax({
	    type : "POST",
	    url : rakenduseNimi+"/vaadeProjektEsimene.htm",
	    data : {kasutajaNimi: nimi, projektID: pid},
	    success : function(response) {
	    	//$(location).attr('href',"vaadeProjektEsimene.htm?id="+pid);
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    },
	    error : function(err) {
	    }
	});
	
}

function salvestaProjektiKasutajad(){
	
	$.ajax({
	    type : "POST",
	    url : "/vaadeProjektEsimene",
	    data : {kasutajad: k},
	    success : function(response) {
	       // do something ... 
	    },
	    error : function(e) {
	       alert('Error: ' + e);
	    }
	});
	
}