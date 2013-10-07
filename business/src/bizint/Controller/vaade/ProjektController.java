package bizint.Controller.vaade;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class ProjektController {

	@RequestMapping("/vaadeProjektEsimene.htm")
	public ModelAndView vaadeProjektEsimene() {
		
		/*
		 * nimi,reiting,kasutaja(vastutaja,aktiivne,nimi,osalus),kirjeldus,logi(sonum,formaaditudaeg),kommentaar(kasutaja,sonum,formaaditudaeg)
		 */
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		List<Logi> logi = new ArrayList<Logi>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		
		ResultSet rs = null;
		String nimi = null;
		String kirjeldus = null;
		
		try{
			nimi = rs.getString("nimi");
			kirjeldus = rs.getString("kirjeldus");
			
			
			while(rs.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs.getString("nimi");
				boolean aktiivne = rs.getBoolean("aktiivne");
				boolean vastutaja = rs.getBoolean("vastutaja");
				Double osalus = rs.getDouble("osalus");
				
				kasutaja.setNimi(kasutajaNimi);
				kasutaja.setAktiivne(aktiivne);
				kasutaja.setVastutaja(vastutaja);
				kasutaja.setOsalus(osalus);
				
				kasutajad.add(kasutaja);
			}
			
			while(rs.next()){
				Logi log = new Logi();
				
				String sonum = rs.getString("");
				Date aeg = rs.getDate("");
				
				log.setSonum(sonum);
				log.setAeg(aeg);
				
				logi.add(log);
			}
			while(rs.next()){
				Kommentaar kommentaar = new Kommentaar();
				
				Date aeg = rs.getDate("aeg");
				String sõnum = rs.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(sõnum);
				
				kommentaarid.add(kommentaar);
			}
		}catch(Exception x){
			
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setNimi(nimi);
		projekt.setKasutajad(kasutajad);
		projekt.setKirjeldus(kirjeldus);
		projekt.setLogi(logi);
		projekt.setKommentaarid(kommentaarid);
		
		return new ModelAndView("vaadeProjektEsimene", "projekt", projekt); 
	}

	@RequestMapping("/vaadeProjektTeine.htm")
	public ModelAndView vaadeProjektTeine() {
		
		/*
		 * nimi,kogutulu,tulu(summa,nimi,formaaditudaeg),kulu(summa,nimi,formaaditudaeg),kommentaar(sonum,kasutaja,formaaditudaeg)
		 */
		
		List<Kulu> kulud = new ArrayList<Kulu>();
		List<Tulu> tulud = new ArrayList<Tulu>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		
		ResultSet rs = null;
		
		String nimi = null;
		
		try{
			nimi = rs.getString("nimi");
			
			while(rs.next()){
				Tulu tulu = new Tulu();
				
				Double summa = rs.getDouble("summa");
				String tuluNimi = rs.getString("nimi");
				Date aeg = rs.getDate("aeg");
				
				tulu.setSumma(summa);
				tulu.setNimi(tuluNimi);
				tulu.setAeg(aeg);
				
				tulud.add(tulu);
			}
			
			while(rs.next()){
				Kulu kulu = new Kulu();
				
				Double summa = rs.getDouble("summa");
				String kuluNimi = rs.getString("nimi");
				Date aeg = rs.getDate("aeg");
				
				kulu.setSumma(summa);
				kulu.setNimi(kuluNimi);
				kulu.setAeg(aeg);
				
				kulud.add(kulu);
			}
			while(rs.next()){
				Kommentaar kommentaar = new Kommentaar();
				
				Date aeg = rs.getDate("aeg");
				String sõnum = rs.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(sõnum);
				
				kommentaarid.add(kommentaar);
			}
		}catch(Exception x){
			
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setNimi(nimi);
		projekt.setKulud(kulud);
		projekt.setTulud(tulud);
		projekt.setKommentaarid(kommentaarid);
		
		return new ModelAndView("vaadeProjektTeine", "projekt", projekt); 
	}
	
}
