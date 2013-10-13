package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
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
		
		Connection con = Mysql.connection;;
		
		try{

			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi, kasutajaID FROM kasutajad";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				int kasutajaID = rs.getInt("kasutajaID");
				
				kasutaja.setKasutajaID(kasutajaID);
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				kasutajad.add(kasutaja);
			}

		}catch(Exception x){
			teade = "Viga andmebaasiga";
		}
		

		m.addAttribute("kasutajad",kasutajad);
		m.addAttribute("teade", teade);
		m.addAttribute("uusTootaja",new Kasutaja());
		m.addAttribute("kustutaTootaja",new Kasutaja());

		teade = null;
		
		return "vaadeTootajadTabel"; 
	}
	
	@RequestMapping(value = "/vaadeTootajadGraaf.htm", method = RequestMethod.GET)
	public String vaadeTootajadGraaf(Model m) {
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		
		Connection con = Mysql.connection;;
		
		try{

			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi FROM kasutajad";
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
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kasutajaID",})
	public View kustutaTöötaja(@ModelAttribute("kustutaTootaja") Kasutaja kasutaja, Model m){
		
		int vastus = Kasutaja.kustutaKasutajaAndmebaasist(kasutaja);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja eemaldamine õnnestus";
		}
		
		return new RedirectView("vaadeTootajadTabel.htm");
	}

}
