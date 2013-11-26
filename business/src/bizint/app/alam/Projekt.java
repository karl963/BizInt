package bizint.app.alam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import bizint.andmebaas.Mysql;
import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;
import bizint.post.UusKasutaja;
import bizint.post.UusKirjeldus;
import bizint.post.UusKommentaar;
import bizint.post.UusProjektiNimi;
public class Projekt {
	
	private static final String DEFAULT_NIMI = "uus projekt";
	private static final String DEFAULT_KIRJELDUS = "lahe projekt";
	private static final int DEFAULT_REITING = 0;
	private static final int DEFAULT_PROJEKTI_JÄRJEKORRA_NUMBER = 0;
	public static final int ERROR_JUBA_EKSISTEERIB = 0, VIGA_ANDMEBAASIGA_ÜHENDUMISEL = 1, KÕIK_OKEI = 2, PROJEKTI_NIMI_TÜHI = 3;
	
	private String nimi,kirjeldus;
	private int id;
	private List<Kasutaja> kasutajad;
	private List<Tulu> tulud;
	private List<Kulu> kulud;
	private List<Kommentaar> kommentaarid;
	private List<Logi> logi;
	private int reiting;
	private int staatusID;
	private String reitinguHTML;
	private int projektiJärjekorraNumber;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Projekt(){
		this.nimi = Projekt.DEFAULT_NIMI;
		this.kirjeldus = Projekt.DEFAULT_KIRJELDUS;
		this.kasutajad = new ArrayList<Kasutaja>();
		this.tulud = new ArrayList<Tulu>();
		this.kulud = new ArrayList<Kulu>(); 
		this.kommentaarid = new ArrayList<Kommentaar>(); 
		this.logi = new ArrayList<Logi>();
		this.reiting = Projekt.DEFAULT_REITING;
		this.setProjektiJärjekorraNumber(Projekt.DEFAULT_PROJEKTI_JÄRJEKORRA_NUMBER);
	}
	
	public Projekt(String nimi){
		this.nimi = nimi;
		this.kirjeldus = Projekt.DEFAULT_KIRJELDUS;
		this.kasutajad = new ArrayList<Kasutaja>();
		this.tulud = new ArrayList<Tulu>();
		this.kulud = new ArrayList<Kulu>(); 
		this.kommentaarid = new ArrayList<Kommentaar>(); 
		this.logi = new ArrayList<Logi>();
		this.reiting = Projekt.DEFAULT_REITING;
		this.setProjektiJärjekorraNumber(Projekt.DEFAULT_PROJEKTI_JÄRJEKORRA_NUMBER);
	}
	
	public Projekt(String nimi, int projektiJärjekorraNumber){
		this.nimi = nimi;
		this.kirjeldus = Projekt.DEFAULT_KIRJELDUS;
		this.kasutajad = new ArrayList<Kasutaja>();
		this.tulud = new ArrayList<Tulu>();
		this.kulud = new ArrayList<Kulu>(); 
		this.kommentaarid = new ArrayList<Kommentaar>(); 
		this.logi = new ArrayList<Logi>();
		this.reiting = Projekt.DEFAULT_REITING;
		this.setProjektiJärjekorraNumber(projektiJärjekorraNumber);
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public void addTulu(Tulu tulu){
		tulud.add(tulu);
	}
	public void addKulu(Kulu kulu){
		kulud.add(kulu);
	}
	
	public Double getKogutulu(){
		Double summa = 0.0;
		for(Tulu t : tulud){
			summa+=(Double)t.getSumma();
		}
		return summa;
	}
	
	public Double getKogukulu(){
		Double summa = 0.0;
		for(Kulu k : kulud){
			summa+=(Double)k.getSumma();
		}
		return summa;
	}
	
	public Kasutaja getVastutaja(){
		for(Kasutaja k : kasutajad){
			if(k.isVastutaja()){
				return k;
			}
		}
		return null;
	}
	
	public static List<Projekt> paneJärjekorda(List<Projekt> projektid){
		
		List<Projekt> sorteeritudProjektid = new ArrayList<Projekt>();
		
		while(projektid.size() > 0 ){
			int väikseimNumber = projektid.get(0).getProjektiJärjekorraNumber();
			int index = 0;
			int i = 0;
			
			for(Projekt p : projektid){
				if(p.getProjektiJärjekorraNumber() < väikseimNumber){
					väikseimNumber = p.getProjektiJärjekorraNumber();
					index = i;
				}
				i++;
			}
			
			sorteeritudProjektid.add(projektid.get(index));
			projektid.remove(index);
		}
		
		return sorteeritudProjektid;
		
	}
	
	public static int lisaProjektAndmebaasi(Projekt projekt,int staatusID, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		if(projekt.getNimi() != null && !projekt.getNimi().isEmpty() && !projekt.getNimi().trim().isEmpty()){
			try {
				
				int projektiJärjekorraNR = 1;
				
				Statement stmt0 = con.createStatement();
				String query0 = "SELECT MAX(projektiJärjekorraNR) AS max FROM projektid WHERE staatus_ID="+staatusID+" AND juhtID="+juhtID;
				ResultSet rs0 = stmt0.executeQuery(query0);
				
				if(rs0.next()){
					projektiJärjekorraNR = rs0.getInt("max") + 1;
				}
				
				Statement stmt = con.createStatement();
				String query = "INSERT INTO projektid (projektNimi, staatus_ID, projektiJärjekorraNR,juhtID) VALUES ('"+projekt.getNimi()+"',"+staatusID+","+projektiJärjekorraNR+","+juhtID+")";
				stmt.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
				
				ResultSet rs = stmt.getGeneratedKeys();
				
				if(rs.next()){
					try {
						
						Statement stmt2 = con.createStatement();
						String query2 = "SELECT staatusNimi FROM staatused WHERE staatusID="+staatusID+" AND juhtID="+juhtID;
						ResultSet rs2 = stmt2.executeQuery(query2);
						
						rs2.next();
						
						String staatusnimi = rs2.getString("staatusNimi");
						
						Statement stmt9 = con.createStatement();
						String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
						ResultSet rs9 = stmt9.executeQuery(query9);
						rs9.next();
						String kasutajanimi = rs9.getString("kasutajaNimi");
						
						Statement stmt3 = con.createStatement();
						String query3 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+rs.getInt(1)+",'"+kasutajanimi+" tekitas projekti staatusesse : "+staatusnimi+"',"+juhtID+")";
						stmt3.executeUpdate(query3);
					
						try{rs2.close();stmt2.close();}catch(Exception x){}
						try{stmt3.close();}catch(Exception x){}
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				try{stmt.close();}catch(Exception x){}
			} catch (SQLException e) {
				e.printStackTrace();
				if (con!=null) try {con.close();}catch (Exception ignore) {}
				return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
			}
		}
		if(projekt.getNimi() == null || projekt.getNimi().isEmpty() || projekt.getNimi().trim().isEmpty()){
			return Projekt.PROJEKTI_NIMI_TÜHI;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiKirjeldusAndmebaasis(UusKirjeldus uusKirjeldus, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE projektid SET kirjeldus = '"+uusKirjeldus.getKirjeldus()+"' WHERE projektID="+uusKirjeldus.getProjektID()+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
			
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+uusKirjeldus.getProjektID()+","+"'"+kasutajanimi+" muutis projekti kirjeldust',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaUusKasutajaAndmebaasi(UusKasutaja uusKasutaja, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "INSERT INTO projektikasutajad (kasutaja_ID, projekt_ID,juhtID) VALUES ((SELECT kasutajaID FROM kasutajad WHERE kasutajaNimi='"+uusKasutaja.getKasutajaNimi()+"' AND juhtID="+juhtID+"),"+uusKasutaja.getProjektID()+","+juhtID+")";
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+uusKasutaja.getProjektID()+","+"'"+kasutajanimi+" määras "+uusKasutaja.getKasutajaNimi()+" projekti töötajaks',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaKasutajateAndmeidProjektigaAndmebaasis(String kasutajad, int projektID, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		boolean oliJubaVastutaja = false;
		
		for(String kasutaja : kasutajad.split(";")){
			
			if(kasutaja == null || kasutaja.equals(" ") || kasutaja.equals("")){
				break;
			}
			
			boolean aktiivne;
			boolean vastutaja;
			Double osalus;
			int kasutajaID;
			
			try{
				vastutaja = Boolean.parseBoolean(kasutaja.split("''")[0]);
				
				if(vastutaja && oliJubaVastutaja){
					vastutaja = false;
				}
				else if(vastutaja){
					oliJubaVastutaja = true;
				}
			}catch(Exception x){
				vastutaja = false;
			}
			
			try{
				aktiivne = Boolean.parseBoolean(kasutaja.split("''")[1]);
			}catch(Exception x){
				aktiivne = false;
			}
			
			try{
				kasutajaID = Integer.parseInt(kasutaja.split("''")[2]);
			}catch(Exception x){
				kasutajaID = 0;
			}
			
			try{
				osalus = Double.parseDouble(kasutaja.split("''")[3].replaceAll(" ", ""));
			}catch(Exception x){
				osalus = 0.0;
			}
			
			Statement stmt;
			try {
				stmt = con.createStatement();
				String query = "UPDATE projektikasutajad SET aktiivne="+aktiivne+", osalus="+osalus+",vastutaja="+vastutaja+" WHERE projekt_ID="+projektID+" AND kasutaja_ID="+kasutajaID+" AND juhtID="+juhtID;
				stmt.executeUpdate(query);
				
				try{stmt.close();}catch(Exception x){}
			} catch (SQLException e) {
				e.printStackTrace();
				if (con!=null) try {con.close();}catch (Exception ignore) {}
				return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
			}
			
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" muutis töötajate andmeid projektis',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiNimeAndmebaasis(UusProjektiNimi nimi, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		if(nimi.getUusNimi() != null && !nimi.getUusNimi().isEmpty() && !nimi.getUusNimi().trim().isEmpty()){
			Statement stmt;
			try {
				stmt = con.createStatement();
				String query = "UPDATE projektid SET projektNimi = '"+nimi.getUusNimi()+"' WHERE projektID="+nimi.getProjektID()+" AND juhtID="+juhtID;
				stmt.executeUpdate(query);
				
				try{stmt.close();}catch(Exception x){}
			} catch (SQLException e) {
				e.printStackTrace();
				if (con!=null) try {con.close();}catch (Exception ignore) {}
				return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
			}
			
			try {
				
				Statement stmt9 = con.createStatement();
				String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
				ResultSet rs9 = stmt9.executeQuery(query9);
				rs9.next();
				String kasutajanimi = rs9.getString("kasutajaNimi");
				
				Statement stmt2 = con.createStatement();
				String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+nimi.getProjektID()+","+"'"+kasutajanimi+" määras projektile uue nime : "+nimi.getUusNimi()+"',"+juhtID+")";
				stmt2.executeUpdate(query2);
				
				try{stmt2.close();}catch(Exception x){}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(nimi.getUusNimi() == null || nimi.getUusNimi().isEmpty() || nimi.getUusNimi().trim().isEmpty()){
			return Projekt.PROJEKTI_NIMI_TÜHI;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int paneProjektArhiiviAndmebaasis(int projektID, int juhtID){
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int hetkeAasta = cal.get(Calendar.YEAR);
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE projektid SET aeg = '"+hetkeAasta+"' arhiivis = '1', staatus_ID = '1' WHERE projektID="+projektID+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			Statement stmtlogi1 = con.createStatement();
			String querylogi1 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rslogi1 = stmtlogi1.executeQuery(querylogi1);
			rslogi1.next();
			String kasutajanimi = rslogi1.getString("kasutajaNimi");
			
			Statement stmtlogi2 = con.createStatement();
			String querylogi2 = "INSERT INTO logid (projekt_ID, sonum, juhtID) VALUES ("+projektID+",'"+kasutajanimi+" arhiveeris projekti',"+juhtID+")";
			stmtlogi2.executeUpdate(querylogi2);	
			
			try{stmt.close();}catch(Exception x){}
			try{stmtlogi1.close();}catch(Exception x){}
			try{stmtlogi2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiReitingutAndmebaasis(int projektID, int reiting, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE projektid SET reiting = "+reiting+" WHERE projektID="+projektID+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" muutis projekti reitingut : "+reiting+"',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaKommentaarAndmebaasi(UusKommentaar uusKommentaar, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			
			
			
			stmt = con.createStatement();
			String query = "INSERT INTO kommentaarid (sonum, projekt_ID, juhtID) VALUES ('"+uusKommentaar.getSonum()+"',"+uusKommentaar.getProjektID()+","+juhtID+")";
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}

	public static int lisaKuluAndmebaasi(Kulu kulu, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			
			Timestamp aeg = new Timestamp(kulu.getAeg().getTime());
		
			if(!kulu.getKasutajaNimi().equals("")){
				String query = "INSERT INTO kulud (kulu, aeg, kuluNimi, projekt_ID, kaspalk,kaibemaksuArvestatakse,juhtID) "
						+ "VALUES ("+kulu.getSumma()+",'"+aeg+"','palk töötajale: "+kulu.getKasutajaNimi()+"',"+kulu.getProjektID()+",1,"+kulu.getkasArvestaKaibemaksu()+","+juhtID+")";
				stmt.executeUpdate(query);
			}
			else{
				String query = "INSERT INTO kulud (kulu, aeg, kuluNimi, projekt_ID,kaibemaksuArvestatakse,juhtID) "
					+ "VALUES ("+kulu.getSumma()+",'"+aeg+"','"+kulu.getKuluNimi()+"',"+kulu.getProjektID()+","+kulu.getkasArvestaKaibemaksu()+","+juhtID+")";
				stmt.executeUpdate(query);
			}
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2;
			if(kulu.getKuluNimi().equals(" ") || kulu.getKuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+kulu.getProjektID()+","+"'"+kasutajanimi+" lisas projektile uue kulu : "+kulu.getSumma()+"',"+juhtID+")";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+kulu.getProjektID()+","+"'"+kasutajanimi+" lisas projektile uue kulu : "+kulu.getKuluNimi()+" ("+kulu.getSumma()+")',"+juhtID+")";

			}
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
		
	}
	public static int lisaTuluAndmebaasi(Tulu tulu, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			
			Timestamp aeg = new Timestamp(tulu.getAeg().getTime());
			String query = "INSERT INTO tulud (tulu, aeg, tuluNimi, projekt_ID,kaibemaksuArvestatakse,juhtID) "
				+ "VALUES ("+tulu.getSumma()+",'"+aeg+"','"+tulu.getTuluNimi()+"',"+tulu.getProjektID()+","+tulu.getkasArvestaKaibemaksu()+","+juhtID+")";

			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2;
			if(tulu.getTuluNimi().equals(" ") || tulu.getTuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+tulu.getProjektID()+","+"'"+kasutajanimi+" lisas projektile uue tulu : "+tulu.getSumma()+"',"+juhtID+")";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+tulu.getProjektID()+","+"'"+kasutajanimi+" lisas projektile uue tulu : "+tulu.getTuluNimi()+" ("+tulu.getSumma()+")',"+juhtID+")";

			}
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int kustutaTuluAndmebaasist(Tulu tulu, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt = null;
		try {
			stmt = con.createStatement();
		}catch(Exception x){return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;}
		
		Timestamp aeg = null;
		String query = null;
		
		try {
			
			aeg = new Timestamp(Tulu.AJAFORMAAT.parse(tulu.getStringAeg()).getTime());
			query = "DELETE FROM tulud WHERE tulu="+tulu.getSumma()
					+ " AND aeg='"+aeg
					+ "' AND tuluNimi='"+tulu.getTuluNimi()
					+ "' AND projekt_ID="+tulu.getProjektID()
					+ " AND juhtID="+juhtID
					+" LIMIT 1";

		} catch (ParseException e1) {
			
			query = "DELETE FROM tulud WHERE tulu="+tulu.getSumma()
					+ " AND tuluNimi='"+tulu.getTuluNimi()
					+ "' AND projekt_ID="+tulu.getProjektID()
					+ " AND juhtID="+juhtID
					+" LIMIT 1";
		}
		
		try {
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2;
			if(tulu.getTuluNimi().equals(" ") || tulu.getTuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+tulu.getProjektID()+","+"'"+kasutajanimi+" eemaldas projektist tulu : "+tulu.getSumma()+"',"+juhtID+")";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+tulu.getProjektID()+","+"'"+kasutajanimi+" eemaldas projektist tulu : "+tulu.getTuluNimi()+" ("+tulu.getSumma()+")'"+juhtID+")";

			}
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int kustutaKuluAndmebaasist(Kulu kulu, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		Timestamp aeg = null;
		String query = null;
		
		try {

			aeg = new Timestamp(Kulu.AJAFORMAAT.parse(kulu.getStringAeg()).getTime());

			query = "DELETE FROM kulud WHERE kulu="+kulu.getSumma()
					+ " AND aeg='"+aeg
					+ "' AND kuluNimi='"+kulu.getKuluNimi()
					+ "' AND projekt_ID="+kulu.getProjektID()
					+ " AND juhtID="+juhtID
					+" LIMIT 1";
			
		} catch (ParseException e1) {

			query = "DELETE FROM kulud WHERE kulu="+kulu.getSumma()
					+ " AND kuluNimi='"+kulu.getKuluNimi()
					+ "' AND projekt_ID="+kulu.getProjektID()
					+ " AND juhtID="+juhtID
					+" LIMIT 1";
		}
		
		try {
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2;
			if(kulu.getKuluNimi().equals(" ") || kulu.getKuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+kulu.getProjektID()+","+"'"+kasutajanimi+" eemaldas projektist kulu : "+kulu.getSumma()+"',"+juhtID+")";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+kulu.getProjektID()+","+"'"+kasutajanimi+" eemaldas projektist kulu : "+kulu.getKuluNimi()+" ("+kulu.getSumma()+")',"+juhtID+")";

			}
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int kustutaProjektAndmebaasist(int id, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT projektiJärjekorraNR AS jNR FROM projektid WHERE projektID="+id+" AND juhtID="+juhtID;
			ResultSet rs0 = stmt0.executeQuery(query0);
			
			rs0.next();
			int projektiJärjekorraNR = rs0.getInt("jNR");
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT staatus_ID AS idStaatus FROM projektid WHERE projektID="+id+" AND juhtID="+juhtID;
			ResultSet rs2 = stmt2.executeQuery(query2);
			
			rs2.next();
			String staatus = rs2.getString("idStaatus");
			
			Statement stmt1 = con.createStatement();
			String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR - 1 WHERE projektiJärjekorraNR>"+projektiJärjekorraNR+" AND staatus_ID="+staatus+" AND juhtID="+juhtID;
			stmt1.executeUpdate(query1);
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM projektid WHERE projektID="+id+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			try{stmt2.close();stmt1.close();stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM projektikasutajad WHERE projekt_ID="+id+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			Statement stmt2 = con.createStatement();
			String query2 = "DELETE FROM tulud WHERE projekt_ID="+id+" AND juhtID="+juhtID;
			stmt2.executeUpdate(query2);
			
			Statement stmt3 = con.createStatement();
			String query3 = "DELETE FROM logid WHERE projekt_ID="+id+" AND juhtID="+juhtID;
			stmt3.executeUpdate(query3);
			
			Statement stmt4 = con.createStatement();
			String query4 = "DELETE FROM kulud WHERE projekt_ID="+id+" AND juhtID="+juhtID;
			stmt4.executeUpdate(query4);
			
			Statement stmt5 = con.createStatement();
			String query5 = "DELETE FROM kommentaarid WHERE projekt_ID="+id+" AND juhtID="+juhtID;
			stmt5.executeUpdate(query5);
			
			try{stmt.close();}catch(Exception x){}
			try{stmt2.close();}catch(Exception x){}
			try{stmt3.close();}catch(Exception x){}
			try{stmt4.close();}catch(Exception x){}
			try{stmt5.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int eemaldaKasutajaProjektistAndmebaasis(int kasutajaID, int projektID, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "DELETE FROM projektikasutajad WHERE projekt_ID="+projektID+" AND kasutaja_ID="+kasutajaID+" AND juhtID="+juhtID+" LIMIT 1";
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi FROM kasutajad WHERE kasutajaID="+kasutajaID+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			String nimi = rs.getString("kasutajaNimi");
			
			Statement stmt3 = con.createStatement();
			String query3 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" eemaldas projektist töötaja : "+nimi+"',"+juhtID+")";
			stmt3.executeUpdate(query3);
			
			try{stmt.close();}catch(Exception x){}
			try{stmt3.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaKasutajaProjektiAndmebaasis(String kasutajaNimi, int projektID, int juhtID){

		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "INSERT INTO projektikasutajad (kasutaja_ID, projekt_ID,juhtID) VALUES ((SELECT kasutajaID FROM kasutajad WHERE kasutajaNimi='"+kasutajaNimi+"' AND juhtID="+juhtID+"), "+projektID+","+juhtID+")";
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" lisas projekti töötaja : "+kasutajaNimi+"',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiStaatustAndmebaasis(int projektID, int staatusID, int projektiJärjekorraNR, int staatusVanaID, int projektiVanaJärjekorraNR, int juhtID) {
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			
			if(staatusID == staatusVanaID){
				if(projektiJärjekorraNR != 1 && projektiVanaJärjekorraNR-projektiJärjekorraNR<0){
					Statement stmt1 = con.createStatement();
					String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR +1 WHERE staatus_ID=" + staatusID + " AND projektiJärjekorraNR>"+projektiJärjekorraNR+" AND juhtID="+juhtID;
					stmt1.executeUpdate(query1);
					try{stmt1.close();}catch(Exception x){}
				}
				else{
					Statement stmt1 = con.createStatement();
					String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR +1 WHERE staatus_ID=" + staatusID + " AND projektiJärjekorraNR>="+projektiJärjekorraNR+" AND juhtID="+juhtID;
					stmt1.executeUpdate(query1);
					try{stmt1.close();}catch(Exception x){}
				}
			}	
			else{
				Statement stmt1 = con.createStatement();
				String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR +1 WHERE staatus_ID=" + staatusID + " AND projektiJärjekorraNR>="+projektiJärjekorraNR+" AND juhtID="+juhtID;
				stmt1.executeUpdate(query1);
				try{stmt1.close();}catch(Exception x){}
			}
			
			Statement stmt2 = con.createStatement();
			String query2 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR -1 WHERE staatus_ID=" + staatusVanaID + " AND projektiJärjekorraNR>"+projektiVanaJärjekorraNR+" AND juhtID="+juhtID;
			stmt2.executeUpdate(query2);
			
			stmt = con.createStatement();
			String query = "UPDATE projektid SET staatus_ID="+ staatusID + " , projektiJärjekorraNR="+ projektiJärjekorraNR +" WHERE projektID=" + projektID+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt1 = con.createStatement();
			String query1 = "SELECT staatusNimi FROM staatused WHERE staatusID="+staatusID+" AND juhtID="+juhtID;
			ResultSet rs = stmt1.executeQuery(query1);
			
			rs.next();
			
			String staatusnimi = rs.getString("staatusNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" muutis projekti staatust : "+staatusnimi+"',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{rs.close();stmt.close();}catch(Exception x){}
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiStaatustArhiiviAndmebaasis(int projektID, int staatusID, int projektiJärjekorraNR, int staatusVanaID, int projektiVanaJärjekorraNR, int juhtID) {
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			
			if(staatusID == staatusVanaID){
				if(projektiJärjekorraNR != 1 && projektiVanaJärjekorraNR-projektiJärjekorraNR<0){
					Statement stmt1 = con.createStatement();
					String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR +1 WHERE staatus_ID=" + staatusID + " AND projektiJärjekorraNR>"+projektiJärjekorraNR+" AND juhtID="+juhtID+" AND arhiivis = '1'";
					stmt1.executeUpdate(query1);
					try{stmt1.close();}catch(Exception x){}
				}
				else{
					Statement stmt1 = con.createStatement();
					String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR +1 WHERE staatus_ID=" + staatusID + " AND projektiJärjekorraNR>="+projektiJärjekorraNR+" AND juhtID="+juhtID+" AND arhiivis = '1'";;
					stmt1.executeUpdate(query1);
					try{stmt1.close();}catch(Exception x){}
				}
			}	
			else{
				Statement stmt1 = con.createStatement();
				String query1 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR +1 WHERE staatus_ID=" + staatusID + " AND projektiJärjekorraNR>="+projektiJärjekorraNR+" AND juhtID="+juhtID+" AND arhiivis = '1'";;
				stmt1.executeUpdate(query1);
				try{stmt1.close();}catch(Exception x){}
			}
			
			Statement stmt2 = con.createStatement();
			String query2 = "UPDATE projektid SET projektiJärjekorraNR=projektiJärjekorraNR -1 WHERE staatus_ID=" + staatusVanaID + " AND projektiJärjekorraNR>"+projektiVanaJärjekorraNR+" AND juhtID="+juhtID+" AND arhiivis = '1'";;
			stmt2.executeUpdate(query2);
			
			stmt = con.createStatement();
			String query = "UPDATE projektid SET staatus_ID="+ staatusID + " , projektiJärjekorraNR="+ projektiJärjekorraNR +" WHERE projektID=" + projektID+" AND juhtID="+juhtID+" AND arhiivis = '1'";;
			stmt.executeUpdate(query);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt1 = con.createStatement();
			String query1 = "SELECT staatusNimi FROM staatused WHERE staatusID="+staatusID+" AND juhtID="+juhtID;
			ResultSet rs = stmt1.executeQuery(query1);
			
			rs.next();
			
			String staatusnimi = rs.getString("staatusNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" muutis projekti staatust : "+staatusnimi+"',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{rs.close();stmt.close();}catch(Exception x){}
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiVastutajatAndmebaasis(int projektID,String kasutajaNimi, int juhtID){

		Connection con = new Mysql().getConnection();
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaID FROM kasutajad WHERE kasutajaNimi='"+kasutajaNimi+"'"+" AND juhtID="+juhtID;
			ResultSet rs = stmt.executeQuery(query);
			
			if(rs.next()){
				
				Statement stmt3 = con.createStatement();
				String query3 = "UPDATE projektikasutajad SET vastutaja=0 WHERE projekt_ID="+projektID+" AND juhtID="+juhtID;
				stmt3.executeUpdate(query3);
				
				try{stmt3.close();}catch(Exception x){}
				
				int kasutajaID = rs.getInt("kasutajaID");
				
				Statement stmt2 = con.createStatement();
				String query2 = "SELECT * FROM projektikasutajad WHERE kasutaja_ID="+kasutajaID+" AND projekt_ID="+projektID+" AND juhtID="+juhtID;
				ResultSet rs2 = stmt2.executeQuery(query2);
				
				if(rs2.next()){
					Statement stmt4 = con.createStatement();
					String query4 = "UPDATE projektikasutajad SET vastutaja=1 WHERE kasutaja_ID="+kasutajaID+" AND projekt_ID="+projektID+" AND juhtID="+juhtID;
					stmt4.executeUpdate(query4);
					
					try{stmt4.close();}catch(Exception x){}
				}
				else{
					Statement stmt4 = con.createStatement();
					String query4 = "INSERT INTO projektikasutajad (kasutaja_ID, projekt_ID,vastutaja,juhtID) VALUES ('"+kasutajaID+"', "+projektID+",1,"+juhtID+")";
					stmt4.executeUpdate(query4);
					
					try{stmt4.close();}catch(Exception x){}
				}
				
				try{rs2.close();stmt2.close();}catch(Exception x){}
			}
			else{
				return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}

			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum,juhtID) VALUES ("+projektID+","+"'"+kasutajanimi+" muutis projekti vastutajaks : "+kasutajaNimi+"',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
	}
	public String getKirjeldus() {
		return kirjeldus;
	}
	public void setKirjeldus(String kirjeldus) {
		this.kirjeldus = kirjeldus;
	}
	public List<Kasutaja> getKasutajad() {
		return kasutajad;
	}
	public void setKasutajad(List<Kasutaja> kasutajad) {
		this.kasutajad = kasutajad;
	}
	public List<Tulu> getTulud() {
		return tulud;
	}
	public void setTulud(List<Tulu> tulud) {
		this.tulud = tulud;
	}
	public List<Kulu> getKulud() {
		return kulud;
	}
	public void setKulud(List<Kulu> kulud) {
		this.kulud = kulud;
	}
	public List<Kommentaar> getKommentaarid() {
		return kommentaarid;
	}
	public void setKommentaarid(List<Kommentaar> kommentaarid) {
		
		List<Kommentaar> pööratudKommentaarid = new ArrayList<Kommentaar>();
		
		while(kommentaarid.size()>0){
			int index = 0, mitmes = 0;
			Kommentaar uusimKommentaar = kommentaarid.get(index);
			
			for(Kommentaar k : kommentaarid){
				if(k.getAeg().after(uusimKommentaar.getAeg())){
					uusimKommentaar = k;
					index = mitmes;
				}
				mitmes++;
			}
			pööratudKommentaarid.add(uusimKommentaar);
			kommentaarid.remove(index);
		}
		
		this.kommentaarid = pööratudKommentaarid;
	}
	public List<Logi> getLogi() {
		return logi;
	}
	public void setLogi(List<Logi> logi) {
		List<Logi> pööratudLogi = new ArrayList<Logi>();
		
		while(logi.size()>0){
			int index = 0, mitmes = 0;
			Logi uusimLogi = logi.get(index);
			
			for(Logi k : logi){
				if(k.getAeg().after(uusimLogi.getAeg())){
					uusimLogi = k;
					index = mitmes;
				}
				mitmes++;
			}
			pööratudLogi.add(uusimLogi);
			logi.remove(index);
		}
		
		this.logi = pööratudLogi;
	}
	public int getReiting() {
		return reiting;
	}
	public void setReiting(int reiting) {
		this.reiting = reiting;
	}

	public int getStaatusID() {
		return staatusID;
	}

	public void setStaatusID(int staatusID) {
		this.staatusID = staatusID;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReitinguHTML() {
		return reitinguHTML;
	}

	public void setReitinguHTML(String reitinguHTML) {
		this.reitinguHTML = reitinguHTML;
	}
	
	public int getProjektiJärjekorraNumber() {
		return projektiJärjekorraNumber;
	}

	public void setProjektiJärjekorraNumber(int järjekorraNumber) {
		this.projektiJärjekorraNumber = järjekorraNumber;
	}

	
}
