package bizint.app.alam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.ArrayList;
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
	public static final int ERROR_JUBA_EKSISTEERIB = 0, VIGA_ANDMEBAASIGA_ÜHENDUMISEL = 1, KÕIK_OKEI = 2;
	
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
	
	public static int lisaProjektAndmebaasi(Projekt projekt,int staatusID){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "INSERT INTO projektid (projektNimi, staatus_ID) VALUES ('"+projekt.getNimi()+"',"+staatusID+")";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+projekt.getId()+","+"'Projekt loodi kasutaja "+"Kasutaja"+" poolt')";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiKirjeldusAndmebaasis(UusKirjeldus uusKirjeldus){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "UPDATE projektid SET kirjeldus = '"+uusKirjeldus.getKirjeldus()+"' WHERE projektID="+uusKirjeldus.getProjektID();
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+uusKirjeldus.getProjektID()+","+"'"+"Kasutaja"+" muutis projekti kirjeldust')";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaUusKasutajaAndmebaasi(UusKasutaja uusKasutaja){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "INSERT INTO projektikasutajad (kasutaja_ID, projekt_ID) VALUES ((SELECT kasutajaID FROM kasutajad WHERE kasutajaNimi='"+uusKasutaja.getKasutajaNimi()+"'),"+uusKasutaja.getProjektID()+")";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+uusKasutaja.getProjektID()+","+"'"+"Kasutaja"+" määras "+uusKasutaja.getKasutajaNimi()+" projekti töötajaks')";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static boolean muudaKasutajaKuupalkaAndmebaasis(Projekt projekt, Kasutaja kasutaja, Double Kuupalk, Date aeg){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static int muudaKasutajateAndmeidProjektigaAndmebaasis(String kasutajad, int projektID){
		
		Connection con = Mysql.connection;
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
			} catch (SQLException e) {
				return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
			}
			
			String query = "UPDATE projektikasutajad SET aktiivne="+aktiivne+", osalus="+osalus+",vastutaja="+vastutaja+" WHERE projekt_ID="+projektID+" AND kasutaja_ID="+kasutajaID;
			
			try {
				stmt.executeUpdate(query);
			} catch (SQLException e) {
				e.printStackTrace();
				return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
			}
			
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+projektID+","+"'"+"Kasutaja"+" muutis töötajate andmeid projektis";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static boolean muudaProjektiStaatustAndmebaasis(Projekt projekt, Staatus vanaStaatus, Staatus uusStaatus){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static int muudaProjektiNimeAndmebaasis(UusProjektiNimi nimi){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "UPDATE projektid SET projektNimi = '"+nimi.getUusNimi()+"' WHERE projektID="+nimi.getProjektID();
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+nimi.getProjektID()+","+"'"+"Kasutaja"+" määras projektile uue nime : "+nimi.getUusNimi()+"')";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int muudaProjektiReitingutAndmebaasis(int projektID, int reiting){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "UPDATE projektid SET reiting = "+reiting+" WHERE projektID="+projektID;
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+projektID+","+"'"+"Kasutaja"+" muutis projekti reitingut : "+reiting+"')";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static boolean lisaLogiAndmebaasi(Projekt projekt, Logi logi){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static int lisaKommentaarAndmebaasi(UusKommentaar uusKommentaar){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "INSERT INTO kommentaarid (sonum, projekt_ID, kasutaja_ID) VALUES ('"+uusKommentaar.getSonum()+"',"+uusKommentaar.getProjektID()+",1)";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		return Projekt.KÕIK_OKEI;
	}

	public static int lisaKuluAndmebaasi(Kulu kulu){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		Timestamp aeg = new Timestamp(kulu.getAeg().getTime());
		
		String query = "INSERT INTO kulud (kulu, aeg, kuluNimi, projekt_ID) "
				+ "VALUES ("+kulu.getSumma()+",'"+aeg+"','"+kulu.getKuluNimi()+"',"+kulu.getProjektID()+")";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2;
			if(kulu.getKuluNimi().equals(" ") || kulu.getKuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+kulu.getProjektID()+","+"'"+"Kasutaja"+" lisas projektile uue kulu : "+kulu.getSumma()+"')";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+kulu.getProjektID()+","+"'"+"Kasutaja"+" lisas projektile uue kulu : "+kulu.getKuluNimi()+" ("+kulu.getSumma()+")')";

			}
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
		
	}
	public static int lisaTuluAndmebaasi(Tulu tulu){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		Timestamp aeg = new Timestamp(tulu.getAeg().getTime());
		
		String query = "INSERT INTO tulud (tulu, aeg, tuluNimi, projekt_ID) "
				+ "VALUES ("+tulu.getSumma()+",'"+aeg+"','"+tulu.getTuluNimi()+"',"+tulu.getProjektID()+")";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2;
			if(tulu.getTuluNimi().equals(" ") || tulu.getTuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+tulu.getProjektID()+","+"'"+"Kasutaja"+" lisas projektile uue tulu : "+tulu.getSumma()+"')";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+tulu.getProjektID()+","+"'"+"Kasutaja"+" lisas projektile uue tulu : "+tulu.getTuluNimi()+" ("+tulu.getSumma()+")')";

			}
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int kustutaTuluAndmebaasist(Tulu tulu){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		Timestamp aeg = null;
		String query = null;
		
		try {
			
			aeg = new Timestamp(Tulu.AJAFORMAAT.parse(tulu.getStringAeg()).getTime());
			query = "DELETE FROM tulud WHERE tulu="+tulu.getSumma()
					+ " AND aeg='"+aeg
					+ "' AND tuluNimi='"+tulu.getTuluNimi()
					+ "' AND projekt_ID="+tulu.getProjektID()
					+" LIMIT 1";

		} catch (ParseException e1) {
			
			query = "DELETE FROM tulud WHERE tulu="+tulu.getSumma()
					+ " AND tuluNimi='"+tulu.getTuluNimi()
					+ "' AND projekt_ID="+tulu.getProjektID()
					+" LIMIT 1";
		}
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2;
			if(tulu.getTuluNimi().equals(" ") || tulu.getTuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+tulu.getProjektID()+","+"'"+"Kasutaja"+" eemaldas projektist tulu : "+tulu.getSumma()+"')";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+tulu.getProjektID()+","+"'"+"Kasutaja"+" eemaldas projektist tulu : "+tulu.getTuluNimi()+" ("+tulu.getSumma()+")')";

			}
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int kustutaKuluAndmebaasist(Kulu kulu){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
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
					+" LIMIT 1";
			
		} catch (ParseException e1) {

			query = "DELETE FROM kulud WHERE kulu="+kulu.getSumma()
					+ " AND kuluNimi='"+kulu.getKuluNimi()
					+ "' AND projekt_ID="+kulu.getProjektID()
					+" LIMIT 1";
		}
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2;
			if(kulu.getKuluNimi().equals(" ") || kulu.getKuluNimi().equals("")){
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+kulu.getProjektID()+","+"'"+"Kasutaja"+" eemaldas projektist kulu : "+kulu.getSumma()+"')";

			}
			else{
				query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+kulu.getProjektID()+","+"'"+"Kasutaja"+" eemaldas projektist kulu : "+kulu.getKuluNimi()+" ("+kulu.getSumma()+")')";

			}
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int kustutaProjektAndmebaasist(int id){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM projektid WHERE projektID="+id;
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM projektikasutajad WHERE projekt_ID="+id;
			stmt.executeUpdate(query);
			
			Statement stmt2 = con.createStatement();
			String query2 = "DELETE FROM tulud WHERE projekt_ID="+id;
			stmt2.executeUpdate(query2);
			
			Statement stmt3 = con.createStatement();
			String query3 = "DELETE FROM logid WHERE projekt_ID="+id;
			stmt3.executeUpdate(query3);
			
			Statement stmt4 = con.createStatement();
			String query4 = "DELETE FROM kulud WHERE projekt_ID="+id;
			stmt4.executeUpdate(query4);
			
			Statement stmt5 = con.createStatement();
			String query5 = "DELETE FROM kommentaarid WHERE projekt_ID="+id;
			stmt5.executeUpdate(query5);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int eemaldaKasutajaProjektistAndmebaasis(int kasutajaID, int projektID){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "DELETE FROM projektikasutajad WHERE projekt_ID="+projektID+" AND kasutaja_ID="+kasutajaID+" LIMIT 1";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi FROM kasutajad WHERE kasutajaID="+kasutajaID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			String nimi = rs.getString("kasutajaNimi");
			
			Statement stmt3 = con.createStatement();
			String query3 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+projektID+","+"'"+"Kasutaja"+" eemaldas projektist töötaja : "+nimi+"')";
			stmt3.executeUpdate(query3);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaKasutajaProjektiAndmebaasis(String kasutajaNimi, int projektID){

		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "INSERT INTO projektikasutajad (kasutaja_ID, projekt_ID) VALUES ((SELECT kasutajaID FROM kasutajad WHERE kasutajaNimi='"+kasutajaNimi+"'), "+projektID+")";
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+projektID+","+"'"+"Kasutaja"+" lisas projekti töötaja : "+kasutajaNimi+"')";
			stmt2.executeUpdate(query2);
		} catch (SQLException e) {
		}
		
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
	
}
