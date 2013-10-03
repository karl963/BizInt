package bizint.Controller.vaade;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TootajadController {

	@RequestMapping("/vaadeTootajad.htm")
	public ModelAndView vaadeTootajad() {
		return new ModelAndView("vaadeTootajad", "message", "Paluks kannatust, siia tuleb kunagi töötajate vaade !"); 
	}


}
