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
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    },
	    error : function(err) {
	    	document.location.href = "vaadeProjektEsimene.htm?id="+pid;
	    }
	});
	
}

function listiProjektiKasutajadJaSalvesta(rakenduseNimi,pid){

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
	
	salvestaProjektiKasutajad(rakenduseNimi,pid,kasutajad);
}


function salvestaProjektiKasutajad(rakenduseNimi,pid,kasutajadList){

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