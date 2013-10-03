package bizint.Controller.vaade;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bizint.app.alam.Projekt;

@Controller
public class ProjektController {

	@RequestMapping("/vaadeProjektEsimene.htm")
	public ModelAndView vaadeProjektEsimene() {
		
		Projekt projekt = new Projekt();
		
		return new ModelAndView("vaadeProjektEsimene", "projekt", projekt); 
	}
	
	@RequestMapping("/vaadeProjektTeine.htm")
	public ModelAndView vaadeProjektTeine() {
		
		Projekt projekt = new Projekt();
		
		return new ModelAndView("vaadeProjektTeine", "projekt", projekt); 
	}
	
}
