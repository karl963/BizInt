package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.Staatus;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class PipelineController {
	
	private List<Staatus> staatused = new ArrayList<Staatus>();
	private String teade = null;
	private int juhtID = 0;

	@RequestMapping(value = "/vaadePipeline.htm", method = RequestMethod.GET)
	public String vaadePipeline(HttpServletRequest request,Model m) {
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadePipeline.htm");
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
		
		staatused = new ArrayList<Staatus>();
		
		Connection con = new Mysql().getConnection();
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT staatusNimi,järjekorraNR,staatusID FROM staatused WHERE juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
		
			while(rs.next()){
				Staatus staatus = new Staatus();
				List<Projekt> projektid = new ArrayList<Projekt>();
				
				String staatusNimi = rs.getString("staatusNimi");
				int järjekorraNumber = rs.getInt("järjekorraNR");
				int staatusID = rs.getInt("staatusID");
				
				staatus.setJärjekorraNumber(järjekorraNumber);
				staatus.setNimi(staatusNimi);
				staatus.setId(staatusID);
				
				String query2 = "SELECT projektID FROM projektid WHERE staatus_ID="+staatusID+" AND juhtID="+juhtID;
				Statement stmt2 = con.createStatement();
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				while(rs2.next()){
					
					Projekt projekt = new Projekt();

					List<Tulu> tulud = new ArrayList<Tulu>();
					
					int projektID = rs2.getInt("projektID");
					
					String query4 = "SELECT tulu FROM tulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;;
					Statement stmt4 = con.createStatement();
					ResultSet rs4 = stmt4.executeQuery(query4);
					
					while(rs4.next()){
						Tulu tulu = new Tulu();
						
						Double summa = rs4.getDouble("tulu");
						
						tulu.setSumma(summa);
						
						tulud.add(tulu);
					}
					
					try{rs4.close();stmt4.close();}catch(Exception x){}
					
					List<Kulu> kulud = new ArrayList<Kulu>();
					
					String query5 = "SELECT kulu FROM kulud WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;;
					Statement stmt5 = con.createStatement();
					ResultSet rs5 = stmt5.executeQuery(query5);
					
					while(rs5.next()){
						Kulu kulu = new Kulu();
						
						Double summa = rs5.getDouble("kulu");
						
						kulu.setSumma(summa);
						
						kulud.add(kulu);
					}
					
					projekt.setTulud(tulud);
					projekt.setKulud(kulud);
					projekt.setId(projektID);
					
					projektid.add(projekt);
					
					try{rs5.close();stmt5.close();}catch(Exception x){}
					
				}

				staatus.setProjektid(projektid);
				
				staatused.add(staatus);
				
				try{rs2.close();stmt2.close();}catch(Exception x){}
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		staatused = Staatus.paneJärjekorda(staatused);
		String staatusteArray = "";
		for(int i = 0; i < staatused.size(); i++){
			staatusteArray += staatused.get(i).getNimi() + ";" + staatused.get(i).getKogutulu() + ";" + staatused.get(i).getKogukulu() + ";" + staatused.get(i).getBilanss() + "/";
		}
		
		m.addAttribute("staatused",staatusteArray);
		m.addAttribute("teade", teade);
		
		teade = null;
		return "vaadePipeline";
	}
	
}
