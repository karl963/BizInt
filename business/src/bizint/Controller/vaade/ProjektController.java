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
		
		/*
		 * nimi,reiting,kasutaja(vastutaja,aktiivne,nimi,osalus),kirjeldus,logi(sonum,formaaditudaeg),kommentaar(kasutaja,sonum,formaaditudaeg)
		 */
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		List<Logi> logi = new ArrayList<Logi>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		List<Kasutaja> kõikKasutajad = new ArrayList<Kasutaja>();
		
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
			String query2 = "SELECT kasutajaNimi,aktiivne,vastutaja,osalus FROM projektikasutajad, kasutajad WHERE projektikasutajad.projekt_ID="+projektID+" AND kasutajad.kasutajaID=projektikasutajad.kasutaja_ID";
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			while(rs2.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs2.getString("kasutajaNimi");
				boolean aktiivne = rs2.getBoolean("aktiivne");
				boolean vastutaja = rs2.getBoolean("vastutaja");
				Double osalus = rs2.getDouble("osalus");
				
				kasutaja.setKasutajaNimi(kasutajaNimi);
				kasutaja.setAktiivne(aktiivne);
				kasutaja.setVastutaja(vastutaja);
				kasutaja.setOsalus(osalus);
				
				kasutajad.add(kasutaja);
			}
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT sonum,aeg FROM logid, projektid WHERE logid.projekt_ID="+projektID;
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
				String sõnum = rs4.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				kasutaja.setKasutajaNimi(rs4.getString("kasutajaNimi"));
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(sõnum);
				
				kommentaarid.add(kommentaar);
			}
			
			Statement stmt5 = con.createStatement();
			String query5 = "SELECT kasutajaNimi FROM kasutajad";
			ResultSet rs5 = stmt5.executeQuery(query5);
			
			while(rs5.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs5.getString("kasutajaNimi");
				kasutaja.setKasutajaNimi(kasutajaNimi);

				kõikKasutajad.add(kasutaja);
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
		projekt.setKommentaarid(kommentaarid);
		
		m.addAttribute("kasutajad",kõikKasutajad);
		m.addAttribute("projekt", projekt);
		m.addAttribute("message", teade);
		m.addAttribute("uusKommentaar", new UusKommentaar());
		m.addAttribute("uusKirjeldus", new UusKirjeldus());
		m.addAttribute("uusProjektiNimi", new UusProjektiNimi(projekt.getNimi(),projekt.getId()));
		
		
		teade = null;
		
		return "vaadeProjektEsimene"; 
	}

	@RequestMapping("/vaadeProjektTeine.htm")
	public String vaadeProjektTeine(@RequestParam("id") int projektID, Model m) {
		
		/*
		 * nimi,kogutulu,tulu(summa,nimi,formaaditudaeg),kulu(summa,nimi,formaaditudaeg),kommentaar(sonum,kasutaja,formaaditudaeg)
		 */
		
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
			String query3 = "SELECT  kuluNimi, aeg FROM kulud WHERE projekt_ID="+projektID;
			ResultSet rs3 = stmt3.executeQuery(query3);
			
			while(rs3.next()){
				Kulu kulu = new Kulu();
				
				Double summa = rs.getDouble("kulu");
				String kuluNimi = rs.getString("kuluNimi");
				Date aeg = rs.getTimestamp("aeg");
				
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
				String sõnum = rs4.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				kasutaja.setKasutajaNimi(rs4.getString("kasutajaNimi"));
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(sõnum);
				
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
		m.addAttribute("uusTulu", new Tulu());
		m.addAttribute("uusKulu", new Kulu());
		m.addAttribute("kustutaKulu", new Kulu());
		m.addAttribute("kustutaTulu", new Tulu());
		m.addAttribute("uusProjektiNimi", new UusProjektiNimi(projekt.getNimi(),projekt.getId()));
		
		teade = null;
		
		return "vaadeProjektTeine";
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"sonum","projektID"})
	public View addKommentaar1(@ModelAttribute("uusKommentaar") UusKommentaar uusKommentaar, Model m){

		int vastus = Projekt.lisaKommentaarAndmebaasi(uusKommentaar);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kommenteerimine õnnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+uusKommentaar.getProjektID());
	}
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"sonum","projektID"})
	public View addKommentaar2(@ModelAttribute("uusKommentaar") UusKommentaar uusKommentaar, Model m){

		int vastus = Projekt.lisaKommentaarAndmebaasi(uusKommentaar);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kommenteerimine õnnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+uusKommentaar.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"kirjeldus","projektID"})
	public View muudaKirjeldus(@ModelAttribute("uusKirjeldus") UusKirjeldus uusKirjeldus, Model m){

		int vastus = Projekt.muudaProjektiKirjeldusAndmebaasis(uusKirjeldus);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kirjelduse muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+uusKirjeldus.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"kasutajaNimi","projektID"})
	public View addKasutaja(@ModelAttribute("uusKasutaja") UusKasutaja uusKasutaja, Model m){

		int vastus = Projekt.lisaUusKasutajaAndmebaasi(uusKasutaja);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kasutaja lisamine õnnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+uusKasutaja.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"tuluNimi","summa","aeg","projektID"})
	public View addTulu(@ModelAttribute("uusTulu") Tulu tulu, Model m){

		int vastus = Projekt.lisaTuluAndmebaasi(tulu);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Tulu lisamine õnnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+tulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"kuluNimi","summa","aeg","projektID"})
	public View addKulu(@ModelAttribute("uusKulu") Kulu kulu, Model m){

		int vastus = Projekt.lisaKuluAndmebaasi(kulu);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kulu lisamine õnnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+kulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"kustutaKulu"})
	public View kustutaKulu(@ModelAttribute("kustutaKulu") Kulu kulu, Model m){
		
		int vastus = Projekt.kustutaKuluAndmebaasist(kulu);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kulu eemaldamine õnnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+kulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"kustutaTulu"})
	public View kustutaTulu(@ModelAttribute("kustutaTulu") Tulu tulu, Model m){

		int vastus = Projekt.kustutaTuluAndmebaasist(tulu);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Tulu eemaldamine õnnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+tulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"id","kustuta"})
	public View kustutaProjekt(@RequestParam("id") int id,@RequestParam("kustuta") String käsk, Model m){

		if(käsk.equals("jah")){
			
			int vastus = Projekt.kustutaProjektAndmebaasist(id);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
				teade = "Viga andmebaasiga ühendumisel";
				return new RedirectView("vaadeProjektEsimene.htm?id="+id);
			}
			else{
				//teade = "Projekti kustutamine õnnestus";
				return new RedirectView("vaadeProjektid.htm");
			}
			
		}
		else{
			return new RedirectView("vaadeProjektEsimene.htm?id="+id);
		}
	}
	
	@RequestMapping(value = "/vaadeProjektEsimene.htm", method = RequestMethod.POST, params={"uusProjektiNimi"})
	public View muudaProjektiNime1(@ModelAttribute("uusProjektiNimi") UusProjektiNimi nimi, Model m){

		int vastus = Projekt.muudaProjektiNimeAndmebaasis(nimi);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti nime muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektEsimene.htm?id="+nimi.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektTeine.htm", method = RequestMethod.POST, params={"uusProjektiNimi"})
	public View muudaProjektiNime2(@ModelAttribute("uusProjektiNimi") UusProjektiNimi nimi, Model m){

		int vastus = Projekt.muudaProjektiNimeAndmebaasis(nimi);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti nime muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektTeine.htm?id="+nimi.getProjektID());
	}
	
}
