package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

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
import bizint.app.alam.kuuAndmed.Kuud;
import bizint.app.alam.kuuAndmed.TabeliData;
import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;
import bizint.post.UusKasutaja;
import bizint.post.UusKirjeldus;
import bizint.post.UusKommentaar;
import bizint.post.UusProjektiNimi;

@Controller
public class TootajadController {
	
	private String teade;

	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.GET)
	public String vaadeTootajadTabel(Model m) {
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
	
		Connection con = new Mysql().getConnection();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hetkeAasta = cal.get(Calendar.YEAR);
		
		try{

			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi, kasutajaID FROM kasutajad WHERE töötab=1";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				List<TabeliData> tabeliAndmed = new ArrayList<TabeliData>();
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				int kasutajaID = rs.getInt("kasutajaID");
				
				kasutaja.setKasutajaID(kasutajaID);
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				for(int i = 1; i<=12 ;i++){
					Statement stmt2 = con.createStatement();
					String query2 = "SELECT palk FROM palgad WHERE kasutaja_ID="+kasutajaID+" AND kuu="+i+" AND aasta="+hetkeAasta;
					ResultSet rs2 = stmt2.executeQuery(query2);
					
					int kuuNumber = i;
					Double palk = 0.0;
					
					if(rs2.next()){
						palk = rs2.getDouble("palk");
					}
					
					TabeliData data = new TabeliData();
					data.setPalk(palk);
					data.setKuuNumber(kuuNumber);
					
					tabeliAndmed.add(data);
					
					try{rs2.close();stmt2.close();}catch(Exception x){}
					
				}

				Statement stmt3 = con.createStatement();
				String query3 = "SELECT tulu, osalus, aeg FROM tulud, projektikasutajad WHERE projektikasutajad.kasutaja_ID="+kasutajaID+" AND tulud.projekt_ID=projektikasutajad.projekt_ID";
				ResultSet rs3 = stmt3.executeQuery(query3);
				
				while(rs3.next()){
					
					cal.setTime(new Date(rs3.getTimestamp("aeg").getTime()));
					
					if(cal.get(Calendar.YEAR) == hetkeAasta){

						int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
						
						Double tulu = rs3.getDouble("tulu")*rs3.getDouble("osalus");
						
						tabeliAndmed = lisaTuluTabeliAndmetesse(kuu, tulu,tabeliAndmed);
					}
				}
				
				try{rs3.close();stmt3.close();}catch(Exception x){}
				
				kasutaja.setTabeliAndmed(paneTabelJärjekorda(tabeliAndmed));
				
				kasutajad.add(kasutaja);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		int[] aastad = {hetkeAasta-3,hetkeAasta-2,hetkeAasta-1,hetkeAasta,hetkeAasta+1,hetkeAasta+2,hetkeAasta+3};
		
		m.addAttribute("hetkeAasta", Calendar.getInstance().get(Calendar.YEAR));
		m.addAttribute("aastad", aastad);
		m.addAttribute("kuupaevad",Kuud.KUUD_LYHEND);
		m.addAttribute("kasutajad",kasutajad);
		m.addAttribute("teade", teade);
		m.addAttribute("uusTootaja",new Kasutaja());
		m.addAttribute("kustutaTootaja",new Kasutaja());

		teade = null;
		
		return "vaadeTootajadTabel"; 
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.GET, params = {"aasta"})
	public String vaadeTootajadTabelValitudAasta(@RequestParam("aasta") int aasta, Model m) {

		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		
		Connection con = new Mysql().getConnection();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hetkeAasta = cal.get(Calendar.YEAR);
		
		try{

			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi, kasutajaID FROM kasutajad WHERE töötab=1";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				List<TabeliData> tabeliAndmed = new ArrayList<TabeliData>();
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				int kasutajaID = rs.getInt("kasutajaID");
				
				kasutaja.setKasutajaID(kasutajaID);
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				for(int i = 1; i<=12 ;i++){
					Statement stmt2 = con.createStatement();
					String query2 = "SELECT palk FROM palgad WHERE kasutaja_ID="+kasutajaID+" AND palgad.kuu="+i+" AND aasta="+aasta;
					ResultSet rs2 = stmt2.executeQuery(query2);
					
					int kuuNumber = i;
					Double palk = 0.0;
					
					if(rs2.next()){
						palk = rs2.getDouble("palk");
					}
					
					TabeliData data = new TabeliData();
					data.setPalk(palk);
					data.setKuuNumber(kuuNumber);
					
					tabeliAndmed.add(data);
					
					try{rs2.close();stmt2.close();}catch(Exception x){}
					
				}

				Statement stmt3 = con.createStatement();
				String query3 = "SELECT tulu, osalus, aeg FROM tulud, projektikasutajad WHERE projektikasutajad.kasutaja_ID="+kasutajaID+" AND tulud.projekt_ID=projektikasutajad.projekt_ID";
				ResultSet rs3 = stmt3.executeQuery(query3);
				
				while(rs3.next()){
					
					cal.setTime(new Date(rs3.getTimestamp("aeg").getTime()));
					
					if(cal.get(Calendar.YEAR) == aasta){
						int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
						
						Double tulu = rs3.getDouble("tulu")*rs3.getDouble("osalus");
						
						tabeliAndmed = lisaTuluTabeliAndmetesse(kuu, tulu,tabeliAndmed);
					}
				}
				
				try{rs3.close();stmt3.close();}catch(Exception x){}
				
				kasutaja.setTabeliAndmed(paneTabelJärjekorda(tabeliAndmed));
				
				kasutajad.add(kasutaja);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		int[] aastad = {hetkeAasta-3,hetkeAasta-2,hetkeAasta-1,hetkeAasta,hetkeAasta+1,hetkeAasta+2,hetkeAasta+3};
		
		m.addAttribute("hetkeAasta", aasta);
		m.addAttribute("aastad", aastad);
		m.addAttribute("kuupaevad",Kuud.KUUD_LYHEND);
		m.addAttribute("kasutajad",kasutajad);
		m.addAttribute("teade", teade);
		m.addAttribute("uusTootaja",new Kasutaja());
		m.addAttribute("kustutaTootaja",new Kasutaja());

		teade = null;

		return "vaadeTootajadTabel";

	}
	
	private List<TabeliData> lisaTuluTabeliAndmetesse(int kuu, Double tulu, List<TabeliData> tabeliAndmed){

		for(TabeliData d : tabeliAndmed){
			if(d.getKuuNumber() == kuu){
				d.setTulu(d.getTulu()+tulu);
				break;
			}
		}

		return tabeliAndmed;
	}
	
	private List<TabeliData> paneTabelJärjekorda(List<TabeliData> tabeliAndmed){
		List<TabeliData> uuedAndmed = new ArrayList<TabeliData>();

		int k = 1;
		
		while(tabeliAndmed.size()>0){
			
			for(int i = 0; i<tabeliAndmed.size() ;i++){
				
				if(tabeliAndmed.get(i).getKuuNumber() == k){
					uuedAndmed.add(tabeliAndmed.get(i));
					tabeliAndmed.remove(i);
					break;
				}
			}
			k++;
		}

		return uuedAndmed;
	}

	@RequestMapping(value = "/vaadeTootajadGraaf.htm", method = RequestMethod.GET)
	public String vaadeTootajadGraaf(Model m) {
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		
		Connection con = new Mysql().getConnection();
		
		try{

			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi FROM kasutajad WHERE töötab=1";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				kasutajad.add(kasutaja);
			}

		}catch(Exception x){
			teade = "Viga andmebaasiga";
		}
		

		m.addAttribute("kasutajad",kasutajad);
		m.addAttribute("teade", teade);

		teade = null;
		
		return "vaadeTootajadGraaf"; 
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kasutajaNimi"})
	public View lisaUusTöötaja(@ModelAttribute("uusTootaja") Kasutaja kasutaja, Model m){

		int vastus = Kasutaja.lisaKasutajaAndmebaasi(kasutaja);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else if(vastus == Kasutaja.VIGA_JUBA_EKSISTEERIB){
			teade = "Sellise nimega töötaja juba eksisteeerib";
		}
		else{
			teade = "Töötaja lisamine õnnestus";
		}
		
		return new RedirectView("vaadeTootajadTabel.htm");
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kasutajaID"})
	public View kustutaTöötaja(@ModelAttribute("kustutaTootaja") Kasutaja kasutaja, Model m){

		int vastus = Kasutaja.muudaKasutajaTöötuksAndmebaasis(kasutaja);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja eemaldamine õnnestus";
		}
		
		return new RedirectView("vaadeTootajadTabel.htm");
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"tootajad","aastaNumber"})
	public void salvestaTootajatePalgad(HttpServletRequest request, HttpServletResponse response,@RequestParam("tootajad") String tootajad,@RequestParam("aastaNumber") int aasta){

		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		
		for(String rida : tootajad.split("/")){
			
			List<TabeliData> tabeliAndmed = new ArrayList<TabeliData>();
			Kasutaja k = new Kasutaja();
			k.setKasutajaNimi(rida.split("#")[0].split(";")[0]);
			
			for(String tootaja : rida.split("#")){
				
				TabeliData tabeliData = new TabeliData();
				
				tabeliData.setKuuNumber(Integer.parseInt(tootaja.split(";")[1]));
				tabeliData.setPalk(Double.parseDouble(tootaja.split(";")[2]));
				tabeliData.setAasta(aasta);
				
				tabeliAndmed.add(tabeliData);
				
			}
			
			k.setTabeliAndmed(tabeliAndmed);
			kasutajad.add(k);
			
		}

		int vastus = Kasutaja.muudaKasutajatePalkasidAndmebaasis(kasutajad);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja palkade muutmine õnnestus";
		}

	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kid","uusNimi"})
	public void muudaTöötajaNime(HttpServletRequest request, HttpServletResponse response,@RequestParam("kid") int kasutajaID,@RequestParam("uusNimi") String uusNimi){

		int vastus = Kasutaja.muudaKasutajaNimeAndmebaasis(kasutajaID,uusNimi);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else if(vastus == Kasutaja.VIGA_JUBA_EKSISTEERIB){
			teade  = "Selle nimega töötaja juba eksisteerib";
		}
		else{
			teade = "Töötaja nime muutmine õnnestus";
		}

	}

}
