package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.Staatus;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class PipelineController {
	
	private List<Staatus> staatused = new ArrayList<Staatus>();
	private String teade;

	@RequestMapping(value = "/vaadePipeline.htm", method = RequestMethod.GET)
	public String vaadePipeline(Model m) {
		
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
				
				String query2 = "SELECT projektID FROM projektid WHERE staatus_ID="+staatusID;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				while(rs2.next()){
					
					Projekt projekt = new Projekt();

					List<Tulu> tulud = new ArrayList<Tulu>();
					
					int projektID = rs2.getInt("projektID");
					
					String query4 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID;
					Statement stmt4 = con.createStatement();
					ResultSet rs4 = stmt4.executeQuery(query4);
					
					while(rs4.next()){
						Tulu tulu = new Tulu();
						
						Double summa = rs4.getDouble("tulu");
						
						tulu.setSumma(summa);
						
						tulud.add(tulu);
					}
					
					projekt.setTulud(tulud);
					projekt.setId(projektID);
					
					projektid.add(projekt);
					
				}
				
				staatus.setProjektid(projektid);
				
				staatused.add(staatus);
			}
			
		}catch(Exception x){
			x.printStackTrace();
		}
		
		staatused = Staatus.paneJärjekorda(staatused);
		
		m.addAttribute("staatused", staatused);
		m.addAttribute("teade", teade);
		
		return "vaadePipeline";
	}
	
}
