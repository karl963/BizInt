package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.kuuAndmed.TabeliData;
import bizint.app.alam.rahaline.Kulu;

@Controller
public class TootajadGraafController {
	
	private String teade;
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy");
	private int juhtID = 0;
	
	@RequestMapping(value = "/vaadeTootajadGraaf.htm", method = RequestMethod.GET)
	public String vaadeTootajadGraaf(HttpServletRequest request,@RequestParam(value = "aasta", defaultValue = "puudub") String hetkeAastaString,
			@RequestParam(value = "kvartal", defaultValue = "puudub") String hetkeKvartalString ,Model m){
		
		Connection con = (new Mysql()).getConnection();
		if(con==null){
			teade = "Viga andmebaasige ühendusmisel";
		}
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeTootajadGraaf.htm");
			return "redirect:/vaadeViga.htm";
		}
		
		if(request.getSession().getAttribute("juhtID") == null){
			juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
			request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
		}
		else{
			juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
		}
		
		String[] kvartalid = {"I - esimene kvartal","II - teine kvartal","III - kolmas kvartal","IV - neljas kvartal"};
		
		int hetkeAasta = 0;
		try{
			hetkeAasta = Integer.parseInt(hetkeAastaString);
		}
		catch(Exception x){
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			hetkeAasta = c.get(Calendar.YEAR);
		}
		
		int alguseKuu = 1;int lõpuKuu = 12;
		if(hetkeKvartalString.equals("puudub")){
			Calendar c = Calendar.getInstance();
			c.setTime(new Date());
			
			int hetkeKuu = c.get(Calendar.MONTH)+1;

			if(hetkeKuu >= 1 && hetkeKuu <= 3){
				hetkeKvartalString = "I - esimene kvartal";
				alguseKuu=1;lõpuKuu=3;
			}
			else if(hetkeKuu >= 4 && hetkeKuu <= 6){
				hetkeKvartalString = "II - teine kvartal";
				alguseKuu=4;lõpuKuu=6;
			}
			else if(hetkeKuu >= 7 && hetkeKuu <= 9){
				hetkeKvartalString = "III - kolmas kvartal";
				alguseKuu=7;lõpuKuu=9;
			}
			else if(hetkeKuu >= 10 && hetkeKuu <= 12){
				hetkeKvartalString = "IV - neljas kvartal";
				alguseKuu=10;lõpuKuu=12;
			}
		}
		else{
			if(hetkeKvartalString.equals(kvartalid[0])){
				alguseKuu=1;lõpuKuu=3;
			}
			else if(hetkeKvartalString.equals(kvartalid[1])){
				alguseKuu=4;lõpuKuu=6;
			}
			else if(hetkeKvartalString.equals(kvartalid[2])){
				alguseKuu=7;lõpuKuu=9;
			}
			else if(hetkeKvartalString.equals(kvartalid[3])){
				alguseKuu=10;lõpuKuu=12;
			}
		}
		
		Calendar cal = Calendar.getInstance();
		String töötajateArray = "";
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi, kasutajaID FROM kasutajad WHERE töötab=1"+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				String töötaja = "";
				Double kuupalkSumma = 0.0;
				Double projektiPalkSumma = 0.0;
				Double koguTulu = 0.0;
				
				
				töötaja = rs.getString("kasutajaNimi");
				
				int kasutajaID = rs.getInt("kasutajaID");
				
				Statement stmt2 = con.createStatement();
				String query2 = "SELECT palk FROM palgad WHERE kasutaja_ID="+kasutajaID+" AND kuu >= "+alguseKuu+" AND kuu <= "+lõpuKuu+" AND aasta="+hetkeAasta+" AND juhtID="+juhtID;
				ResultSet rs2 = stmt2.executeQuery(query2);
					
				while(rs2.next()){
					kuupalkSumma += rs2.getDouble("palk");

				}
				try{rs2.close();stmt2.close();}catch(Exception ex){}
				
				Statement stmt3 = con.createStatement();
				String query3 = "SELECT tulu, osalus, aeg FROM tulud, projektikasutajad WHERE projektikasutajad.kasutaja_ID="+kasutajaID+" AND tulud.projekt_ID=projektikasutajad.projekt_ID AND YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(alguseKuu)+" OR MONTH(aeg)="+(alguseKuu+1)+" OR MONTH(aeg)="+(alguseKuu+2)+")"+" AND tulud.juhtID="+juhtID+" AND projektikasutajad.juhtID="+juhtID;
				ResultSet rs3 = stmt3.executeQuery(query3);
				
				while(rs3.next()){
					cal.setTime(new Date(rs3.getTimestamp("aeg").getTime()));

					int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
					
					if(alguseKuu <= kuu && kuu <= lõpuKuu){
						Double tulu = rs3.getDouble("tulu")*rs3.getDouble("osalus");
						koguTulu += tulu;
					}
				}
				
				try{rs3.close();stmt3.close();}catch(Exception ex){}
				
				Statement stmt4 = con.createStatement();
				String query4 = "SELECT kulu, aeg FROM kulud WHERE kulud.kuluNimi LIKE '%"+töötaja+"%' AND YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(alguseKuu)+" OR MONTH(aeg)="+(alguseKuu+1)+" OR MONTH(aeg)="+(alguseKuu+2)+")"+" AND kulud.juhtID="+juhtID+" AND kasPalk=1";
				ResultSet rs4 = stmt4.executeQuery(query4);

				while(rs4.next()){
					
					cal.setTime(new Date(rs4.getTimestamp("aeg").getTime()));

					int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
					
					if(alguseKuu <= kuu && kuu <= lõpuKuu){
						Double kulu = rs4.getDouble("kulu");
						projektiPalkSumma += kulu;

					}

				}
				
				try{rs4.close();stmt4.close();}catch(Exception ex){}
				
				töötajateArray += töötaja +";"+koguTulu+";"+kuupalkSumma+";"+projektiPalkSumma +"/";
			}
			
		}
		catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga ühendumisel !";
		}
		finally{
			if(con!=null){try{con.close();}catch(Exception x){}}
		}

		int[] aastad = {hetkeAasta-3,hetkeAasta-2,hetkeAasta-1,hetkeAasta,hetkeAasta+1,hetkeAasta+2,hetkeAasta+3};
		
		m.addAttribute("hetkeAasta", hetkeAasta);
		m.addAttribute("aastad", aastad);
		
		m.addAttribute("hetkeKvartal",hetkeKvartalString);
		m.addAttribute("kvartalid",kvartalid);

		m.addAttribute("teade",teade);
		m.addAttribute("andmedString",töötajateArray);
		
		teade = null;
		return "vaadeTootajadGraaf"; 
	}
	
	

}
