package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.Staatus;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class ArhiivController {
	
	private List<Staatus> staatused = new ArrayList<Staatus>();
	private String teade;
	private int juhtID = 0;
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.GET)
	public String vaadeArhiiv(HttpServletRequest request, Model m){

		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeArhiiv.htm");
			return "redirect:/vaadeViga.htm";
		}
		

		if(request.getSession().getAttribute("juhtID") == null){
			juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
		}
		else{
			juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
		}
		
		staatused = new ArrayList<Staatus>();
		List<String> töötajad = new ArrayList<String>();
		
		Connection con = new Mysql().getConnection();;
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hetkeAasta = cal.get(Calendar.YEAR);
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT staatusNimi,järjekorraNR,staatusID FROM staatused WHERE juhtID=0";
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()){
				Staatus staatus = new Staatus();
				List<Projekt> arhiiv = new ArrayList<Projekt>();
				
				String staatusNimi = rs.getString("staatusNimi");
				int järjekorraNumber = rs.getInt("järjekorraNR");
				int staatusID = rs.getInt("staatusID");
				
				staatus.setJärjekorraNumber(järjekorraNumber);
				staatus.setNimi(staatusNimi);
				staatus.setId(staatusID);
				
				String query2 = "SELECT projektNimi, projektID, projektiJärjekorraNR FROM projektid WHERE aeg='"+hetkeAasta+"' AND arhiivis=1 AND staatus_ID="+staatusID+" AND juhtID="+juhtID;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				while(rs2.next()){
					
					Projekt projekt = new Projekt();
					List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
					List<Tulu> tulud = new ArrayList<Tulu>();
					
					int projektID = rs2.getInt("projektID");
					String projektNimi = rs2.getString("projektNimi");
					int projektiJärjekorraNumber = rs2.getInt("projektiJärjekorraNR");
					
					String query3 = "SELECT kasutajaNimi, vastutaja FROM projektikasutajad, kasutajad WHERE kasutajaID=kasutaja_ID AND projekt_ID="+projektID+" AND projektikasutajad.juhtID="+juhtID+" AND kasutajad.juhtID="+juhtID;
					Statement stmt3 = con.createStatement();
					ResultSet rs3 = stmt3.executeQuery(query3);
					
					while(rs3.next()){
						Kasutaja kasutaja = new Kasutaja();
						
						String kasutajaNimi = rs3.getString("kasutajaNimi");
						boolean vastutaja = rs3.getBoolean("vastutaja");
						
						kasutaja.setKasutajaNimi(kasutajaNimi);
						kasutaja.setVastutaja(vastutaja);
						
						kasutajad.add(kasutaja);
					}
					
					try{rs3.close();stmt3.close();}catch(Exception x){}
					
					String query4 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
					Statement stmt4 = con.createStatement();
					ResultSet rs4 = stmt4.executeQuery(query4);
					
					while(rs4.next()){
						Tulu tulu = new Tulu();
						
						Double summa = rs4.getDouble("tulu");
						
						tulu.setSumma(summa);
						
						tulud.add(tulu);
					}
					
					try{rs4.close();stmt4.close();}catch(Exception x){}
					
					projekt.setNimi(projektNimi);
					projekt.setTulud(tulud);
					projekt.setKasutajad(kasutajad);
					projekt.setId(projektID);
					projekt.setProjektiJärjekorraNumber(projektiJärjekorraNumber);
					
					arhiiv.add(projekt);
					
				}
				
				try{rs2.close();stmt2.close();}catch(Exception x){}
				
				arhiiv = paneProjektidJärjekorda(arhiiv);
				staatus.setProjektid(arhiiv);
				
				staatused.add(staatus);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT kasutajaNimi FROM kasutajad WHERE töötab=1 AND juhtID="+juhtID;
			ResultSet rs2 = stmt2.executeQuery(query2);
		
			while(rs2.next()){
				töötajad.add(rs2.getString("kasutajaNimi"));
			}
			try{rs2.close();stmt2.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		staatused = paneJärjekorda(staatused);
		int[] aastad = {hetkeAasta-3, hetkeAasta-2, hetkeAasta-1, hetkeAasta, hetkeAasta+1, hetkeAasta+2, hetkeAasta+3 };
		
		m.addAttribute("hetkeAasta", Calendar.getInstance().get(Calendar.YEAR));
		m.addAttribute("aastad",aastad);
		
		m.addAttribute("kasutajad",töötajad);
		m.addAttribute("kasutajanimi",request.getSession().getAttribute("kasutajaNimi"));
		m.addAttribute("staatuseKustutamine", new Staatus());
		m.addAttribute("staatused", staatused);
		m.addAttribute("uusStaatus", new Staatus());
		m.addAttribute("uusProjekt", new Projekt());
		m.addAttribute("staatuseNimeMuutmine", new Staatus());
		m.addAttribute("teade", teade);
		teade = null;
		
		return "vaadeArhiiv";
	}
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.GET, params = {"aasta"})
	public String vaadeArhiivValitudAasta(HttpServletRequest request,@RequestParam(value = "aasta", required = true) int aasta, Model m){

		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeArhiiv.htm?aasta="+aasta);
			return "redirect:/vaadeViga.htm";
		}
		

		if(request.getSession().getAttribute("juhtID") == null){
			juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
		}
		else{
			juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
		}

		int hetkeAasta = 0;
		try{
			hetkeAasta = aasta;
		}
		catch(Exception x){
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			hetkeAasta = c.get(Calendar.YEAR);
		}
		
		staatused = new ArrayList<Staatus>();
		List<String> töötajad = new ArrayList<String>();
		
		Connection con = new Mysql().getConnection();;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT staatusNimi,järjekorraNR,staatusID FROM staatused WHERE juhtID=0";
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()){
				Staatus staatus = new Staatus();
				List<Projekt> arhiiv = new ArrayList<Projekt>();
				
				String staatusNimi = rs.getString("staatusNimi");
				int järjekorraNumber = rs.getInt("järjekorraNR");
				int staatusID = rs.getInt("staatusID");
				
				staatus.setJärjekorraNumber(järjekorraNumber);
				staatus.setNimi(staatusNimi);
				staatus.setId(staatusID);
				
				String query2 = "SELECT projektNimi, projektID, projektiJärjekorraNR FROM projektid WHERE aeg ='"+hetkeAasta+"' AND arhiivis=1 AND staatus_ID="+staatusID+" AND juhtID="+juhtID;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				while(rs2.next()){
					
					Projekt projekt = new Projekt();
					List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
					List<Tulu> tulud = new ArrayList<Tulu>();
					
					int projektID = rs2.getInt("projektID");
					String projektNimi = rs2.getString("projektNimi");
					int projektiJärjekorraNumber = rs2.getInt("projektiJärjekorraNR");
					
					String query3 = "SELECT kasutajaNimi, vastutaja FROM projektikasutajad, kasutajad WHERE kasutajaID=kasutaja_ID AND projekt_ID="+projektID+" AND projektikasutajad.juhtID="+juhtID+" AND kasutajad.juhtID="+juhtID;
					Statement stmt3 = con.createStatement();
					ResultSet rs3 = stmt3.executeQuery(query3);
					
					while(rs3.next()){
						Kasutaja kasutaja = new Kasutaja();
						
						String kasutajaNimi = rs3.getString("kasutajaNimi");
						boolean vastutaja = rs3.getBoolean("vastutaja");
						
						kasutaja.setKasutajaNimi(kasutajaNimi);
						kasutaja.setVastutaja(vastutaja);
						
						kasutajad.add(kasutaja);
					}
					
					try{rs3.close();stmt3.close();}catch(Exception x){}
					
					String query4 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
					Statement stmt4 = con.createStatement();
					ResultSet rs4 = stmt4.executeQuery(query4);
					
					while(rs4.next()){
						Tulu tulu = new Tulu();
						
						Double summa = rs4.getDouble("tulu");
						
						tulu.setSumma(summa);
						
						tulud.add(tulu);
					}
					
					try{rs4.close();stmt4.close();}catch(Exception x){}
					
					projekt.setNimi(projektNimi);
					projekt.setTulud(tulud);
					projekt.setKasutajad(kasutajad);
					projekt.setId(projektID);
					projekt.setProjektiJärjekorraNumber(projektiJärjekorraNumber);
					
					arhiiv.add(projekt);
					
				}
				
				try{rs2.close();stmt2.close();}catch(Exception x){}
				
				arhiiv = paneProjektidJärjekorda(arhiiv);
				staatus.setProjektid(arhiiv);
				
				staatused.add(staatus);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT kasutajaNimi FROM kasutajad WHERE töötab=1 AND juhtID="+juhtID;
			ResultSet rs2 = stmt2.executeQuery(query2);
		
			while(rs2.next()){
				töötajad.add(rs2.getString("kasutajaNimi"));
			}
			try{rs2.close();stmt2.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		staatused = paneJärjekorda(staatused);
		int[] aastad = {hetkeAasta-3, hetkeAasta-2, hetkeAasta-1, hetkeAasta, hetkeAasta+1, hetkeAasta+2, hetkeAasta+3 };
		
		m.addAttribute("hetkeAasta", Calendar.getInstance().get(Calendar.YEAR));
		m.addAttribute("hetkeAasta", hetkeAasta);
		m.addAttribute("aastad",aastad);
		
		m.addAttribute("kasutajad",töötajad);
		m.addAttribute("kasutajanimi",request.getSession().getAttribute("kasutajaNimi"));
		m.addAttribute("staatuseKustutamine", new Staatus());
		m.addAttribute("staatused", staatused);
		m.addAttribute("uusStaatus", new Staatus());
		m.addAttribute("uusProjekt", new Projekt());
		m.addAttribute("staatuseNimeMuutmine", new Staatus());
		m.addAttribute("teade", teade);
		teade = null;
		
		return "vaadeArhiiv";
	}
	
	private List<Staatus> paneJärjekorda(List<Staatus> staatused){
		List<Staatus> uusList = new ArrayList<Staatus>();
		
		while(staatused.size()>0){
			
			int väikseimI = 0;
			int väikseim = 99;
			int index = 0;
			
			for(Staatus s : staatused){
				
				if(s.getJärjekorraNumber()<väikseim){
					väikseim = s.getJärjekorraNumber();
					väikseimI = index;
				}
				
				index++;
			}
			
			uusList.add(staatused.get(väikseimI));
			staatused.remove(väikseimI);
			
		}
		
		return uusList;
	}
	
	private List<Projekt> paneProjektidJärjekorda(List<Projekt> arhiiv){
		List<Projekt> uusList = new ArrayList<Projekt>();
		
		while(arhiiv.size()>0){
			
			int väikseimI = 0;
			int väikseim = 99;
			int index = 0;
			
			for(Projekt p : arhiiv){
				
				if(p.getProjektiJärjekorraNumber()<väikseim){
					väikseim = p.getProjektiJärjekorraNumber();
					väikseimI = index;
				}
				
				index++;
			}
			
			uusList.add(arhiiv.get(väikseimI));
			arhiiv.remove(väikseimI);
			
		}
		
		return uusList;
	}
	

	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.POST, params={"nimi"})
	public View addStaatus(@ModelAttribute("uusStaatus") Staatus staatus, Model m){
		
		int vastus = Staatus.lisaStaatusAndmebaasi(staatus,juhtID);
		
		if(vastus == Staatus.ERROR_JUBA_EKSISTEERIB){
			teade = "Selle nimega staatus juba eksisteerib";
		}
		else if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Staatuse lisamine õnnestus";
		}
		
		return new RedirectView("vaadeArhiiv.htm");
	}
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.POST, params={"nimi","staatusID"})
	public View addProjekt(@ModelAttribute("uusProjekt") Projekt projekt,@RequestParam(value="staatusID", required=true) int staatusID, Model m){

		int vastus = Projekt.lisaProjektAndmebaasi(projekt,staatusID,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti lisamine õnnestus";
		}
		
		return new RedirectView("vaadeArhiiv.htm");
	}
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.POST, params={"id","kustuta"})
	public View kustutaStaatus(@RequestParam("id") int staatusID, Model m){
		
		int vastus = Staatus.kustutaStaatusAndmebaasist(staatusID,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else if(vastus == Staatus.ERROR_STAATUS_POLE_TÜHI){
			teade = "Viga, staatuses on veel kehtivaid projekte";
		}
		else{
			teade = "Staatuse kustutamine õnnestus";
		}
		
		return new RedirectView("vaadeArhiiv.htm");
	}
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.POST, params={"nimi","id"})
	public View muudaStaatuseNime(@ModelAttribute("staatuseNimeMuutmine") Staatus staatus, Model m){
		
		int vastus = Staatus.muudaStaatuseNimeAndmebaasis(staatus,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Staatuse nime muutmine õnnestus";
		}
		
		return new RedirectView("vaadeArhiiv.htm");
	}
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.GET, params={"logivalja"})
	public View logiVälja(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="logivalja", required=true) String välja){

		if(välja.equals("1")){
			request.getSession().removeAttribute("kasutajaNimi");
			request.getSession().removeAttribute("juhtID");
			request.getSession().invalidate();
			return new RedirectView("vaadeLogin.htm");
		}
		else{
			return new RedirectView("vaadeArhiiv.htm");
		}
	}
	
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.POST, params={"projektDragId","staatusDragId","projektiDragJNR"})
	public View muudaProjektiStaatustArhiivis(@RequestParam(value="staatusDragId", required=true) int staatusID,@RequestParam(value="projektDragId", required=true) int projektID,
			@RequestParam(value="projektiDragJNR", required=true) int projektiJärjekorraNR,@RequestParam(value="staatusVanaDragId", required=true) int staatusVanaID,
			@RequestParam(value="projektiVanaDragJNR", required=true) int projektiVanaJärjekorraNR,Model m){

		int vastus = Projekt.muudaProjektiStaatustArhiiviAndmebaasis(projektID,staatusID,projektiJärjekorraNR,staatusVanaID,projektiVanaJärjekorraNR,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Projekti staatuse muutmine õnnestus";
		}
		
		return new RedirectView("vaadeArhiiv.htm");
	}
	
	@RequestMapping(value = "/vaadeArhiiv.htm", method = RequestMethod.POST, params={"vastutajaProjektid","vastutajaNimi"})
	public View muudaProjektiStaatust(@RequestParam(value="vastutajaNimi", required=true) String nimi,@RequestParam(value="vastutajaProjektid", required=true) int projektID){

		int vastus = Projekt.muudaProjektiVastutajatAndmebaasis(projektID,nimi,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Vastutaja muutmine õnnestus";
		}
		
		return new RedirectView("vaadeArhiiv.htm");
	}
	
}
