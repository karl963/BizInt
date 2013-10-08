package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
	private String sõnum;
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.GET)
	public String vaadeProjektid(Model m){
		
		
		/*
		 * STAATILINE KOOD, ilma andmebaasita
		 * staatus(järjekorranr,nimi,projekt(nimi,vastutaja,kogutulu))
		 */
		/*
		staatused = new ArrayList<Staatus>();

		Staatus s1 = new Staatus("Suvalised",0);
		
		Projekt p1 = new Projekt("Veebileht");
		Kasutaja ka1 = new Kasutaja("Mehike");
		ka1.setVastutaja(true);
		p1.setKasutajad(Arrays.asList(ka1));
		//p1.setVastutaja(new Kasutaja("Dolan"));
		List<Tulu> tulud1 = new ArrayList<Tulu>();
		tulud1.add(new Tulu(123.4));
		tulud1.add(new Tulu(234.57));
		p1.setTulud(tulud1);
		
		Projekt p2 = new Projekt("Spring mvc");
		Kasutaja ka2 = new Kasutaja("Kalle");
		ka2.setVastutaja(true);
		p2.setKasutajad(Arrays.asList(ka2));
		//p2.setVastutaja(new Kasutaja("Springer"));
		List<Tulu> tulud2 = new ArrayList<Tulu>();
		tulud2.add(new Tulu(9999.99));
		p1.setTulud(tulud2);
		
		Projekt p3 = new Projekt("Kodukas");
		//p3.setVastutaja(new Kasutaja("Tark mees"));

		List<Tulu> tulud3 = new ArrayList<Tulu>();
		tulud3.add(new Tulu(0.01));
		p1.setTulud(tulud3);
		s1.addProjekt(p1);
		s1.addProjekt(p2);
		s1.addProjekt(p3);
		
		Staatus s2 = new Staatus("Teine staatus",1);
		
		Projekt p11 = new Projekt("Mõttetu projekt");
		//p11.setVastutaja(new Kasutaja("kaval Pea"));
		List<Tulu> tulud11 = new ArrayList<Tulu>();
		tulud11.add(new Tulu(1111.1));
		p11.setTulud(tulud11);
		
		Projekt p21 = new Projekt("Lahe projekt");
		p21.setKasutajad(Arrays.asList(ka2));
		//p21.setVastutaja(new Kasutaja("jobu"));
		List<Tulu> tulud21 = new ArrayList<Tulu>();
		tulud21.add(new Tulu(412.0));
		p11.setTulud(tulud21);
		
		s2.addProjekt(p11);
		s2.addProjekt(p21);
		
		staatused.add(s1);
		staatused.add(s2);
		 */
		
		staatused = new ArrayList<Staatus>();
		
		Connection con = Mysql.connection;;
		
		try{
			
			Statement stmt = con.createStatement();
			
			String query = "SELECT staatusNimi,järjekorraNR,staatusID FROM staatused";
			
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()){
				Staatus staatus = new Staatus();
				List<Projekt> projektid = new ArrayList<Projekt>();
				
				String staatusNimi = rs.getString("staatusNimi");
				int järjekorraNumber = rs.getInt("järjekorraNR");
				int staatusID = rs.getInt("staatusID");
				
				staatus.setJärjekorraNumber(järjekorraNumber);
				staatus.setNimi(staatusNimi);
				staatus.setId(staatusID);
				
				String query2 = "SELECT projektNimi, projektID FROM projektid WHERE staatus_ID="+staatusID;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				while(rs2.next()){
					
					Projekt projekt = new Projekt();
					List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
					List<Tulu> tulud = new ArrayList<Tulu>();
					
					int projektID = rs2.getInt("projektID");
					String projektNimi = rs2.getString("projektNimi");
					
					String query3 = "SELECT kasutajaNimi, vastutaja FROM projektiKasutajad, kasutajad WHERE kasutajaID=kasutaja_ID AND projekt_ID="+projektID;
					Statement stmt3 = con.createStatement();
					ResultSet rs3 = stmt3.executeQuery(query3);
					
					while(rs3.next()){
						Kasutaja kasutaja = new Kasutaja();
						
						String kasutajaNimi = rs3.getString("kasutajaNimi");
						boolean vastutaja = rs3.getBoolean("vastutaja");
						
						kasutaja.setNimi(kasutajaNimi);
						kasutaja.setVastutaja(vastutaja);
						
						kasutajad.add(kasutaja);
					}
					
					String query4 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID;
					Statement stmt4 = con.createStatement();
					ResultSet rs4 = stmt4.executeQuery(query4);
					
					while(rs4.next()){
						Tulu tulu = new Tulu();
						
						Double summa = rs4.getDouble("tulu");
						
						tulu.setSumma(summa);
						
						tulud.add(tulu);
					}
					
					projekt.setNimi(projektNimi);
					projekt.setTulud(tulud);
					projekt.setKasutajad(kasutajad);
					
					projektid.add(projekt);
					
				}
				
				staatus.setProjektid(projektid);
				
				staatused.add(staatus);
			}
			
		}catch(Exception x){
			x.printStackTrace();
		}
		
		
		m.addAttribute("staatused", staatused);
		m.addAttribute("uusStaatus", new Staatus());
		m.addAttribute("uusProjekt", new Projekt());
		m.addAttribute("message", sõnum);
		sõnum = null;
		
		return "vaadeProjektid";
	}

	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"nimi"})
	public View addStaatus(@ModelAttribute("uusStaatus") Staatus staatus, Model m){
		
		int vastus = Staatus.lisaStaatusAndmebaasi(staatus);
		
		if(vastus == Staatus.ERROR_JUBA_EKSISTEERIB){
			sõnum = "Selle nimega staatus juba eksisteerib";
		}
		else if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			sõnum = "Viga andmebaasiga ühendumisel";
		}
		else{
			sõnum = "Staatuse lisamine õnnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	@RequestMapping(value = "/vaadeProjektid.htm", method = RequestMethod.POST, params={"nimi","staatusID"})
	public View addStaatus(@ModelAttribute("uusProjekt") Projekt projekt, int staatusID, Model m){

		int vastus = Projekt.lisaProjektAndmebaasi(projekt,staatusID);
		
		if(vastus == Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			sõnum = "Viga andmebaasiga ühendumisel";
		}
		else{
			sõnum = "Projekti lisamine õnnestus";
		}
		
		return new RedirectView("vaadeProjektid.htm");
	}
	
	//@RequestParam("nimi") String nimi, @RequestParam("nimi2") String nimi2
}
