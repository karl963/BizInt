package bizint.Controller.vaade;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bizint.andmebaas.Mysql;
import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class ProjektController {

	@RequestMapping("/vaadeProjektEsimene.htm")
	public String vaadeProjektEsimene(@RequestParam("id") int projektID, Model m) {
		
		String teade = null;
		
		/*
		 * nimi,reiting,kasutaja(vastutaja,aktiivne,nimi,osalus),kirjeldus,logi(sonum,formaaditudaeg),kommentaar(kasutaja,sonum,formaaditudaeg)
		 */
		
		List<Kasutaja> kasutajad = new ArrayList<Kasutaja>();
		List<Logi> logi = new ArrayList<Logi>();
		List<Kommentaar> kommentaarid = new ArrayList<Kommentaar>();
		
		String nimi = null;
		String kirjeldus = null;
		
		Connection con = Mysql.connection;;
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "SELECT kirjeldus, projektNimi FROM projektid WHERE projektID="+projektID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			nimi = rs.getString("projektNimi");
			kirjeldus = rs.getString("kirjeldus");
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT kasutajaNimi,aktiivne,vastutaja,osalus FROM projektiKasutajad, kasutajad WHERE projektiKasutajad.projekt_ID="+projektID+" AND kasutajad.kasutajaID=projektiKasutajad.kasutaja_ID";
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			while(rs2.next()){
				Kasutaja kasutaja = new Kasutaja();
				
				String kasutajaNimi = rs2.getString("kasutajaNimi");
				boolean aktiivne = rs2.getBoolean("aktiivne");
				boolean vastutaja = rs2.getBoolean("vastutaja");
				Double osalus = rs2.getDouble("osalus");
				
				kasutaja.setNimi(kasutajaNimi);
				kasutaja.setAktiivne(aktiivne);
				kasutaja.setVastutaja(vastutaja);
				kasutaja.setOsalus(osalus);
				
				kasutajad.add(kasutaja);
			}
			
			Statement stmt3 = con.createStatement();
			String query3 = "SELECT sonum,aeg FROM logid, projektid WHERE logid.projekt_ID="+projektID;
			ResultSet rs3 = stmt3.executeQuery(query3);
			
			while(rs3.next()){
				Logi log = new Logi();
				
				String sonum = rs3.getString("sonum");
				Date aeg = rs3.getTimestamp("aeg");
				
				log.setSonum(sonum);
				log.setAeg(aeg);
				
				logi.add(log);
			}
			
			Statement stmt4 = con.createStatement();
			String query4 = "SELECT sonum,aeg,kasutajaNimi FROM kommentaarid, projektid, kasutajad WHERE kommentaarid.projekt_ID="+projektID+" AND kommentaarid.kasutaja_ID=kasutajad.kasutajaID";
			ResultSet rs4 = stmt4.executeQuery(query4);
			
			while(rs4.next()){
				Kommentaar kommentaar = new Kommentaar();
				
				Date aeg = rs4.getTimestamp("aeg");
				String s�num = rs4.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				kasutaja.setNimi(rs4.getString("kasutajaNimi"));
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(s�num);
				
				kommentaarid.add(kommentaar);
			}
		}catch(Exception x){
			x.printStackTrace();
			teade = "Viga andmebaasiga";
		}
		
		Projekt projekt = new Projekt();
		
		projekt.setNimi(nimi);
		projekt.setKasutajad(kasutajad);
		projekt.setKirjeldus(kirjeldus);
		projekt.setLogi(logi);
		projekt.setKommentaarid(kommentaarid);
		
		m.addAttribute("projekt", projekt);
		m.addAttribute("message", teade);
		
		return "vaadeProjektEsimene"; 
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
				String s�num = rs.getString("sonum");
				
				Kasutaja kasutaja = new Kasutaja();
				
				kommentaar.setAeg(aeg);
				kommentaar.setKasutaja(kasutaja);
				kommentaar.setSonum(s�num);
				
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
