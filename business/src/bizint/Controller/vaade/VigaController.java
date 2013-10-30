package bizint.Controller.vaade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VigaController {
	
	public static int VIGA_MITTE_LOGITUD = 1,VIGA_PROJEKTI_POLE_OLEMAS = 2;
	private String teade;

	@RequestMapping(value = "/vaadeViga.htm", method = RequestMethod.GET)
	public String vaadeViga(HttpServletRequest request, Model m){
		
		if(Integer.parseInt(String.valueOf(request.getSession().getAttribute("viga"))) == VigaController.VIGA_MITTE_LOGITUD){
			teade =  "Te pole sisse loginud !";
		}
		else if(Integer.parseInt(String.valueOf(request.getSession().getAttribute("viga"))) == VigaController.VIGA_PROJEKTI_POLE_OLEMAS){
			teade =  "Sellise ID-ga projekti pole olemas";
		}

		m.addAttribute("teade",teade);
		
		teade = "Viga lehe laadimisel !";
		
		return "vaadeViga";
	}
	
	
}
