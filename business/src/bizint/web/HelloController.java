package bizint.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {
	
	@RequestMapping(value = "/vaadeProjektid.htm")
	public ModelAndView vaadeProjektid() {
		return new ModelAndView("vaadeProjektid", "message", "11111"); 
	}
	
	@RequestMapping(value = "/vaadePipeline.htm")
	public ModelAndView vaadePipeline() {
		return new ModelAndView("vaadePipeline", "message", "2222"); 
	}
	
	@RequestMapping(value = "/vaadeTootajad.htm")
	public ModelAndView vaadeTootajad() {
		return new ModelAndView("vaadeTootajad", "message", "3333"); 
	}
	
	@RequestMapping(value = "/vaadeRahavoog.htm")
	public ModelAndView vaadeRahavoog() {
		return new ModelAndView("vaadeRahavoog", "message", "4444"); 
	}
}