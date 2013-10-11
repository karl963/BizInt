package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.Staatus;
import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;
import bizint.post.UusKasutaja;
import bizint.post.UusKirjeldus;
import bizint.post.UusKommentaar;
import bizint.post.UusProjektiNimi;

@Controller
public class ProjektController {

	private String teade = null;
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.GET, params={"id"})
	public String vaadeProjektEsimene(@RequestParam("id") int projektID, Model m) {
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		List<Logi> logi = new ArrayList<Logi>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		List<Kasutaja> k�ikKasutajad = new ArrayList<Kasutaja>();
		List<Kulu> kulud = new ArrayList<Kulu>();
		List<Tulu> tulud = new ArrayList<Tulu>();
		
		String nimi = null;
		String kirjeldus = null;
		
		Connection con = Mysql.connection;;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT kirjeldus, projektNimi FROM projektid WHERE projektID="+projektID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			nimi = rs.getString("projektNimi");
			kirjeldus = rs.getString("kirjeldus");
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT kasutajaNimi,aktiivne,vastutaja,osalus,kasutajaID FROM projektikasutajad, kasutajad WHERE projektikasutajad.projekt_ID="+projektID+" AND kasutajad.kasutajaID=projektikasutajad.kasutaja_ID";
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			while(rs2.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs2.getString("kasutajaNimi");
				boolean aktiivne = rs2.getBoolean("aktiivne");
				boolean vastutaja = rs2.getBoolean("vastutaja");
				Double osalus = rs2.getDouble("osalus");
				int kasutajaID = rs2.getInt("kasutajaID");
				
				kasutaja.setKasutajaID(kasutajaID);
				kasutaja.setKasutajaNimi(kasutajaNimi);
				kasutaja.setAktiivne(aktiivne);
				kasutaja.setVastutaja(vastutaja);
				kasutaja.setOsalus(osalus);
				
				kasutajad.add(kasutaja);
			}
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT sonum,aeg FROM logid WHERE projekt_ID="+projektID;
			ResultSet rs3 = stmt3.executeQuery(query3);
			
			while(rs3.next()){
				Logi log = new Logi();
				
				String sonum = rs3.getString("sonum");
				Date aeg = rs3.getTimestamp("aeg");
				
				log.setSonum(sonum);
				log.setAeg(aeg);
				
				logi.add(log);
			}

			Statement stmt4 = con.createStatement();
			String query4 = "SELECT sonum, aeg, kasutajaNimi FROM kommentaarid, kasutajad WHERE kommentaarid.projekt_ID="+projektID+" AND kommentaarid.kasutaja_ID=kasutajad.kasutajaID";
			ResultSet rs4 = stmt4.executeQuery(query4);

			while(rs4.next()){

				Kommentaar kommentaar = new Kommentaar();
				
				Date aeg = rs4.getTimestamp("aeg");
				String s�num = rs4.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				kasutaja.setKasutajaNimi(rs4.getString("kasutajaNimi"));
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(s�num);
				
				kommentaarid.add(kommentaar);
			}
			
			Statement stmt5 = con.createStatement();
			String query5 = "SELECT kasutajaNimi FROM kasutajad";
			ResultSet rs5 = stmt5.executeQuery(query5);
			
			while(rs5.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs5.getString("kasutajaNimi");
				kasutaja.setKasutajaNimi(kasutajaNimi);

				k�ikKasutajad.add(kasutaja);
			}
			
			Statement stmt7 = con.createStatement();
			String query7 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID;
			ResultSet rs7 = stmt7.executeQuery(query7);
			
			while(rs7.next()){

				Tulu tulu = new Tulu();
				
				Double summa = rs7.getDouble("tulu");
				tulu.setSumma(summa);
				
				tulud.add(tulu);
			}
			
			Statement stmt6 = con.createStatement();
			String query6 = "SELECT kulu FROM kulud WHERE projekt_ID="+projektID;
			ResultSet rs6 = stmt6.executeQuery(query6);
			
			while(rs6.next()){
				Kulu kulu = new Kulu();
				
				Double summa = rs6.getDouble("kulu");
				kulu.setSumma(summa);
				
				kulud.add(kulu);
			}
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setNimi(nimi);
		projekt.setId(projektID);
		projekt.setKasutajad(kasutajad);
		projekt.setKirjeldus(kirjeldus);
		projekt.setLogi(logi);
		projekt.setKulud(kulud);
		projekt.setTulud(tulud);
		projekt.setKommentaarid(kommentaarid);
		
		m.addAttribute("kasutajad",k�ikKasutajad);
		m.addAttribute("projekt", projekt);
		m.addAttribute("message", teade);
		m.addAttribute("uusKommentaar", new UusKommentaar());
		m.addAttribute("uusKirjeldus", new UusKirjeldus());
		m.addAttribute("uusProjektiNimi", new UusProjektiNimi(projekt.getNimi(),projekt.getId()));
		m.addAttribute("eemaldaKasutaja",new Kasutaja());
		
		teade = null;
		
		return "vaadeProjektEsimene"; 
	}

	@RequestMapping("/vaadeProjektTeine.htm")
	public String vaadeProjektTeine(@RequestParam("id") int projektID, Model m) {
		
		List<Kulu> kulud = new ArrayList<Kulu>();
		List<Tulu> tulud = new ArrayList<Tulu>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		
		String nimi = null;
		
		Connection con = Mysql.connection;;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT projektNimi FROM projektid WHERE projektID="+projektID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			nimi = rs.getString("projektNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT tulu, tuluNimi, aeg FROM tulud WHERE projekt_ID="+projektID;
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			while(rs2.next()){

				Tulu tulu = new Tulu();
				
				Double summa = rs2.getDouble("tulu");
				String tuluNimi = rs2.getString("tuluNimi");
				Date aeg = rs2.getTimestamp("aeg");
				
				tulu.setSumma(summa);
				tulu.setTuluNimi(tuluNimi);
				tulu.setAeg(aeg);
				
				tulud.add(tulu);
			}
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT kulu, kuluNimi, aeg FROM kulud WHERE projekt_ID="+projektID;
			ResultSet rs3 = stmt3.executeQuery(query3);
			
			while(rs3.next()){
				Kulu kulu = new Kulu();
				
				Double summa = rs3.getDouble("kulu");
				String kuluNimi = rs3.getString("kuluNimi");
				Date aeg = rs3.getTimestamp("aeg");
				
				kulu.setSumma(summa);
				kulu.setKuluNimi(kuluNimi);
				kulu.setAeg(aeg);
				
				kulud.add(kulu);
			}
			
			Statement stmt4 = con.createStatement();
			String query4 = "SELECT sonum, aeg, kasutajaNimi FROM kommentaarid, kasutajad WHERE kommentaarid.projekt_ID="+projektID+" AND kommentaarid.kasutaja_ID=kasutajad.kasutajaID";
			ResultSet rs4 = stmt4.executeQuery(query4);

			while(rs4.next()){

				Kommentaar kommentaar = new Kommentaar();
				
				Date aeg = rs4.getTimestamp("aeg");
				String s�num = rs4.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				kasutaja.setKasutajaNimi(rs4.getString("kasutajaNimi"));
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(s�num);
				
				kommentaarid.add(kommentaar);
			}
		}catch(Exception x){
			x.printStackTrace();
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setNimi(nimi);
		projekt.setId(projektID);
		projekt.setKulud(kulud);
		projekt.setTulud(tulud);
		projekt.setKommentaarid(kommentaarid);
		
		m.addAttribute("projekt", projekt);
		m.addAttribute("message", teade);
		m.addAttribute("uusKommentaar", new UusKommentaar());
		m.addAttribute("kustutaKulu", new Kulu());
		m.addAttribute("kustutaTulu", new Tulu());
		m.addAttribute("uusTulu", new Tulu());
		m.addAttribute("uusKulu", new Kulu());
		m.addAttribute("uusProjektiNimi", new UusProjektiNimi(projekt.getNimi(),projekt.getId()));
		
		teade = null;
		
		return "vaadeProjektTeine";
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"sonum","projektID"})
	public View addKommentaar1(@ModelAttribute("uusKommentaar") UusKommentaar uusKommentaar, Model m){

		int vastus = Projekt.lisaKommentaarAndmebaasi(uusKommentaar);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Kommenteerimine �nnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+uusKommentaar.getProjektID());
	}
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"sonum","projektID"})
	public View addKommentaar2(@ModelAttribute("uusKommentaar") UusKommentaar uusKommentaar, Model m){

		int vastus = Projekt.lisaKommentaarAndmebaasi(uusKommentaar);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Kommenteerimine �nnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+uusKommentaar.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"kirjeldus","projektID"})
	public View muudaKirjeldus(@ModelAttribute("uusKirjeldus") UusKirjeldus uusKirjeldus, Model m){

		int vastus = Projekt.muudaProjektiKirjeldusAndmebaasis(uusKirjeldus);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Kirjelduse muutmine �nnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+uusKirjeldus.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"kasutajaNimi","projektID"})
	public View addKasutaja(@ModelAttribute("uusKasutaja") UusKasutaja uusKasutaja, Model m){

		int vastus = Projekt.lisaUusKasutajaAndmebaasi(uusKasutaja);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Kasutaja lisamine �nnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+uusKasutaja.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"aeg.time","tuluNimi","summa","stringAeg","projektID"})
	public View addTulu(@ModelAttribute("uusTulu") Tulu tulu, Model m){

		Date uusAeg = Tulu.muudaStringAjaks(tulu.getStringAeg());
		
		if(uusAeg==null){
			teade="Tulu kuup�ev peab olema kujul 'p�ev.kuu.aasta' ning eraldajaks v�ivad olla .,;-_/";
		}
		else{
			
			tulu.setAeg(uusAeg);
			
			int vastus = Projekt.lisaTuluAndmebaasi(tulu);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
				teade = "Viga andmebaasiga �hendumisel";
			}
			else{
				teade = "Tulu lisamine �nnestus";
			}
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+tulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"aeg.time","kuluNimi","summa","stringAeg","projektID"})
	public View addKulu(@ModelAttribute("uusKulu") Kulu kulu, Model m){

		Date uusAeg = Kulu.muudaStringAjaks(kulu.getStringAeg());
		
		if(uusAeg==null){
			teade="Kulu kuup�ev peab olema kujul 'p�ev.kuu.aasta' ning eraldajaks v�ivad olla .,;-_/";
		}
		else{
			
			kulu.setAeg(uusAeg);
			
			int vastus = Projekt.lisaKuluAndmebaasi(kulu);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
				teade = "Viga andmebaasiga �hendumisel";
			}
			else{
				teade = "Kulu lisamine �nnestus";
			}
		}
		return new RedirectView("vaadeProjektTeine.htm?id="+kulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"kuluNimi","summa","stringAeg","projektID"})
	public View kustutaKulu(@ModelAttribute("kustutaKulu") Kulu kulu, Model m){
		
		int vastus = Projekt.kustutaKuluAndmebaasist(kulu);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Kulu eemaldamine �nnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+kulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"tuluNimi","summa","stringAeg","projektID"})
	public View kustutaTulu(@ModelAttribute("kustutaTulu") Tulu tulu, Model m){

		int vastus = Projekt.kustutaTuluAndmebaasist(tulu);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Tulu eemaldamine �nnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+tulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"kustuta","projektID"})
	public View kustutaProjekt(@RequestParam("projektID") int id,@RequestParam("kustuta") String k�sk, Model m){

		if(k�sk.equals("jah")){
			
			int vastus = Projekt.kustutaProjektAndmebaasist(id);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
				teade = "Viga andmebaasiga �hendumisel";
				return new RedirectView("vaadeProjektEsimene.htm?id="+id);
			}
			else{
				//teade = "Projekti kustutamine �nnestus";
				return new RedirectView("vaadeProjektid.htm");
			}
			
		}
		else{
			return new RedirectView("vaadeProjektEsimene.htm?id="+id);
		}
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"uusNimi","projektID"})
	public View muudaProjektiNime1(@ModelAttribute("uusProjektiNimi") UusProjektiNimi nimi, Model m){
		
		int vastus = Projekt.muudaProjektiNimeAndmebaasis(nimi);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Projekti nime muutmine �nnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+nimi.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"uusNimi","projektID"})
	public View muudaProjektiNime2(@ModelAttribute("uusProjektiNimi") UusProjektiNimi nimi, Model m){

		int vastus = Projekt.muudaProjektiNimeAndmebaasis(nimi);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Projekti nime muutmine �nnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+nimi.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"projektID","kasutajaID"})
	public View eemaldaKasutaja(@RequestParam("kasutajaID") int kasutajaID,@RequestParam("projektID") int projektID, Model m){
		
		
		int vastus = Projekt.eemaldaKasutajaProjektistAndmebaasis(kasutajaID,projektID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "T��taja eemaldamine �nnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+projektID);
	}
	
}
