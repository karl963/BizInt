package bizint.Controller.vaade;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.post.SisseLogija;

@Controller
public class LoginController {
	
	private String teade = "parool ja kasutajanimi peavad olema samad";
	
	@RequestMapping(value = "/vaadeLogin.htm", method = RequestMethod.GET)
	public String vaadeLgin(Model m) {
		
		m.addAttribute("login",new bizint.post.SisseLogija());
		m.addAttribute("teade",teade);
		
		teade = "parool ja kasutajanimi peavad olema samad";
		
		return "vaadeLogin";
	}
	
	@RequestMapping(value = "/vaadeLogin.htm", method = RequestMethod.POST)
	public View logiSisse(HttpServletRequest request, HttpServletResponse response,@ModelAttribute("login") SisseLogija logija, Model m) {
		
		if(logija.getKasutajaNimi().equals(logija.getParool())){
			request.getSession().setAttribute("kasutajaNimi", logija.getKasutajaNimi());
			return new RedirectView("vaadeProjektid.htm");
		}
		else{
			teade = "Parool või kasutajanimi on vale!";
			return new RedirectView("vaadeLogin.htm");
		}
	}

}
