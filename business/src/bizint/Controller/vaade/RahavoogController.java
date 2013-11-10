package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;













import bizint.andmebaas.Mysql;
import bizint.app.alam.Staatus;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class RahavoogController {
	
	private String teade;
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy");
	private int juhtID = 0;
	
	@RequestMapping(value = "/vaadeRahavoog.htm", method = RequestMethod.GET)
	public String vaadeRahavoog(HttpServletRequest request,Model m){//@RequestParam("algus") String algus,@RequestParam("lopp") String lõpp,Model m) {
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeRahavoog.htm");
			return "redirect:/vaadeViga.htm";
		}
		
		if(juhtID == 0){
			if(request.getSession().getAttribute("juhtID") == null){
				juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split(".")[0]);
			}
			else{
				juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
			}
		}
		/*
		Connection con = (new Mysql()).getConnection();
		
		Timestamp algusAeg,lõppAeg;
		
		try{
			algusAeg = new Timestamp(AJAFORMAAT.parse(algus).getTime());
		}catch(Exception x){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)-1);
			
			lõppAeg = new Timestamp(cal.getTime().getTime());
		}
		try{
			lõppAeg = new Timestamp(AJAFORMAAT.parse(lõpp).getTime());
		}catch(Exception x){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR)+1);
			
			lõppAeg = new Timestamp(cal.getTime().getTime());
		}
		
		String andmedString = "";
		
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "";
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				
			}
			
		}
		catch(Exception x){
			teade = "Viga andmebaasiga ühendumisel !";
		}
		finally{
			if(con!=null){try{con.close();}catch(Exception x){}}
		}
		
		*/
		m.addAttribute("tegemisel","Rahavoo vaade on veel arendamisel (tähtaeg 28.november)");
		m.addAttribute("teade",teade);
		//m.addAttribute("andmedString",andmedString);
		
		teade = null;
		return "vaadeRahavoog"; 
	}

}
