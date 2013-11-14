package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.Cookie;
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
public class ProjektidController {
	
	private List<Staatus> staatused = new ArrayList<Staatus>();
	private String teade;
	private int juhtID = 0;
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.GET)
	public String vaadeProjektid(HttpServletRequest request,Model m){
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeProjektid.htm");
			return "redirect:/vaadeViga.htm";
		}
		
		if(request.getSession().getAttribute("juhtID") == null){
			juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
			request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
		}
		else{
			juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
		}
		
		staatused = new ArrayList<Staatus>();
		List<String> t��tajad = new ArrayList<String>();
		
		Connection con = new Mysql().getConnection();;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT staatusNimi,j�rjekorraNR,staatusID FROM staatused WHERE juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()){
				Staatus staatus = new Staatus();
				List<Projekt> projektid = new ArrayList<Projekt>();
				
				String staatusNimi = rs.getString("staatusNimi");
				int j�rjekorraNumber = rs.getInt("j�rjekorraNR");
				int staatusID = rs.getInt("staatusID");
				
				staatus.setJ�rjekorraNumber(j�rjekorraNumber);
				staatus.setNimi(staatusNimi);
				staatus.setId(staatusID);
				
				String query2 = "SELECT projektNimi, projektID, projektiJ�rjekorraNR FROM projektid WHERE arhiivis=0 AND staatus_ID="+staatusID+" AND juhtID="+juhtID;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				while(rs2.next()){
					
					Projekt projekt = new Projekt();
					List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
					List<Tulu> tulud = new ArrayList<Tulu>();
					
					int projektID = rs2.getInt("projektID");
					String projektNimi = rs2.getString("projektNimi");
					int projektiJ�rjekorraNumber = rs2.getInt("projektiJ�rjekorraNR");
					
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
					projekt.setProjektiJ�rjekorraNumber(projektiJ�rjekorraNumber);
					
					projektid.add(projekt);
					
				}
				
				try{rs2.close();stmt2.close();}catch(Exception x){}
				
				projektid = paneProjektidJ�rjekorda(projektid);
				staatus.setProjektid(projektid);
				
				staatused.add(staatus);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT kasutajaNimi FROM kasutajad WHERE t��tab=1 AND juhtID="+juhtID;
			ResultSet rs2 = stmt2.executeQuery(query2);
		
			while(rs2.next()){
				t��tajad.add(rs2.getString("kasutajaNimi"));
			}
			try{rs2.close();stmt2.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		staatused = paneJ�rjekorda(staatused);
		
		m.addAttribute("kasutajad",t��tajad);
		m.addAttribute("kasutajanimi",request.getSession().getAttribute("kasutajaNimi"));
		m.addAttribute("staatuseKustutamine", new Staatus());
		m.addAttribute("staatused", staatused);
		m.addAttribute("uusStaatus", new Staatus());
		m.addAttribute("uusProjekt", new Projekt());
		m.addAttribute("staatuseNimeMuutmine", new Staatus());
		m.addAttribute("teade", teade);
		teade = null;
		
		return "vaadeProjektid";
	}
	
	private List<Staatus> paneJ�rjekorda(List<Staatus> staatused){
		List<Staatus> uusList = new ArrayList<Staatus>();
		
		while(staatused.size()>0){
			
			int v�ikseimI = 0;
			int v�ikseim = 99;
			int index = 0;
			
			for(Staatus s : staatused){
				
				if(s.getJ�rjekorraNumber()<v�ikseim){
					v�ikseim = s.getJ�rjekorraNumber();
					v�ikseimI = index;
				}
				
				index++;
			}
			
			uusList.add(staatused.get(v�ikseimI));
			staatused.remove(v�ikseimI);
			
		}
		
		return uusList;
	}
	
	private List<Projekt> paneProjektidJ�rjekorda(List<Projekt> projektid){
		List<Projekt> uusList = new ArrayList<Projekt>();
		
		while(projektid.size()>0){
			
			int v�ikseimI = 0;
			int v�ikseim = 99;
			int index = 0;
			
			for(Projekt p : projektid){
				
				if(p.getProjektiJ�rjekorraNumber()<v�ikseim){
					v�ikseim = p.getProjektiJ�rjekorraNumber();
					v�ikseimI = index;
				}
				
				index++;
			}
			
			uusList.add(projektid.get(v�ikseimI));
			projektid.remove(v�ikseimI);
			
		}
		
		return uusList;
	}
	

	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"nimi"})
	public View addStaatus(@ModelAttribute("uusStaatus") Staatus staatus, Model m){
		
		int vastus = Staatus.lisaStaatusAndmebaasi(staatus,juhtID);
		
		if(vastus == Staatus.ERROR_JUBA_EKSISTEERIB){
			teade = "Selle nimega staatus juba eksisteerib";
		}
		else if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Staatuse lisamine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"nimi","staatusID"})
	public View addProjekt(@ModelAttribute("uusProjekt") Projekt projekt,@RequestParam(value="staatusID", required=true) int staatusID, Model m){

		int vastus = Projekt.lisaProjektAndmebaasi(projekt,staatusID,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else if(vastus == Projekt.PROJEKTI_NIMI_T�HI){
			teade = "Projekti nimi ei tohi olla t�hi!";
		}
		else{
			teade = "Projekti lisamine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"id","kustuta"})
	public View kustutaStaatus(@RequestParam("id") int staatusID, Model m){
		
		int vastus = Staatus.kustutaStaatusAndmebaasist(staatusID,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else if(vastus == Staatus.ERROR_STAATUS_POLE_T�HI){
			teade = "Viga, staatuses on veel kehtivaid projekte";
		}
		else{
			teade = "Staatuse kustutamine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"nimi","id"})
	public View muudaStaatuseNime(@ModelAttribute("staatuseNimeMuutmine") Staatus staatus, Model m){
		
		int vastus = Staatus.muudaStaatuseNimeAndmebaasis(staatus,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else if(vastus == Staatus.STAATUSE_NIMI_T�HI){
			teade = "Staatuse nimi ei tohi olla t�hi!";
		}
		else{
			teade = "Staatuse nime muutmine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.GET, params={"logivalja"})
	public View logiV�lja(HttpServletRequest request, HttpServletResponse response,@RequestParam(value="logivalja", required=true) String v�lja){

		if(v�lja.equals("1")){
			request.getSession().removeAttribute("kasutajaNimi");
			request.getSession().removeAttribute("juhtID");
			request.getSession().removeAttribute("suunatudLink");
			request.getSession().invalidate();
			
			response.addCookie(new Cookie("sid",null));
			
			return new RedirectView("vaadeLogin.htm");
		}
		else{
			return new RedirectView("vaadeProjektid.htm");
		}
	}
	
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"projektDragId","staatusDragId","projektiDragJNR"})
	public View muudaProjektiStaatust(@RequestParam(value="staatusDragId", required=true) int staatusID,@RequestParam(value="projektDragId", required=true) int projektID,
			@RequestParam(value="projektiDragJNR", required=true) int projektiJ�rjekorraNR,@RequestParam(value="staatusVanaDragId", required=true) int staatusVanaID,
			@RequestParam(value="projektiVanaDragJNR", required=true) int projektiVanaJ�rjekorraNR,Model m){

		int vastus = Projekt.muudaProjektiStaatustAndmebaasis(projektID,staatusID,projektiJ�rjekorraNR,staatusVanaID,projektiVanaJ�rjekorraNR,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Projekti staatuse muutmine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"vastutajaProjektid","vastutajaNimi"})
	public View muudaProjektiStaatust(@RequestParam(value="vastutajaNimi", required=true) String nimi,@RequestParam(value="vastutajaProjektid", required=true) int projektID){

		int vastus = Projekt.muudaProjektiVastutajatAndmebaasis(projektID,nimi,juhtID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Vastutaja muutmine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"staatuseSVanaDragId","staatuseSJNR","staatuseSVanaJNR"})
	public View muudaStaatuseJNR(@RequestParam(value="staatuseSJNR", required=true) int staatuseJ�rjekorraNR,@RequestParam(value="staatuseSVanaDragId", required=true) int staatusVanaID,
			@RequestParam(value="staatuseSVanaJNR", required=true) int staatuseVanaJ�rjekorraNR){

		int vastus = Staatus.muudaStaatuseJ�rjekordaAndmebaasis(staatuseJ�rjekorraNR,staatusVanaID,staatuseVanaJ�rjekorraNR,juhtID);
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL){
			teade = "Viga andmebaasiga �hendumisel";
		}
		else{
			teade = "Staatuse j�rjekorra muutmine �nnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
}
