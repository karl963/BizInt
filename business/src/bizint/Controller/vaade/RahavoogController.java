package bizint.Controller.vaade;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.app.alam.Staatus;

@Controller
public class RahavoogController {

	@RequestMapping("/vaadeRahavoog.htm")
	public String vaadeRahavoog(HttpServletRequest request,Model m) {
		
		if(request.getSession().getAttribute("kasutajaNimi") == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			return "redirect:/vaadeViga.htm";
		}
		
		m.addAttribute("teade","Paluks kannatust, siia tuleb kunagi rahavoo vaade !");
		
		return "vaadeRahavoog"; 
	}

}
