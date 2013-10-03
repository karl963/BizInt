package bizint.Controller.vaade;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PipelineController {

	@RequestMapping("/vaadePipeline.htm")
	public ModelAndView vaadePipeline() {
		return new ModelAndView("vaadePipeline", "message", "Paluks kannatust, siia tuleb kunagi pipeline vaade !"); 
	}

	
}
