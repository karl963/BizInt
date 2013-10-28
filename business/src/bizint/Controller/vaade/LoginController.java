package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.andmebaas.Mysql;
import bizint.post.SisseLogija;

@Controller
public class LoginController {
	
	private String teade = "";
	
	@RequestMapping(value = "/vaadeLogin.htm", method = RequestMethod.GET)
	public String vaadeLgin(Model m) {
		
		m.addAttribute("login",new bizint.post.SisseLogija());
		m.addAttribute("teade",teade);
		
		teade = "";
		
		return "vaadeLogin";
	}
	
	@RequestMapping(value = "/vaadeLogin.htm", method = RequestMethod.POST)
	public View logiSisse(HttpServletRequest request, HttpServletResponse response,@ModelAttribute("login") SisseLogija logija, Model m) {
		
		Connection con = (new Mysql()).getConnection();
		if(con==null){
			teade = "Viga andmebaasige ühendusmisel";
		}
		else{
			try{
				
				Statement stmt = con.createStatement();
				String query = "SELECT juhtID FROM juhid WHERE kasutajaNimi='"+logija.getKasutajaNimi()+"' AND parool='"+logija.getParool()+"'";
				ResultSet rs = stmt.executeQuery(query);
				
				if(rs.next()){
					request.getSession().setAttribute("kasutajaNimi", logija.getKasutajaNimi());
					request.getSession().setAttribute("juhtID", rs.getInt("juhtID"));
					teade = "";
				}
				else{
					teade = "Viga kasutajanime või parooliga";
				}
				
			}
			catch(Exception x){
				teade = "Viga andmebaasige ühendusmisel";
			}
		}
		
		return new RedirectView("vaadeLogin.htm");
	}

}
