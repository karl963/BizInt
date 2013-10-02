package bizint.Controller.vaade;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bizint.app.alam.Staatus;

@Controller
public class ProjektidController {
	
	List<Staatus> staatused;
	
	@RequestMapping("/vaadeProjektid.htm")
	public ModelAndView annaAndmed(){
		
		Staatus s = new Staatus();
		s.setNimi("lahe staatus");
		//s.setProjektid(Arrays.asList(new Projekt("lahe projekt"),new Projekt("lahedam projekt")));

		Staatus s2 = new Staatus();
		s2.setNimi("lahedam staatus");

		Staatus s3 = new Staatus();

		return new ModelAndView("vaadeProjektid", "staatused", Arrays.asList(s,s2,s3));
	}


}
