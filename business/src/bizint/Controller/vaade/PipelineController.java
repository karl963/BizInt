package bizint.Controller.vaade;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bizint.app.alam.Projekt;
import bizint.app.alam.Staatus;

@Controller
public class PipelineController {

	@RequestMapping("/vaadePipeline.htm")
	public ModelAndView vaadePipeline() {
		
		//List<Staatus>staatused = getStaatusedAndmebaasist();
		List<Staatus> staatused = new ArrayList<Staatus>();
		return new ModelAndView("vaadePipeline", "staatused", staatused); 
	}
/*
	private List<Staatus> getStaatusedAndmebaasist(){
		
		List<Staatus> staatused = new ArrayList<Staatus>();
		
		Statement statement = conn.createStatement();
		ResultSet rs = statement.executeQuery(query);
		
		while(rs.next()){
			List<Projekt> projektid = new ArrayList<Projekt>();
			Staatus staatus = new Staatus();
			
			String nimi = rs.getString("nimi");
			int järjekorraNR = rs.getInt("järjekorraNR");
			
			while(rs2.next()){
				Projekt p = new Projekt();
				
				while(){
					
				}
				
				p.setTulud(tulud);
				
				projektid.add(projekt);
			}
			
			staatus.setProjektid(projektid);
			staatus.setNimi(nimi);
			staatus.setJärjekorraNumber(järjekorraNR)
			staatused.add(staatus);
		}
		
	}
	*/
}
