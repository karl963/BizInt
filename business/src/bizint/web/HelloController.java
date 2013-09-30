package bizint.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HelloController {

    @RequestMapping("/welcome")
    public ModelAndView helloWorld() {
 
        String message = "<br><div align='center'>" + "<h1>Hello World, Spring 3.2.1 Example by Crunchify.com<h1> <br>";
        message += "<a href='http://crunchify.com/category/java-web-development-tutorial/'>More Examples</a>";
        return new ModelAndView("welcome", "message", message);
    }
}