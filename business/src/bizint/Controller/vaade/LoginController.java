package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.http.Cookie;
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
	public String vaadeLgin(HttpServletRequest request,Model m) {
		
		String kasutajaNimi = "";
		String parool = "";
		boolean onOlemasLogitud = false;
		
		if(request.getCookies()!=null){
			for(Cookie cookie : request.getCookies()){
				if(cookie.getName().equals("user")){
					onOlemasLogitud = true;
					kasutajaNimi = cookie.getValue();
				}
				else if(cookie.getName().equals("parool")){
					parool = cookie.getValue();
				}
			}
		}
		
		if(onOlemasLogitud){
			Connection con = (new Mysql()).getConnection();
			try{
				
				Statement stmt = con.createStatement();
				String query = "SELECT juhtID FROM juhid WHERE kasutajaNimi='"+kasutajaNimi+"' AND parool='"+parool+"'";
				ResultSet rs = stmt.executeQuery(query);
				
				if(rs.next()){
					request.getSession().setAttribute("kasutajaNimi", kasutajaNimi);
					request.getSession().setAttribute("juhtID", rs.getInt("juhtID"));
					teade = "";

					return "redirect:/vaadeProjektid.htm";
				}
				
			}
			catch(Exception x){
				x.printStackTrace();
			}
			finally{
				if(con!=null){try{con.close();}catch(Exception x){}}
			}
		}
		
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
					
					// lisame autologiniks cookied
					response.addCookie(new Cookie("user",logija.getKasutajaNimi()));
					response.addCookie(new Cookie("parool",logija.getParool()));

					
				}
				else{
					teade = "Viga kasutajanime või parooliga";
				}
				
			}
			catch(Exception x){
				x.printStackTrace();
				teade = "Viga andmebaasige ühendusmisel";
			}
			finally{
				if(con!=null){try{con.close();}catch(Exception x){}}
			}
		}
		
		
		return new RedirectView("vaadeLogin.htm");
	}

}
