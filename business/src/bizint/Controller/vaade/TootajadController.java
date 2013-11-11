package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.kuuAndmed.Kuud;
import bizint.app.alam.kuuAndmed.TabeliData;
import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;
import bizint.post.UusKasutaja;
import bizint.post.UusKirjeldus;
import bizint.post.UusKommentaar;
import bizint.post.UusProjektiNimi;

@Controller
public class TootajadController {
	
	private String teade = null;
	private int esimeseKuuTulusid = 0,teiseKuuTulusid = 0,kolmandaKuuTulusid = 0;
	private int esimeseKuuKulusid = 0,teiseKuuKulusid = 0,kolmandaKuuKulusid = 0;
	private int juhtID = 0;
	
	private String[] kvartalid = {"I - esimene kvartal","II - teine kvartal","III - kolmas kvartal","IV - neljas kvartal"};

	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.GET)
	public String vaadeTootajadTabel(HttpServletRequest request,Model m) {
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeTootajadTabel.htm");
			return "redirect:/vaadeViga.htm";
		}
		
		if(juhtID == 0){
			if(request.getSession().getAttribute("juhtID") == null){
				juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
				request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
			}
			else{
				juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
			}
		}
		
		//// kvartalid 
		
		String hetkeKvartalNimi = "";
		int hetkeKvartalNumber = 0;int algusKvartal = 0; int lõppKvartal = 0;
		int hetkeKuu = Calendar.getInstance().get(Calendar.MONTH) +1;
		
		if(1 <= hetkeKuu && hetkeKuu <= 3){
			hetkeKvartalNimi = kvartalid[0];
			hetkeKvartalNumber = 0;
			algusKvartal = 1; lõppKvartal = 3;
		}
		else if(4 <= hetkeKuu && hetkeKuu <= 6){
			hetkeKvartalNimi = kvartalid[1];
			hetkeKvartalNumber = 1;
			algusKvartal = 4;lõppKvartal = 6;
		}
		else if(7 <= hetkeKuu && hetkeKuu <= 9){
			hetkeKvartalNimi = kvartalid[2];
			hetkeKvartalNumber = 2;
			algusKvartal = 7;lõppKvartal = 9;
		}
		else{
			hetkeKvartalNimi = kvartalid[3];
			hetkeKvartalNumber = 3;
			algusKvartal = 10;lõppKvartal = 12;
		}
		
		//////
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
	
		Connection con = new Mysql().getConnection();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hetkeAasta = cal.get(Calendar.YEAR);
		
		int palgaKuupaev1 = 1;int palgaKuupaev2 = 1;int palgaKuupaev3 = 1;
		
		List<String> kuupaevad1 = new ArrayList<String>();
		List<String> kuupaevad2 = new ArrayList<String>();
		List<String> kuupaevad3 = new ArrayList<String>();
		
		List<String> kuupaevadKulud1 = new ArrayList<String>();
		List<String> kuupaevadKulud2 = new ArrayList<String>();
		List<String> kuupaevadKulud3 = new ArrayList<String>();
		try{
			
			Map<Long,String> kuupäevadMap = new HashMap<Long,String>();
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT aeg FROM tulud, projektikasutajad,kasutajad WHERE töötab=1 AND YEAR(aeg)="+hetkeAasta+" AND projektikasutajad.kasutaja_ID=kasutajad.kasutajaID AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND tulud.juhtID="+juhtID+" AND projektikasutajad.juhtID="+juhtID+" AND kasutajad.juhtID="+juhtID;
			ResultSet rs0 = stmt0.executeQuery(query0);
			
			while(rs0.next()){
				
				long aeg = rs0.getTimestamp("aeg").getTime();
				
				if(kuupäevadMap.get(aeg) == null){
					kuupäevadMap.put(aeg, TabeliData.AJAFORMAAT.format(aeg));
					
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(new Date(aeg));
					int päev = cal1.get(Calendar.DAY_OF_MONTH);
					if(cal1.get(Calendar.MONTH)+1 == 1 || cal1.get(Calendar.MONTH)+1 == 4 || cal1.get(Calendar.MONTH)+1 == 7 || cal1.get(Calendar.MONTH)+1 == 10){
						kuupaevad1.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 2 || cal1.get(Calendar.MONTH)+1 == 5 || cal1.get(Calendar.MONTH)+1 == 8 || cal1.get(Calendar.MONTH)+1 == 11){
						kuupaevad2.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 3 || cal1.get(Calendar.MONTH)+1 == 6 || cal1.get(Calendar.MONTH)+1 == 9 || cal1.get(Calendar.MONTH)+1 == 12){
						kuupaevad3.add(päev+"");
					}
				}
			}
			try{rs0.close();stmt0.close();}catch(Exception ex){}
			
			Map<Long,String> kuupäevadKuludMap = new HashMap<Long,String>();
			
			Statement stmt01 = con.createStatement();
			String query01 = "SELECT aeg FROM kulud WHERE YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND kulud.juhtID="+juhtID+" AND kasPalk=1";
			ResultSet rs01 = stmt01.executeQuery(query01);
			
			while(rs01.next()){
				
				long aeg = rs01.getTimestamp("aeg").getTime();
				
				if(kuupäevadMap.get(aeg) == null){
					kuupäevadKuludMap.put(aeg, TabeliData.AJAFORMAAT.format(aeg));
					
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(new Date(aeg));
					int päev = cal1.get(Calendar.DAY_OF_MONTH);
					if(cal1.get(Calendar.MONTH)+1 == 1 || cal1.get(Calendar.MONTH)+1 == 4 || cal1.get(Calendar.MONTH)+1 == 7 || cal1.get(Calendar.MONTH)+1 == 10){
						kuupaevadKulud1.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 2 || cal1.get(Calendar.MONTH)+1 == 5 || cal1.get(Calendar.MONTH)+1 == 8 || cal1.get(Calendar.MONTH)+1 == 11){
						kuupaevadKulud2.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 3 || cal1.get(Calendar.MONTH)+1 == 6 || cal1.get(Calendar.MONTH)+1 == 9 || cal1.get(Calendar.MONTH)+1 == 12){
						kuupaevadKulud3.add(päev+"");
					}
				}
			}
			try{rs01.close();stmt01.close();}catch(Exception ex){}
			
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi, kasutajaID FROM kasutajad WHERE töötab=1"+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Kasutaja kasutaja = new Kasutaja();
				TabeliData data = new TabeliData();
				
				data = lisaKuudJärjekorrasMapi(data,kuupäevadMap,kuupäevadKuludMap);
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				int kasutajaID = rs.getInt("kasutajaID");
				
				kasutaja.setKasutajaID(kasutajaID);
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				int x = 0;
				
				for(int i = algusKvartal; i <= lõppKvartal ;i++){
					Statement stmt2 = con.createStatement();
					String query2 = "SELECT palk, päev FROM palgad WHERE kasutaja_ID="+kasutajaID+" AND kuu="+i+" AND aasta="+hetkeAasta+" AND juhtID="+juhtID;
					ResultSet rs2 = stmt2.executeQuery(query2);
					
					Double palk = 0.0;
					
					if(rs2.next()){
						palk = rs2.getDouble("palk");
						
						if(i == algusKvartal){
							palgaKuupaev1 = rs2.getInt("päev");
						}
						else if(i == lõppKvartal){
							palgaKuupaev3 = rs2.getInt("päev");
						}
						else{
							palgaKuupaev2 = rs2.getInt("päev");
						}
					}
					
					data.getPalgad()[x] = palk;
					
					try{rs2.close();stmt2.close();}catch(Exception ex){}
					
					x++;
				}

				Statement stmt3 = con.createStatement();
				String query3 = "SELECT tulu, osalus, aeg FROM tulud, projektikasutajad WHERE projektikasutajad.kasutaja_ID="+kasutajaID+" AND tulud.projekt_ID=projektikasutajad.projekt_ID AND YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND tulud.juhtID="+juhtID+" AND projektikasutajad.juhtID="+juhtID;
				ResultSet rs3 = stmt3.executeQuery(query3);
				
				while(rs3.next()){
					cal.setTime(new Date(rs3.getTimestamp("aeg").getTime()));

					int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
					String päev = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					
					if(algusKvartal <= kuu && kuu <= lõppKvartal){
						Double tulu = rs3.getDouble("tulu")*rs3.getDouble("osalus");

						data = lisaTuluTabeliAndmetesse(kuu,päev, tulu,data);
					}
					else{

						data = lisaTuluTabeliAndmetesse(kuu,päev, 0.0,data);
					}
				}
				
				try{rs3.close();stmt3.close();}catch(Exception ex){}
				
				Statement stmt4 = con.createStatement();
				String query4 = "SELECT kulu, aeg FROM kulud WHERE kulud.kuluNimi LIKE '%"+kasutajaNimi+"%' AND YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND kulud.juhtID="+juhtID+" AND kulud.kasPalk=1";
				ResultSet rs4 = stmt4.executeQuery(query4);

				while(rs4.next()){
					
					cal.setTime(new Date(rs4.getTimestamp("aeg").getTime()));

					int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
					String päev = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					
					if(algusKvartal <= kuu && kuu <= lõppKvartal){
						Double kulu = rs4.getDouble("kulu");

						data = lisaKuluTabeliAndmetesse(kuu,päev, kulu,data);
					}
					else{
						data = lisaKuluTabeliAndmetesse(kuu,päev, 0.0,data);
					}

				}
				
				try{rs4.close();stmt4.close();}catch(Exception ex){}
				
				data.lisaTuludMapistListi();
				
				data.setPalgaKuupäevad(new int[]{palgaKuupaev1,palgaKuupaev2,palgaKuupaev3});
				kasutaja.setTabeliAndmed(data);
				
				kasutajad.add(kasutaja);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		int[] aastad = {hetkeAasta-3,hetkeAasta-2,hetkeAasta-1,hetkeAasta,hetkeAasta+1,hetkeAasta+2,hetkeAasta+3};
		
		////
		
		m.addAttribute("hetkeAasta", Calendar.getInstance().get(Calendar.YEAR));
		m.addAttribute("hetkeKvartal",hetkeKvartalNimi);
		m.addAttribute("aastad", aastad);
		m.addAttribute("kvartalid", kvartalid);
		
		m.addAttribute("kasutajad",kasutajad);
		
		m.addAttribute("kuupaevad1",kuupaevad1);
		m.addAttribute("kuupaevad2",kuupaevad2);
		m.addAttribute("kuupaevad3",kuupaevad3);
		
		m.addAttribute("kuupaevProjektiPalgad1",kuupaevadKulud1);
		m.addAttribute("kuupaevProjektiPalgad2",kuupaevadKulud2);
		m.addAttribute("kuupaevProjektiPalgad3",kuupaevadKulud3);
		
		m.addAttribute("palgaKuupaev1",palgaKuupaev1);
		m.addAttribute("palgaKuupaev2",palgaKuupaev2);
		m.addAttribute("palgaKuupaev3",palgaKuupaev3);

		m.addAttribute("esimeneKuu",Kuud.getKuudKvartalis(hetkeKvartalNumber).get(0));
		m.addAttribute("teineKuu",Kuud.getKuudKvartalis(hetkeKvartalNumber).get(1));
		m.addAttribute("kolmasKuu",Kuud.getKuudKvartalis(hetkeKvartalNumber).get(2));
		
		m.addAttribute("esimeneKuuNumber",esimeseKuuTulusid+1+esimeseKuuKulusid);
		m.addAttribute("teineKuuNumber",teiseKuuTulusid+1+teiseKuuKulusid);
		m.addAttribute("kolmasKuuNumber",kolmandaKuuTulusid+1+kolmandaKuuKulusid);
		m.addAttribute("kogupikkus",esimeseKuuTulusid+teiseKuuTulusid+kolmandaKuuTulusid+3+esimeseKuuKulusid+teiseKuuKulusid+kolmandaKuuKulusid);
		
		m.addAttribute("teade", teade);
		
		m.addAttribute("uusTootaja",new Kasutaja());
		m.addAttribute("kustutaTootaja",new Kasutaja());

		teade = null;
		
		return "vaadeTootajadTabel"; 
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.GET, params = {"aasta","kvartal"})
	public String vaadeTootajadTabelValitudAasta(HttpServletRequest request,@RequestParam(value = "aasta", required = true) int aasta,@RequestParam(value= "kvartal", required = true) String hetkeKvartal, Model m) {

		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeTootajadTabel.htm?aasta="+aasta+"&kvartal="+hetkeKvartal);
			return "redirect:/vaadeViga.htm";
		}
		
		if(juhtID == 0){
			if(request.getSession().getAttribute("juhtID") == null){
				juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
				request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
			}
			else{
				juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
			}
		}
		
		//// kvartalid 
		
		String hetkeKvartalNimi = hetkeKvartal;
		int hetkeKvartalNumber = 1;int algusKvartal = 1; int lõppKvartal = 1;
		
		if(hetkeKvartal.equals(kvartalid[0])){
			hetkeKvartalNumber = 0;
			algusKvartal = 1; lõppKvartal = 3;
		}
		else if(hetkeKvartal.equals(kvartalid[1])){
			hetkeKvartalNumber = 1;
			algusKvartal = 4;lõppKvartal = 6;
		}
		else if(hetkeKvartal.equals(kvartalid[2])){
			hetkeKvartalNumber = 2;
			algusKvartal = 7;lõppKvartal = 9;
		}
		else{
			hetkeKvartalNumber = 3;
			algusKvartal = 10;lõppKvartal = 12;
		}
		
		//////
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
	
		Connection con = new Mysql().getConnection();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hetkeAasta = aasta;
		
		int palgaKuupaev1 = 1;int palgaKuupaev2 = 1;int palgaKuupaev3 = 1;
		
		List<String> kuupaevad1 = new ArrayList<String>();
		List<String> kuupaevad2 = new ArrayList<String>();
		List<String> kuupaevad3 = new ArrayList<String>();
		
		List<String> kuupaevadKulud1 = new ArrayList<String>();
		List<String> kuupaevadKulud2 = new ArrayList<String>();
		List<String> kuupaevadKulud3 = new ArrayList<String>();
		try{
			
			Map<Long,String> kuupäevadMap = new HashMap<Long,String>();
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT aeg FROM tulud, projektikasutajad,kasutajad WHERE töötab=1 AND YEAR(aeg)="+hetkeAasta+" AND projektikasutajad.kasutaja_ID=kasutajad.kasutajaID AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND tulud.juhtID="+juhtID+" AND projektikasutajad.juhtID="+juhtID+" AND kasutajad.juhtID="+juhtID;
			ResultSet rs0 = stmt0.executeQuery(query0);
			
			while(rs0.next()){
				
				long aeg = rs0.getTimestamp("aeg").getTime();

				if(kuupäevadMap.get(aeg) == null){
					kuupäevadMap.put(aeg, TabeliData.AJAFORMAAT.format(aeg));

					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(new Date(aeg));
					int päev = cal1.get(Calendar.DAY_OF_MONTH);
					if(cal1.get(Calendar.MONTH)+1 == 1 || cal1.get(Calendar.MONTH)+1 == 4 || cal1.get(Calendar.MONTH)+1 == 7 || cal1.get(Calendar.MONTH)+1 == 10){
						kuupaevad1.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 2 || cal1.get(Calendar.MONTH)+1 == 5 || cal1.get(Calendar.MONTH)+1 == 8 || cal1.get(Calendar.MONTH)+1 == 11){
						kuupaevad2.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 3 || cal1.get(Calendar.MONTH)+1 == 6 || cal1.get(Calendar.MONTH)+1 == 9 || cal1.get(Calendar.MONTH)+1 == 12){
						kuupaevad3.add(päev+"");
					}
				}
			}
			try{rs0.close();stmt0.close();}catch(Exception ex){}
			
			Map<Long,String> kuupäevadKuludMap = new HashMap<Long,String>();
			
			Statement stmt01 = con.createStatement();
			String query01 = "SELECT aeg FROM kulud WHERE YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND kulud.juhtID="+juhtID+" AND kasPalk=1";
			ResultSet rs01 = stmt01.executeQuery(query01);
			
			while(rs01.next()){
				
				long aeg = rs01.getTimestamp("aeg").getTime();
				
				if(kuupäevadMap.get(aeg) == null){
					kuupäevadKuludMap.put(aeg, TabeliData.AJAFORMAAT.format(aeg));
					
					Calendar cal1 = Calendar.getInstance();
					cal1.setTime(new Date(aeg));
					int päev = cal1.get(Calendar.DAY_OF_MONTH);
					if(cal1.get(Calendar.MONTH)+1 == 1 || cal1.get(Calendar.MONTH)+1 == 4 || cal1.get(Calendar.MONTH)+1 == 7 || cal1.get(Calendar.MONTH)+1 == 10){
						kuupaevadKulud1.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 2 || cal1.get(Calendar.MONTH)+1 == 5 || cal1.get(Calendar.MONTH)+1 == 8 || cal1.get(Calendar.MONTH)+1 == 11){
						kuupaevadKulud2.add(päev+"");
					}
					else if(cal1.get(Calendar.MONTH)+1 == 3 || cal1.get(Calendar.MONTH)+1 == 6 || cal1.get(Calendar.MONTH)+1 == 9 || cal1.get(Calendar.MONTH)+1 == 12){
						kuupaevadKulud3.add(päev+"");
					}
				}
			}
			try{rs01.close();stmt01.close();}catch(Exception ex){}
			
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi, kasutajaID FROM kasutajad WHERE töötab=1"+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Kasutaja kasutaja = new Kasutaja();
				TabeliData data = new TabeliData();
				
				data = lisaKuudJärjekorrasMapi(data,kuupäevadMap,kuupäevadKuludMap);
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				int kasutajaID = rs.getInt("kasutajaID");
				
				kasutaja.setKasutajaID(kasutajaID);
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				int x = 0;
				
				for(int i = algusKvartal; i <= lõppKvartal ;i++){
					Statement stmt2 = con.createStatement();
					String query2 = "SELECT palk,päev FROM palgad WHERE kasutaja_ID="+kasutajaID+" AND kuu="+i+" AND aasta="+hetkeAasta+" AND juhtID="+juhtID;
					ResultSet rs2 = stmt2.executeQuery(query2);
					
					Double palk = 0.0;
					
					if(rs2.next()){
						palk = rs2.getDouble("palk");
						
						if(i == algusKvartal){
							palgaKuupaev1 = rs2.getInt("päev");
						}
						else if(i == lõppKvartal){
							palgaKuupaev3 = rs2.getInt("päev");
						}
						else{
							palgaKuupaev2 = rs2.getInt("päev");
						}
					}
					
					data.getPalgad()[x] = palk;
					
					try{rs2.close();stmt2.close();}catch(Exception ex){}
					
					x++;
				}

				Statement stmt3 = con.createStatement();
				String query3 = "SELECT tulu, osalus, aeg FROM tulud, projektikasutajad WHERE projektikasutajad.kasutaja_ID="+kasutajaID+" AND tulud.projekt_ID=projektikasutajad.projekt_ID AND YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND projektikasutajad.juhtID="+juhtID+" AND tulud.juhtID="+juhtID;
				ResultSet rs3 = stmt3.executeQuery(query3);
				
				while(rs3.next()){
					
					cal.setTime(new Date(rs3.getTimestamp("aeg").getTime()));

					int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
					String päev = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					
					if(algusKvartal <= kuu && kuu <= lõppKvartal){
						Double tulu = rs3.getDouble("tulu")*rs3.getDouble("osalus");

						data = lisaTuluTabeliAndmetesse(kuu,päev, tulu,data);
					}
					else{
						data = lisaTuluTabeliAndmetesse(kuu,päev, 0.0,data);
					}

				}
				
				try{rs3.close();stmt3.close();}catch(Exception ex){}
				
				Statement stmt4 = con.createStatement();
				String query4 = "SELECT kulu, aeg FROM kulud WHERE kulud.kuluNimi LIKE '%"+kasutajaNimi+"%' AND YEAR(aeg)="+hetkeAasta+" AND (MONTH(aeg)="+(algusKvartal)+" OR MONTH(aeg)="+(algusKvartal+1)+" OR MONTH(aeg)="+(algusKvartal+2)+")"+" AND kulud.juhtID="+juhtID+" AND kulud.kasPalk=1";
				ResultSet rs4 = stmt4.executeQuery(query4);

				while(rs4.next()){

					cal.setTime(new Date(rs4.getTimestamp("aeg").getTime()));

					int kuu = cal.get(Calendar.MONTH)+1; // +1 sest date-s algavad kuud 0-st
					String päev = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
					
					if(algusKvartal <= kuu && kuu <= lõppKvartal){
						Double kulu = rs4.getDouble("kulu");

						data = lisaKuluTabeliAndmetesse(kuu,päev, kulu,data);
					}
					else{
						data = lisaKuluTabeliAndmetesse(kuu,päev, 0.0,data);
					}

				}
				
				try{rs4.close();stmt4.close();}catch(Exception ex){}
				
				data.lisaTuludMapistListi();
				data.setPalgaKuupäevad(new int[]{palgaKuupaev1,palgaKuupaev2,palgaKuupaev3});
				
				kasutaja.setTabeliAndmed(data);

				kasutajad.add(kasutaja);
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}finally {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
        }
		
		int[] aastad = {hetkeAasta-3,hetkeAasta-2,hetkeAasta-1,hetkeAasta,hetkeAasta+1,hetkeAasta+2,hetkeAasta+3};
		
		////

		m.addAttribute("hetkeAasta", hetkeAasta);
		m.addAttribute("hetkeKvartal",hetkeKvartalNimi);
		m.addAttribute("aastad", aastad);
		m.addAttribute("kvartalid", kvartalid);
		
		m.addAttribute("kasutajad",kasutajad);
		
		m.addAttribute("kuupaevad1",kuupaevad1);
		m.addAttribute("kuupaevad2",kuupaevad2);
		m.addAttribute("kuupaevad3",kuupaevad3);
		
		m.addAttribute("kuupaevProjektiPalgad1",kuupaevadKulud1);
		m.addAttribute("kuupaevProjektiPalgad2",kuupaevadKulud2);
		m.addAttribute("kuupaevProjektiPalgad3",kuupaevadKulud3);
		
		m.addAttribute("palgaKuupaev1",palgaKuupaev1);
		m.addAttribute("palgaKuupaev2",palgaKuupaev2);
		m.addAttribute("palgaKuupaev3",palgaKuupaev3);
		
		m.addAttribute("esimeneKuu",Kuud.getKuudKvartalis(hetkeKvartalNumber).get(0));
		m.addAttribute("teineKuu",Kuud.getKuudKvartalis(hetkeKvartalNumber).get(1));
		m.addAttribute("kolmasKuu",Kuud.getKuudKvartalis(hetkeKvartalNumber).get(2));
		
		m.addAttribute("esimeneKuuNumber",esimeseKuuTulusid+1+esimeseKuuKulusid);
		m.addAttribute("teineKuuNumber",teiseKuuTulusid+1+teiseKuuKulusid);
		m.addAttribute("kolmasKuuNumber",kolmandaKuuTulusid+1+kolmandaKuuKulusid);
		m.addAttribute("kogupikkus",esimeseKuuTulusid+teiseKuuTulusid+kolmandaKuuTulusid+3+esimeseKuuKulusid+teiseKuuKulusid+kolmandaKuuKulusid);
		
		m.addAttribute("teade", teade);
		
		m.addAttribute("uusTootaja",new Kasutaja());
		m.addAttribute("kustutaTootaja",new Kasutaja());

		teade = null;
		
		return "vaadeTootajadTabel"; 
	}
	
	private TabeliData lisaKuudJärjekorrasMapi(TabeliData data, Map<Long,String> ajad, Map<Long,String> ajadPalgad){
		
		esimeseKuuTulusid = 0;teiseKuuTulusid = 0;kolmandaKuuTulusid = 0;
		esimeseKuuKulusid = 0;teiseKuuKulusid = 0;kolmandaKuuKulusid = 0;
		
		ajad = new TreeMap<Long, String>(ajad); // sortib aja (key) järgi

		Iterator<Entry<Long, String>> it = ajad.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<Long,String> pairs = (Map.Entry<Long,String>)it.next();
	        
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date(pairs.getKey()));
	        int kuu = cal.get(Calendar.MONTH)+1;

	        if((kuu==1 || kuu==4 || kuu==7 || kuu==10) && data.getTuludMap1().get(cal.get(Calendar.DAY_OF_MONTH)) == null){
	        	data.getTuludMap1().put((cal.get(Calendar.DAY_OF_MONTH))+"",0.0);
	        	esimeseKuuTulusid++;
	        }
	        else if((kuu==2 || kuu==5 || kuu==8 || kuu==11) && data.getTuludMap2().get(cal.get(Calendar.DAY_OF_MONTH)) == null){
	        	data.getTuludMap2().put((cal.get(Calendar.DAY_OF_MONTH))+"",0.0);
	        	teiseKuuTulusid++;
	        }
	        else if((kuu==3 || kuu==6 || kuu==9 || kuu==12) && data.getTuludMap3().get(cal.get(Calendar.DAY_OF_MONTH)) == null){
	        	data.getTuludMap3().put((cal.get(Calendar.DAY_OF_MONTH))+"",0.0);
	        	kolmandaKuuTulusid++;
	        }
	    }
	    
	    ajadPalgad = new TreeMap<Long, String>(ajadPalgad); // sortib aja (key) järgi
	    
		Iterator<Entry<Long, String>> it2 = ajadPalgad.entrySet().iterator();
	    while (it2.hasNext()) {
	        Map.Entry<Long,String> pairs = (Map.Entry<Long,String>)it2.next();
	        
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(new Date(pairs.getKey()));
	        int kuu = cal.get(Calendar.MONTH)+1;

	        if((kuu==1 || kuu==4 || kuu==7 || kuu==10) && data.getKuludMap1().get(cal.get(Calendar.DAY_OF_MONTH)) == null){
	        	data.getKuludMap1().put((cal.get(Calendar.DAY_OF_MONTH))+"",0.0);
	        	esimeseKuuKulusid++;
	        }
	        else if((kuu==2 || kuu==5 || kuu==8 || kuu==11) && data.getKuludMap2().get(cal.get(Calendar.DAY_OF_MONTH)) == null){
	        	data.getKuludMap2().put((cal.get(Calendar.DAY_OF_MONTH))+"",0.0);
	        	teiseKuuKulusid++;
	        }
	        else if((kuu==3 || kuu==6 || kuu==9 || kuu==12) && data.getKuludMap3().get(cal.get(Calendar.DAY_OF_MONTH)) == null){
	        	data.getKuludMap3().put((cal.get(Calendar.DAY_OF_MONTH))+"",0.0);
	        	kolmandaKuuKulusid++;
	        }
	    }

	    
		return data;
	}
	
	private TabeliData lisaTuluTabeliAndmetesse(int kuu, String päev, Double tulu, TabeliData tabeliAndmed){
		
        if(kuu==1 || kuu==4 || kuu==7 || kuu==10){
        	Double summa = 0.0;
        	try{
        		summa = tabeliAndmed.getTuludMap1().get(päev)+tulu;
        	}catch(Exception x){
        		summa = tulu;
        	}
        	tabeliAndmed.getTuludMap1().put(päev, summa);
        }
        else if(kuu==2 || kuu==5 || kuu==8 || kuu==11){
        	Double summa = 0.0;
        	try{
        		summa = tabeliAndmed.getTuludMap2().get(päev)+tulu;
        	}catch(Exception x){
        		summa = tulu;
        	}
        	tabeliAndmed.getTuludMap2().put(päev, summa);
        }
        else if(kuu==3 || kuu==6 || kuu==9 || kuu==12){
        	Double summa = 0.0;
        	try{
        		summa = tabeliAndmed.getTuludMap3().get(päev)+tulu;
        	}catch(Exception x){
        		summa = tulu;
        	}
        	tabeliAndmed.getTuludMap3().put(päev,  summa);
        }

		return tabeliAndmed;
	}
	
	private TabeliData lisaKuluTabeliAndmetesse(int kuu, String päev, Double kulu, TabeliData tabeliAndmed){
		
        if(kuu==1 || kuu==4 || kuu==7 || kuu==10){
        	Double summa = 0.0;
        	try{
        		summa = tabeliAndmed.getKuludMap1().get(päev)+kulu;
        	}catch(Exception x){
        		summa = kulu;
        	}
        	tabeliAndmed.getKuludMap1().put(päev, summa);
        }
        else if(kuu==2 || kuu==5 || kuu==8 || kuu==11){
        	Double summa = 0.0;
        	try{
        		summa = tabeliAndmed.getKuludMap2().get(päev)+kulu;
        	}catch(Exception x){
        		summa = kulu;
        	}
        	tabeliAndmed.getKuludMap2().put(päev, summa);
        }
        else if(kuu==3 || kuu==6 || kuu==9 || kuu==12){
        	Double summa = 0.0;
        	try{
        		summa = tabeliAndmed.getKuludMap3().get(päev)+kulu;
        	}catch(Exception x){
        		summa = kulu;
        	}
        	tabeliAndmed.getKuludMap3().put(päev,  summa);
        }

		return tabeliAndmed;
	}
	
	@RequestMapping(value = "/vaadeTootajadGraaf.htm", method = RequestMethod.GET)
	public String vaadeTootajadGraaf(HttpServletRequest request,Model m) {
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeTootajadGraaf.htm");
			return "redirect:/vaadeViga.htm";
		}
		
		if(juhtID == 0){
			if(request.getSession().getAttribute("juhtID") == null){
				juhtID = Integer.parseInt(LoginController.kontrolliSidOlemasolu(request.getCookies()).split("\\.")[0]);
				request.getSession().setAttribute("kasutajaNimi", LoginController.getKasutajaNimiCookiest(request.getCookies()));
			}
			else{
				juhtID = Integer.parseInt(String.valueOf(request.getSession().getAttribute("juhtID")));
			}
			
		}
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		
		Connection con = new Mysql().getConnection();
		
		try{

			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi FROM kasutajad WHERE töötab=1"+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs.getString("kasutajaNimi");
				
				kasutaja.setKasutajaNimi(kasutajaNimi);
				
				kasutajad.add(kasutaja);
			}

		}catch(Exception x){
			teade = "Viga andmebaasiga";
		}
		

		m.addAttribute("kasutajad",kasutajad);
		m.addAttribute("teade", teade);

		teade = null;
		
		return "vaadeTootajadGraaf"; 
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kasutajaNimi"})
	public View lisaUusTöötaja(@ModelAttribute("uusTootaja") Kasutaja kasutaja, Model m){

		int vastus = Kasutaja.lisaKasutajaAndmebaasi(kasutaja,juhtID);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else if(vastus == Kasutaja.VIGA_JUBA_EKSISTEERIB){
			teade = "Sellise nimega töötaja juba eksisteeerib";
		}
		else{
			teade = "Töötaja lisamine õnnestus";
		}
		
		return new RedirectView("vaadeTootajadTabel.htm");
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kasutajaID"})
	public View kustutaTöötaja(@ModelAttribute("kustutaTootaja") Kasutaja kasutaja, Model m){

		int vastus = Kasutaja.muudaKasutajaTöötuksAndmebaasis(kasutaja,juhtID);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja eemaldamine õnnestus";
		}
		
		return new RedirectView("vaadeTootajadTabel.htm");
	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"tootajad","aastaNumber","kvartal"})
	public void salvestaTootajatePalgad(HttpServletRequest request, HttpServletResponse response,@RequestParam("tootajad") String tootajad,@RequestParam("aastaNumber") int aasta, @RequestParam("kvartal") String kvartalS){
		
		int kvartal = 0;
		
		if(kvartalS.equals(kvartalid[0])){
			kvartal = 1;
		}
		else if(kvartalS.equals(kvartalid[1])){
			kvartal = 2;
		}
		else if(kvartalS.equals(kvartalid[2])){
			kvartal = 3;
		}
		else{
			kvartal = 4;
		}
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		
		for(String rida : tootajad.split("/")){

			TabeliData tabeliAndmed = new TabeliData();
			Kasutaja k = new Kasutaja();
			k.setKasutajaNimi(rida.split("#")[0]);
			
			
			try{
				tabeliAndmed.getPalgad()[0] = Double.parseDouble(rida.split("#")[1].split(";")[1]);
			}catch(Exception x){
				tabeliAndmed.getPalgad()[0] = 0.0;
			}
			
			try{
				tabeliAndmed.getPalgad()[1] = Double.parseDouble(rida.split("#")[2].split(";")[1]);
			}catch(Exception x){
				tabeliAndmed.getPalgad()[1] = 0.0;
			}
			
			try{
				tabeliAndmed.getPalgad()[2] = Double.parseDouble(rida.split("#")[3].split(";")[1]);
			}catch(Exception x){
				tabeliAndmed.getPalgad()[2] = 0.0;
			}

			try{
				tabeliAndmed.getPalgaKuupäevad()[0] = Integer.parseInt(rida.split("#")[1].split(";")[0]);
			}catch(Exception x){
				tabeliAndmed.getPalgaKuupäevad()[0] = 1;
			}
			
			try{
				tabeliAndmed.getPalgaKuupäevad()[1] = Integer.parseInt(rida.split("#")[2].split(";")[0]);
			}catch(Exception x){
				tabeliAndmed.getPalgaKuupäevad()[1] = 1;
			}
			
			try{
				tabeliAndmed.getPalgaKuupäevad()[2] = Integer.parseInt(rida.split("#")[3].split(";")[0]);
			}catch(Exception x){
				tabeliAndmed.getPalgaKuupäevad()[2] = 1;
			}
			
			int kuu1 = 0, kuu2 = 0, kuu3 = 0;
			
			if(kvartal==1){
				kuu1 = 1; kuu2 = 2; kuu3 = 3;
			}
			else if(kvartal==2){
				kuu1 = 4; kuu2 = 5; kuu3 = 6;
			}
			else if(kvartal==3){
				kuu1 = 7; kuu2 = 8; kuu3 = 9;
			}
			else{
				kuu1 = 10; kuu2 = 11; kuu3 = 12;
			}
			
			tabeliAndmed.getKuud()[0] = kuu1;
			tabeliAndmed.getKuud()[1] = kuu2;
			tabeliAndmed.getKuud()[2] = kuu3;
			
			k.setTabeliAndmed(tabeliAndmed);
			kasutajad.add(k);
			
		}

		int vastus = Kasutaja.muudaKasutajatePalkasidAndmebaasis(kasutajad,aasta,juhtID);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja palkade muutmine õnnestus";
		}

	}
	
	@RequestMapping(value = "/vaadeTootajadTabel.htm", method = RequestMethod.POST, params = {"kid","uusNimi"})
	public void muudaTöötajaNime(HttpServletRequest request, HttpServletResponse response,@RequestParam("kid") int kasutajaID,@RequestParam("uusNimi") String uusNimi){

		int vastus = Kasutaja.muudaKasutajaNimeAndmebaasis(kasutajaID,uusNimi,juhtID);
		
		if(vastus == Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL){
			teade = "Viga andmebaasiga ühendumisel";
		}
		else{
			teade = "Töötaja nime muutmine õnnestus";
		}

	}

}
