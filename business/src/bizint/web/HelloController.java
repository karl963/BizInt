package bizint.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/esimene.htm")
	public ModelAndView esimene() {
		return new ModelAndView("esimene", "message", "11111"); 
	}
	
	@RequestMapping(value = "/teine.htm")
	public ModelAndView teine() {
		return new ModelAndView("teine", "message", "2222"); 
	}
}