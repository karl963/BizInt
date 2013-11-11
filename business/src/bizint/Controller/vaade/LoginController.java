package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;

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
		String sid = "";
		
		boolean onOlemasLogitud = false;
		
		if(request.getCookies()!=null){
			for(Cookie cookie : request.getCookies()){
				if(cookie.getName().equals("sid")){
					sid = cookie.getValue();
			
					if(sid != null){
						cookie.setMaxAge(10*24*60*60*1000); // 10 päeva
						onOlemasLogitud = true;
						break;
					}
				}
			}
		}
		
		if(onOlemasLogitud){
			Connection con = (new Mysql()).getConnection();
			try{
				Statement stmt0 = con.createStatement();
				String query0 = "SELECT parool FROM juhid, sessioonid WHERE sessioonid.kasutajaNimi=juhid.kasutajaNimi AND session_id ='"+sid+"'";
				ResultSet rs0 = stmt0.executeQuery(query0);
				
				if(rs0.next()){
					parool = rs0.getString("parool");
				}
				else{
					request.getSession().removeAttribute("kasutajaNimi");
					request.getSession().removeAttribute("juhtID");
				}
				
				kasutajaNimi = sid.split("\\.")[2];
				
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
					
					String sid = looUusSessionId(logija.getKasutajaNimi(),logija.getParool());
					// lisame autologiniks cookie
					Cookie c = new Cookie("sid",sid);
					c.setMaxAge(10*24*60*60*1000); // 10 päeva
					response.addCookie(c);
					
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

	private String looUusSessionId(String nimi, String parool){
		
		String tähestik = "abcdefghijklmnopqrstuvwxy1234567890";
		String sid = "";
		
		Random r = new Random();
		
		while(sid.length() < 25){
			
			if(r.nextBoolean()){
				int i = r.nextInt(tähestik.length());
				try{
					sid += String.valueOf(tähestik.charAt(i)).toUpperCase();
				}
				catch(Exception x){
					sid += String.valueOf(tähestik.charAt(i));
				}
			}
			else{
				sid += tähestik.charAt(r.nextInt(tähestik.length()));
			}
			
		}
		
		return lisaSidAndmebaasi(nimi,sid);
	}
	
	private String lisaSidAndmebaasi(String nimi, String sid){
		
		String sidTäiendus = null;
		
		Connection con = null;
		
		try{
			
			con = (new Mysql()).getConnection();
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT juhtID FROM sessioonid, juhid WHERE sessioonid.kasutajaNimi = '"+nimi+"' AND juhid.kasutajaNimi = sessioonid.kasutajaNimi";
			ResultSet rs0 = stmt0.executeQuery(query0);
			
			if(rs0.next()){
				
				sidTäiendus = rs0.getInt("juhtID")+"."+sid+"."+nimi;
				
				Statement stmt = con.createStatement();
				String query = "UPDATE sessioonid SET session_id='"+sidTäiendus+"' WHERE kasutajaNimi='"+nimi+"'";
				stmt.executeUpdate(query);
			}
			else{
				Statement stmt2 = con.createStatement();
				String query2 = "SELECT juhtID FROM juhid WHERE kasutajaNimi = '"+nimi+"'";
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				rs2.next();
				sidTäiendus = rs2.getInt("juhtID")+"."+sid+"."+nimi;
				
				Statement stmt = con.createStatement();
				String query = "INSERT INTO sessioonid (kasutajaNimi, session_id) VALUES ('"+nimi+"','"+sidTäiendus+"')";
				stmt.executeUpdate(query);
			}

		}
		catch(Exception x){
			teade = "Viga kasutajanime või parooliga";
			x.printStackTrace();
		}
		finally{
			if(con!=null){try{con.close();}catch(Exception x){}}
		}
	
		return sidTäiendus;
	
	}
	
	public static String getKasutajaNimiCookiest(Cookie[] cookies){
		if(cookies == null){
			return null;
		}
		
		for(Cookie c : cookies){
			if(c.getName().equals("sid")){
				if(c.getValue() == null || c.getValue().equals("")){
					return null;
				}
				else{
					return c.getValue().split("\\.")[2];
				}
			}
		}
		
		return null;
	}
	
	public static String kontrolliSidOlemasolu(Cookie[] cookies){
		
		if(cookies == null){
			return null;
		}
		
		for(Cookie c : cookies){
			if(c.getName().equals("sid")){
				if(c.getValue() == null || c.getValue().equals("")){
					return null;
				}
				else{
					return c.getValue();
				}
			}
		}
		
		return null;
	}
	
}
