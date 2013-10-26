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
	
	@RequestMapping(value = "/vaadeRahavoog.htm", method = RequestMethod.GET)
	public String vaadeRahavoog(HttpServletRequest request,@RequestParam("algus") String algus,@RequestParam("lopp") String lõpp,Model m) {
		
		if(request.getSession().getAttribute("kasutajaNimi") == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			return "redirect:/vaadeViga.htm";
		}
		
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
		
		

		m.addAttribute("teade",teade);
		m.addAttribute("andmedString",andmedString);
		
		teade = null;
		return "vaadeRahavoog"; 
	}

}
