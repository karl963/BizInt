package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

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
import bizint.app.alam.Staatus;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class RahavoogController {
	
	private String teade;
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy");
	private int juhtID = 0;
	
	@RequestMapping(value = "/vaadeRahavoog.htm", method = RequestMethod.GET)
	public String vaadeRahavoog(HttpServletRequest request,@RequestParam(value = "aasta", defaultValue = "puudub") String hetkeAastaString,@RequestParam(value = "kvartal", defaultValue = "puudub") String hetkeKvartalString ,Model m){
		
		Connection con = (new Mysql()).getConnection();
		if(con==null){
			teade = "Viga andmebaasige ühendusmisel";
		}
		
		if(LoginController.kontrolliSidOlemasolu(request.getCookies()) == null){
			request.getSession().setAttribute("viga", VigaController.VIGA_MITTE_LOGITUD);
			request.getSession().setAttribute("suunatudLink", "vaadeRahavoog.htm");
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
		String andmed = "";
		List<Kulu> yldkulud = new ArrayList<Kulu>();
		
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
				alguseKuu=1;lõpuKuu=3;
			}
			else if(hetkeKuu >= 4 && hetkeKuu <= 6){
				alguseKuu=4;lõpuKuu=6;
			}
			else if(hetkeKuu >= 7 && hetkeKuu <= 9){
				alguseKuu=7;lõpuKuu=9;
			}
			else if(hetkeKuu >= 10 && hetkeKuu <= 12){
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
		
		cal.set(Calendar.YEAR, hetkeAasta);
		cal.set(Calendar.MONTH, alguseKuu);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		Date start = cal.getTime();
		String algus = AJAFORMAAT.format(start);
		
		cal.set(Calendar.MONTH, lõpuKuu); 
		cal.set(Calendar.DAY_OF_MONTH, 0); // viimane päev kuus
		Date end = cal.getTime();
		String lõpp = AJAFORMAAT.format(end);

		///////
		
		Map<String,Double> tulud = new HashMap<String,Double>();
		Map<String,Double> kulud = new HashMap<String,Double>();
		Map<String,Double> kuupäevad = new HashMap<String,Double>();
		
		Timestamp algusAeg = null,lõppAeg = null;
		
		try{
			algusAeg = new Timestamp(AJAFORMAAT.parse(algus).getTime());
			lõppAeg = new Timestamp(AJAFORMAAT.parse(lõpp).getTime());
		}catch(Exception x){
		}
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT kulu,aeg FROM kulud WHERE aeg >= '"+algusAeg.toString()+"' AND aeg <= '"+lõppAeg.toString()+"' AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()){
				Double kulu = rs.getDouble("kulu");
				Date aeg = rs.getTimestamp("aeg");
				
				if(kulu != 0.0){
					cal.setTime(aeg);
					String stringAeg = cal.get(Calendar.DAY_OF_MONTH)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.YEAR);
					
					kuupäevad.put(stringAeg, 0.0);
					
					if(kulud.get(stringAeg) != null){
						kulud.put(stringAeg, kulud.get(stringAeg)+kulu);
					}
					else{
						kulud.put(stringAeg, kulu);
					}
				}
			}
			try{rs.close();stmt.close();}catch(Exception ex){}
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT tulu,aeg FROM tulud WHERE aeg >= '"+algusAeg.toString()+"' AND aeg <= '"+lõppAeg.toString()+"' AND juhtID="+juhtID;
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			while(rs2.next()){
				Double tulu = rs2.getDouble("tulu");
				Date aeg = rs2.getTimestamp("aeg");
				
				if(tulu != 0.0){
					cal.setTime(aeg);
					String stringAeg = cal.get(Calendar.DAY_OF_MONTH)+"."+(cal.get(Calendar.MONTH)+1)+"."+cal.get(Calendar.YEAR);
					
					kuupäevad.put(stringAeg, 0.0);
					
					if(tulud.get(stringAeg) != null){
						tulud.put(stringAeg, tulud.get(stringAeg)+tulu);
					}
					else{
						tulud.put(stringAeg, tulu);
					}
				}
			}
			try{rs2.close();stmt2.close();}catch(Exception ex){}
			
			Calendar algusCal = Calendar.getInstance();
			algusCal.setTime(algusAeg);
			int algusPäev = algusCal.get(Calendar.DAY_OF_MONTH);
			int algusKuu = algusCal.get(Calendar.MONTH)+1;
			int algusAasta = algusCal.get(Calendar.YEAR);
			
			Calendar lõppCal = Calendar.getInstance();
			lõppCal.setTime(lõppAeg);
			int lõppPäev = lõppCal.get(Calendar.DAY_OF_MONTH);
			int lõppKuu = lõppCal.get(Calendar.MONTH)+1;
			int lõppAasta = lõppCal.get(Calendar.YEAR);
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT päev,kuu,palk,aasta FROM palgad WHERE kuu >= "+algusKuu+" AND kuu <= "+lõppKuu+" AND aasta >= "+algusAasta+" AND aasta <= "+lõppAasta+" AND juhtID="+juhtID;
			ResultSet rs3 = stmt3.executeQuery(query3);
			
			while(rs3.next()){
				Double kulu = rs3.getDouble("palk");
				
				if(kulu != 0.0){
					String stringAeg = rs3.getInt("päev")+"."+rs3.getInt("kuu")+"."+rs3.getInt("aasta");
	
					kuupäevad.put(stringAeg, 0.0);
					
					if(kulud.get(stringAeg) != null){
						kulud.put(stringAeg, kulud.get(stringAeg)+kulu);
					}
					else{
						kulud.put(stringAeg, kulu);
					}
				}
			}
			try{rs3.close();stmt3.close();}catch(Exception ex){}
			
			Statement stmt4 = con.createStatement();
			String query4 = "SELECT algusAeg, yldkulu FROM yldkulud WHERE korduv=0 AND MONTH(algusAeg)>="+alguseKuu+" AND MONTH(algusAeg)<="+lõpuKuu+" AND YEAR(algusAeg)="+algusAasta+" AND juhtID="+juhtID;
			ResultSet rs4 = stmt4.executeQuery(query4);
			
			while(rs4.next()){
				Double kulu = rs4.getDouble("yldKulu");
				
				if(kulu != 0.0){
					String stringAeg = AJAFORMAAT.format(rs4.getTimestamp("algusAeg"));
	
					kuupäevad.put(stringAeg, 0.0);
					
					if(kulud.get(stringAeg) != null){
						kulud.put(stringAeg, kulud.get(stringAeg)+kulu);
					}
					else{
						kulud.put(stringAeg, kulu);
					}
				}
			}
			try{rs4.close();stmt4.close();}catch(Exception ex){}
			
			Statement stmt5 = con.createStatement();
			String query5 = "SELECT algusAeg, yldkulu FROM yldkulud WHERE korduv=1 AND ((MONTH(algusAeg)<="+lõpuKuu+" AND YEAR(algusAeg)="+algusAasta+") OR (YEAR(algusAeg)<"+algusAasta+")) AND juhtID="+juhtID;
			ResultSet rs5 = stmt5.executeQuery(query5);
			
			while(rs5.next()){
				Double kulu = rs5.getDouble("yldKulu");
				
				if(kulu != 0.0){
					Calendar c = Calendar.getInstance();
					c.setTime(rs5.getTimestamp("algusAeg"));
					
					int kuu1 = 0;
					int kuu2 = lõpuKuu;

					if(c.before(algusCal)){ // kui on eelnev aeg kvartali algusele, siis lisame igasse kuusse
						kuu1 = alguseKuu;
					}
					else{ // kui on esimene - kolmas kuu
						kuu1 = c.get(Calendar.MONTH)+1;
					}
					
					int aasta = c.get(Calendar.YEAR);
					int päev = c.get(Calendar.DAY_OF_MONTH);

					for(int i = kuu1; i<=kuu2; i++){
						
						String stringAeg = päev+"."+i+"."+aasta;

						kuupäevad.put(stringAeg, 0.0);
						
						if(kulud.get(stringAeg) != null){
							kulud.put(stringAeg, kulud.get(stringAeg)+kulu);
						}
						else{
							kulud.put(stringAeg, kulu);
						}
					}
				}
			}
			try{rs5.close();stmt5.close();}catch(Exception ex){}
			
			andmed = paneAndmedStringi(tulud,kulud,kuupäevad);
			
			if(andmed.equals("")){
				teade = "Nende valikutega andmed puuduvad";
			}
			
			Statement stmt6 = con.createStatement();
			String query6 = "SELECT * FROM yldkulud WHERE juhtID="+juhtID;
			ResultSet rs6 = stmt6.executeQuery(query6);
			
			while(rs6.next()){
				
				Kulu kulu = new Kulu();
				
				Double summa = rs6.getDouble("yldKulu");
				String nimi = rs6.getString("yldkuluNimi");
				Date aeg = rs6.getTimestamp("algusAeg");
				boolean kordus = rs6.getBoolean("korduv");
				int id = rs6.getInt("yldkuluID");
			
				kulu.setSumma(summa);
				kulu.setKuluNimi(nimi);
				kulu.setKorduv(kordus);
				kulu.setAeg(aeg);
				kulu.setKuluID(id);
				
				yldkulud.add(kulu);
			}
			try{rs6.close();stmt6.close();}catch(Exception ex){}
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

		m.addAttribute("yldKulud",yldkulud);
		m.addAttribute("hetkeKuupaev",Kulu.AJAFORMAAT.format(new Date()));
		m.addAttribute("teade",teade);
		m.addAttribute("andmedString",andmed);
		
		teade = null;
		return "vaadeRahavoog"; 
	}
	
	private String paneAndmedStringi(Map<String, Double> tulud, Map<String, Double> kulud, Map<String, Double> kuupäevad){
		String andmed = "";
		
		kuupäevad = sordiMap(kuupäevad);

		Iterator<Entry<String, Double>> it = kuupäevad.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String, Double> pairs = (Map.Entry<String, Double>)it.next();

	        Double kulu = 0.0;
	        Double tulu = 0.0;

	        String päev = pairs.getKey().split("\\.")[0];
	        String kuu = pairs.getKey().split("\\.")[1];
	        
	        if(kulud.get(pairs.getKey()) != null){
	        	kulu = kulud.get(pairs.getKey());
	        }

	        if(tulud.get(pairs.getKey()) != null){
	        	tulu = tulud.get(pairs.getKey());
	        }
	        
	        Double kasum = tulu-kulu;
	        
	        andmed += ( päev+"."+kuu + ";"+tulu+";"+kulu+";"+kasum+"/");
	        
	    }
		
		return andmed;
	}
	
	private Map<String, Double> sordiMap(Map<String,Double> map){
		
		Map<String,Double> uusMap = new LinkedHashMap<String,Double>(); // linkedHashMap on insertion order based

		while(map.size()>0){
			
			String väikseim = "";
			Double value = 0.0;
			int i = 0;
			
			Iterator<Entry<String, Double>> it = map.entrySet().iterator();
		    while (it.hasNext()) {

		        Map.Entry<String, Double> pairs = (Map.Entry<String, Double>)it.next();
		        
		    	if(i==0){
		    		väikseim = pairs.getKey();
		    	}

		    	/*
		    	 * kontrollib aastaid, aga kuna meil on ainult kvartalid, siis aasta ei loe
		        if(Integer.parseInt(pairs.getKey().split("\\.")[2]) < Integer.parseInt(väikseim.split("\\.")[2])){
		        	väikseim = pairs.getKey();
		        	value = pairs.getValue();

		        }
		        */
		        //if(Integer.parseInt(pairs.getKey().split("\\.")[2]) == Integer.parseInt(väikseim.split("\\.")[2])){
		        	if(Integer.parseInt(pairs.getKey().split("\\.")[1]) < Integer.parseInt(väikseim.split("\\.")[1])){
		        		väikseim = pairs.getKey();
		        		value = pairs.getValue();
		        		
		        	}
		        	else if(Integer.parseInt(pairs.getKey().split("\\.")[1]) == Integer.parseInt(väikseim.split("\\.")[1])){
		        		if(Integer.parseInt(pairs.getKey().split("\\.")[0]) <= Integer.parseInt(väikseim.split("\\.")[0])){
		        			väikseim = pairs.getKey();
		        			value = pairs.getValue();
		        		}
		        	}
		        //}
		        i++;
		    }
		    
		    map.remove(väikseim);
		    uusMap.put(väikseim, value);

		}
		
		return uusMap;
	}
	
	@RequestMapping(value = "/vaadeRahavoog.htm", method = RequestMethod.POST, params = {"summa","nimetus","kuupaev","korduv"})
	public void lisaUusYldKulu(@RequestParam("summa") String s,@RequestParam("nimetus") String nimetus,@RequestParam("kuupaev") String k,@RequestParam("korduv") boolean korduv){

		Double summa = 0.0;
		try{
			summa = Double.parseDouble(s);
		}catch(Exception x){
			teade = "Vigane summa !";
		}
		
		Date kuupäev = null;
		try{
			kuupäev = Kulu.AJAFORMAAT.parse(k);
		}catch(Exception x){
			teade = "Vigane kuupäev !";
			return;
		}
		
		int vastus = Kulu.lisaYldKuluAndmebaasi(new Kulu(nimetus,kuupäev,summa),korduv,juhtID);
				
		if(vastus == Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL){
			teade = "Viga andmebaasiga ühendumisel!";
		}
		else if(vastus == Kulu.KÕIK_OKEI){
			teade = "Üldkulu lisamine õnnestus!";
		}
		
	}
	
	@RequestMapping(value = "/vaadeRahavoog.htm", method = RequestMethod.POST, params = {"yldKulud"})
	public void muudaYldKuludAndmebaasis(@RequestParam("yldKulud") String kuludString){

		List<Kulu> kulud = new ArrayList<Kulu>();
		
		for(String rida : kuludString.split("#")){
			
			Double summa = 0.0;
			try{
				summa = Double.parseDouble(rida.split(";")[2]);
			}catch(Exception x){
			}
			
			Date kuupäev = null;
			try{
				kuupäev = Kulu.AJAFORMAAT.parse(rida.split(";")[3]);
			}catch(Exception x){
			}
			
			Kulu kulu = new Kulu();
			
			kulu.setKuluNimi(rida.split(";")[1]);
			kulu.setKorduv(Boolean.parseBoolean(rida.split(";")[4]));
			kulu.setKuluID(Integer.parseInt(rida.split(";")[0]));
			kulu.setAeg(kuupäev);
			kulu.setSumma(summa);
			
			kulud.add(kulu);
		}
		
		int vastus = Kulu.muudaYldKuludAndmebaasis(kulud,juhtID);
				
		if(vastus == Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL){
			teade = "Viga andmebaasiga ühendumisel!";
		}
		else if(vastus == Kulu.KÕIK_OKEI){
			teade = "Üldkulude muutmine õnnestus!";
		}
		
	}

	@RequestMapping(value = "/vaadeRahavoog.htm", method = RequestMethod.POST, params = {"kuluID"})
	public void kustutaYldKulu(@RequestParam("kuluID") int kuluID){

		int vastus = Kulu.kustutaKuluAndmebaasist(kuluID,juhtID);
				
		if(vastus == Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL){
			teade = "Viga andmebaasiga ühendumisel!";
		}
		else if(vastus == Kulu.KÕIK_OKEI){
			teade = "Üldkulu kustutamine õnnestus!";
		}
		
	}
	
}
