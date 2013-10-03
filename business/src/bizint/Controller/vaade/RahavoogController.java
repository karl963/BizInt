package bizint.Controller.vaade;

import java.util.Arrays;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bizint.app.alam.Staatus;

@Controller
public class RahavoogController {

	@RequestMapping("/vaadeRahavoog.htm")
	public ModelAndView vaadeRahavoog() {
		return new ModelAndView("vaadeRahavoog", "message", "Paluks kannatust, siia tuleb kunagi rahavoo vaade !"); 
	}

}
