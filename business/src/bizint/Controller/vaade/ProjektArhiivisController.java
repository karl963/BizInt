package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import bizint.post.PaneProjektArhiivi;

@Controller
public class ProjektArhiivisController {

	private String teade = null;
	private int juhtID = 0;
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.GET, params={"id"})
	public String vaadeProjektArhiivisEsimene(HttpServletRequest request,@RequestParam("id") int projektID, Model m) {
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			return "redirect:/vaadeViga.htm";
		}
		
		if(request.getSession().getAttribute("juhtID") == null){
			juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
			request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
		}
		else{
			juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
		}
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		List<Logi> logi = new ArrayList<Logi>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		List<Kasutaja> kõikKasutajad = new ArrayList<Kasutaja>();
		List<Kulu> kulud = new ArrayList<Kulu>();
		List<Tulu> tulud = new ArrayList<Tulu>();
		
		String nimi = null;
		String kirjeldus = null;
		int reiting = 1;
		
		Connection con = new Mysql().getConnection();;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT kirjeldus, projektNimi, reiting FROM projektid WHERE projektID="+projektID+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			nimi = rs.getString("projektNimi");
			kirjeldus = rs.getString("kirjeldus");
			reiting = rs.getInt("reiting");
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT kasutajaNimi,aktiivne,vastutaja,osalus,kasutajaID FROM projektikasutajad, kasutajad WHERE projektikasutajad.projekt_ID="+projektID+" AND kasutajad.kasutajaID=projektikasutajad.kasutaja_ID AND kasutajad.juhtID="+juhtID+" AND projektikasutajad.juhtID="+juhtID;
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
			
			try{rs2.close();stmt2.close();}catch(Exception x){}
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT sonum,aeg FROM logid WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;;
			ResultSet rs3 = stmt3.executeQuery(query3);
			
			while(rs3.next()){
				Logi log = new Logi();
				
				String sonum = rs3.getString("sonum");
				Date aeg = rs3.getTimestamp("aeg");
				
				log.setSonum(sonum);
				log.setAeg(aeg);
				
				logi.add(log);
			}
			
			try{rs3.close();stmt3.close();}catch(Exception x){}

			Statement stmt4 = con.createStatement();
			String query4 = "SELECT sonum, aeg, kasutajaNimi FROM juhid, kommentaarid WHERE kommentaarid.projekt_ID="+projektID+" AND kommentaarid.juhtID="+juhtID+" AND juhid.juhtID="+juhtID;
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
			
			try{rs4.close();stmt4.close();}catch(Exception x){}
			
			Statement stmt5 = con.createStatement();
			String query5 = "SELECT kasutajaNimi, kasutajaID FROM kasutajad WHERE töötab=1 AND juhtID="+juhtID;
			ResultSet rs5 = stmt5.executeQuery(query5);
			
			while(rs5.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs5.getString("kasutajaNimi");
				int kasutajaID = rs5.getInt("kasutajaID");
				
				kasutaja.setKasutajaNimi(kasutajaNimi);
				kasutaja.setKasutajaID(kasutajaID);

				kõikKasutajad.add(kasutaja);
			}
			
			try{rs5.close();stmt5.close();}catch(Exception x){}
			
			Statement stmt7 = con.createStatement();
			String query7 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
			ResultSet rs7 = stmt7.executeQuery(query7);
			
			while(rs7.next()){

				Tulu tulu = new Tulu();
				
				Double summa = rs7.getDouble("tulu");
				tulu.setSumma(summa);
				
				tulud.add(tulu);
			}
			
			try{rs7.close();stmt7.close();}catch(Exception x){}
			
			Statement stmt6 = con.createStatement();
			String query6 = "SELECT kulu FROM kulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
			ResultSet rs6 = stmt6.executeQuery(query6);
			
			while(rs6.next()){
				Kulu kulu = new Kulu();
				
				Double summa = rs6.getDouble("kulu");
				kulu.setSumma(summa);
				
				kulud.add(kulu);
			}
			
			try{rs6.close();stmt6.close();}catch(Exception x){}
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		
		// võtame välja kasutajad (kõikKasutajad listist) kes juba osalevad projektis
		for(Kasutaja projektiKasutaja : kasutajad){
			for(Kasutaja ülejäänudKasutaja : kõikKasutajad){
				if(ülejäänudKasutaja.getKasutajaID() == projektiKasutaja.getKasutajaID()){
					kõikKasutajad.remove(ülejäänudKasutaja);
					break;
				}
			}
		}
		
		// teeme reitinguHTML-i
		String html = "";
		for(int i = 1; i <= 5 ;i++){
			if(i <= reiting){
				html+="<a class='reitingNuppOn' href='vaadeProjektArhiivisEsimene.htm?projektID="+projektID+"&reiting="+i+"' >"+i+"</a> ";
			}
			else{
				html+="<a class='reitingNuppPole' href='vaadeProjektArhiivisEsimene.htm?projektID="+projektID+"&reiting="+i+"' >"+i+"</a> ";
			}
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setReitinguHTML(html);
		projekt.setNimi(nimi);
		projekt.setId(projektID);
		projekt.setKasutajad(kasutajad);
		projekt.setKirjeldus(kirjeldus);
		projekt.setLogi(logi);
		projekt.setKulud(kulud);
		projekt.setTulud(tulud);
		projekt.setKommentaarid(kommentaarid);
		
		m.addAttribute("kasutajad",kõikKasutajad);
		m.addAttribute("projekt", projekt);
		m.addAttribute("teade", teade);
		m.addAttribute("uusKommentaar", new UusKommentaar());
		m.addAttribute("uusKirjeldus", new UusKirjeldus());
		m.addAttribute("uusProjektiNimi", new UusProjektiNimi(projekt.getNimi(),projekt.getId()));
		m.addAttribute("paneProjektArhiivi", new PaneProjektArhiivi(projekt.getId()));
		m.addAttribute("eemaldaKasutaja",new Kasutaja());
		
		teade = null;
		
		return "vaadeProjektArhiivisEsimene"; 
	}

	@RequestMapping("/vaadeProjektArhiivisTeine.htm")
	public String vaadeProjektArhiivisTeine(HttpServletRequest request,@RequestParam("id") int projektID, Model m) {
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			return "redirect:/vaadeViga.htm";
		}
		
		if(request.getSession().getAttribute("juhtID") == null){
			juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
			request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
		}
		else{
			juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
		}
		
		List<Kulu> kulud = new ArrayList<Kulu>();
		List<Tulu> tulud = new ArrayList<Tulu>();
		List<String> tootajad = new ArrayList<String>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		
		String nimi = null;
		int reiting = 1;
		
		Connection con = new Mysql().getConnection();;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT projektNimi, reiting FROM projektid WHERE projektID="+projektID+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			nimi = rs.getString("projektNimi");
			reiting = rs.getInt("reiting");
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT tulu, tuluNimi, aeg FROM tulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
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
			
			try{rs2.close();stmt2.close();}catch(Exception x){}
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT kulu, kuluNimi, aeg FROM kulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
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
			
			try{rs3.close();stmt3.close();}catch(Exception x){}
			
			Statement stmt4 = con.createStatement();
			String query4 = "SELECT sonum, aeg, kasutajaNimi FROM juhid, kommentaarid WHERE kommentaarid.projekt_ID="+projektID+" AND kommentaarid.juhtID="+juhtID+" AND juhid.juhtID="+juhtID;
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
			
			try{rs4.close();stmt4.close();}catch(Exception x){}
			
			Statement stmt5 = con.createStatement();
			String query5 = "SELECT kasutajaNimi FROM kasutajad WHERE töötab=1 AND juhtID="+juhtID;
			ResultSet rs5 = stmt5.executeQuery(query5);

			while(rs5.next()){

				tootajad.add(rs5.getString("kasutajaNimi"));

			}
			
			try{rs5.close();stmt5.close();}catch(Exception x){}
		}catch(Exception x){
			x.printStackTrace();
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		// teeme reitinguHTML-i
		String html = "";
		for(int i = 1; i <= 5 ;i++){
			if(i <= reiting){
				html+="<a class='reitingNuppOn' href='vaadeProjektArhiivisTeine.htm?projektID="+projektID+"&reiting="+i+"' >"+i+"</a> ";
			}
			else{
				html+="<a class='reitingNuppPole' href='vaadeProjektArhiivisTeine.htm?projektID="+projektID+"&reiting="+i+"' >"+i+"</a> ";
			}
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setReitinguHTML(html);
		projekt.setNimi(nimi);
		projekt.setId(projektID);
		projekt.setKulud(kulud);
		projekt.setTulud(tulud);
		projekt.setKommentaarid(kommentaarid);
		
		m.addAttribute("tootajad", tootajad);
		m.addAttribute("projekt", projekt);
		m.addAttribute("teade", teade);
		m.addAttribute("uusKommentaar", new UusKommentaar());
		m.addAttribute("kustutaKulu", new Kulu());
		m.addAttribute("kustutaTulu", new Tulu());
		m.addAttribute("uusTulu", new Tulu());
		m.addAttribute("uusKulu", new Kulu());
		m.addAttribute("uusProjektiNimi", new UusProjektiNimi(projekt.getNimi(),projekt.getId()));
		m.addAttribute("paneProjektArhiivi", new PaneProjektArhiivi(projekt.getId()));
		
		teade = null;
		
		return "vaadeProjektArhiivisTeine";
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"sonum","projektID"})
	public View addKommentaar1(@ModelAttribute("uusKommentaar") UusKommentaar uusKommentaar, Model m){

		int vastus = Projekt.lisaKommentaarAndmebaasi(uusKommentaar,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kommenteerimine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+uusKommentaar.getProjektID());
	}
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"sonum","projektID"})
	public View addKommentaar2(@ModelAttribute("uusKommentaar") UusKommentaar uusKommentaar, Model m){

		int vastus = Projekt.lisaKommentaarAndmebaasi(uusKommentaar,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kommenteerimine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+uusKommentaar.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"kirjeldus","projektID"})
	public View muudaKirjeldus(@ModelAttribute("uusKirjeldus") UusKirjeldus uusKirjeldus, Model m){
		
		int vastus = Projekt.muudaProjektiKirjeldusAndmebaasis(uusKirjeldus,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kirjelduse muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+uusKirjeldus.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"kasutajaNimi","projektID"})
	public View addKasutaja(@ModelAttribute("uusKasutaja") UusKasutaja uusKasutaja, Model m){
		
		int vastus = Projekt.lisaUusKasutajaAndmebaasi(uusKasutaja,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kasutaja lisamine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+uusKasutaja.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"aeg.time","tuluNimi","summa","stringAeg","projektID"})
	public View addTulu(@ModelAttribute("uusTulu") Tulu tulu, Model m){

		Date uusAeg = Tulu.muudaStringAjaks(tulu.getStringAeg());
		
		if(uusAeg==null){
			teade="Tulu kuupäev peab olema kujul 'päev.kuu.aasta' ning eraldajaks võivad olla .,;-_/";
		}
		else{
			
			tulu.setAeg(uusAeg);
			
			int vastus = Projekt.lisaTuluAndmebaasi(tulu,juhtID);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
				teade = "Viga andmebaasiga ühendumisel";
			}
			else{
				teade = "Tulu lisamine õnnestus";
			}
		}

		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+tulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"aeg.time","kuluNimi","summa","stringAeg","projektID","kasutajaNimi"})
	public View addKulu(@ModelAttribute("uusKulu") Kulu kulu, Model m){

		Date uusAeg = Kulu.muudaStringAjaks(kulu.getStringAeg());
		
		if(uusAeg==null){
			teade="Kulu kuupäev peab olema kujul 'päev.kuu.aasta' ning eraldajaks võivad olla .,;-_/";
		}
		else{
			
			kulu.setAeg(uusAeg);
			
			int vastus = Projekt.lisaKuluAndmebaasi(kulu,juhtID);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
				teade = "Viga andmebaasiga ühendumisel";
			}
			else{
				teade = "Kulu lisamine õnnestus";
			}
		}
		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+kulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"kuluNimi","summa","stringAeg","projektID"})
	public View kustutaKulu(@ModelAttribute("kustutaKulu") Kulu kulu, Model m){
		
		int vastus = Projekt.kustutaKuluAndmebaasist(kulu,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Kulu eemaldamine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+kulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"tuluNimi","summa","stringAeg","projektID"})
	public View kustutaTulu(@ModelAttribute("kustutaTulu") Tulu tulu, Model m){

		int vastus = Projekt.kustutaTuluAndmebaasist(tulu,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Tulu eemaldamine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+tulu.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"kustuta","projektID"})
	public View kustutaProjekt1(@RequestParam("projektID") int id,@RequestParam("kustuta") String käsk, Model m){

		if(käsk.equals("jah")){
			
			int vastus = Projekt.kustutaProjektAndmebaasist(id,juhtID);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
				teade = "Viga andmebaasiga ühendumisel";
				return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+id);
			}
			else{
				//teade = "Projekti kustutamine õnnestus";
				return new RedirectView("vaadeArhiiv.htm");
			}
			
		}
		else{
			return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+id);
		}
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"kustuta","projektID"})
	public View kustutaProjekt2(@RequestParam("projektID") int id,@RequestParam("kustuta") String käsk, Model m){

		if(käsk.equals("jah")){
			
			int vastus = Projekt.kustutaProjektAndmebaasist(id,juhtID);
			
			if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
				teade = "Viga andmebaasiga ühendumisel";
				return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+id);
			}
			else{
				//teade = "Projekti kustutamine õnnestus";
				return new RedirectView("vaadeArhiiv.htm");
			}
			
		}
		else{
			return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+id);
		}
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"uusNimi","projektID"})
	public View muudaProjektiNime1(@ModelAttribute("uusProjektiNimi") UusProjektiNimi nimi, Model m){
		
		int vastus = Projekt.muudaProjektiNimeAndmebaasis(nimi,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti nime muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+nimi.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"uusNimi","projektID"})
	public View muudaProjektiNime2(@ModelAttribute("uusProjektiNimi") UusProjektiNimi nimi, Model m){

		int vastus = Projekt.muudaProjektiNimeAndmebaasis(nimi,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti nime muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+nimi.getProjektID());
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"projektID"})
	public View paneProjektArhiivi(@RequestParam("projektID") int projektID, Model m){
		
		int vastus = Projekt.paneProjektArhiiviAndmebaasis(projektID, juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti arhiveerimine õnnestus";
		}

		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.POST, params={"projektID"})
	public View paneProjektArhiivi2(@RequestParam("projektID") int projektID, Model m){
		
		int vastus = Projekt.paneProjektArhiiviAndmebaasis(projektID, juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti arhiveerimine õnnestus";
		}

		return new RedirectView("vaadeProjektid.htm");
	}

	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"projektID","kasutajaID"})
	public View eemaldaKasutaja(@RequestParam("kasutajaID") int kasutajaID,@RequestParam("projektID") int projektID, Model m){
		
		
		int vastus = Projekt.eemaldaKasutajaProjektistAndmebaasis(kasutajaID,projektID,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja eemaldamine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+projektID);
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"projektID","kasutajaNimi"})
	public void lisaKasutajaProjekti(HttpServletRequest request, HttpServletResponse response,@RequestParam("projektID") int projektID,@RequestParam("kasutajaNimi") String nimi, Model m){
		
		int vastus = Projekt.lisaKasutajaProjektiAndmebaasis(nimi, projektID,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja lisamine õnnestus";
		}

	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"projektID","kasutajad"})
	public void muudaKasutajateAndmeid(HttpServletRequest request, HttpServletResponse response,@RequestParam("kasutajad") String kasutajad, @RequestParam("projektID") int projektID, Model m){
		
		int vastus = Projekt.muudaKasutajateAndmeidProjektigaAndmebaasis(kasutajad, projektID,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötajate andmete muutmine õnnestus";
		}
	
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.GET, params={"reiting","projektID"})
	public View muudaReitingut1(@RequestParam("reiting") int reiting,@RequestParam("projektID") int projektID, Model m){
		
		int vastus = Projekt.muudaProjektiReitingutAndmebaasis(projektID, reiting,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Reitingu muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisEsimene.htm?id="+projektID);
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisTeine.htm", method = RequestMethod.GET, params={"reiting","projektID"})
	public View muudaReitingut2(@RequestParam("reiting") int reiting,@RequestParam("projektID") int projektID, Model m){
		
		int vastus = Projekt.muudaProjektiReitingutAndmebaasis(projektID, reiting,juhtID);
		
		if(vastus == Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Reitingu muutmine õnnestus";
		}

		return new RedirectView("vaadeProjektArhiivisTeine.htm?id="+projektID);
	}
	
	@RequestMapping(value = "/vaadeProjektArhiivisEsimene.htm", method = RequestMethod.POST, params={"teade"})
	public void muudaTeade(HttpServletRequest request, HttpServletResponse response,@RequestParam("teade") String teade, Model m){

		this.teade = teade;
	
	}

}
