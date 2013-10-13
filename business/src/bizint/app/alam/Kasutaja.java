package bizint.app.alam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import bizint.andmebaas.Mysql;
import bizint.post.UusKasutaja;

public class Kasutaja {
	
	public static String DEFAULT_NIMI = "nimetu";
	public static Double DEFAULT_KUUPALK = 0.0;
	public static Double DEFAULT_OSALUS = 0.0;
	public static boolean DEFAULT_VASTUTAJA = false;
	public static boolean DEFAULT_AKTIIVNE = false;
	public static int VIGA_ANDMEBAASIGA_ÜHENDUMISEL = 0, VIGA_JUBA_EKSISTEERIB = 1, KÕIK_OKEI = 2;
	
	private String kasutajaNimi;
	private Date aeg;
	private Double kuupalk, osalus;
	private boolean vastutaja, aktiivne;
	private int projektID,kasutajaID;

	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kasutaja(){
		this.kasutajaNimi = Kasutaja.DEFAULT_NIMI;
		this.aeg = new Date();
		this.kuupalk = Kasutaja.DEFAULT_KUUPALK;
		this.osalus = Kasutaja.DEFAULT_OSALUS;
		this.vastutaja = Kasutaja.DEFAULT_VASTUTAJA;
		this.aktiivne = Kasutaja.DEFAULT_AKTIIVNE;
		this.setProjektID(0);
		this.setKasutajaID(0);
	}
	
	public Kasutaja(String nimi){
		this.kasutajaNimi = nimi;
		this.aeg = new Date();
		this.kuupalk = Kasutaja.DEFAULT_KUUPALK;
		this.osalus = Kasutaja.DEFAULT_OSALUS;
		this.vastutaja = Kasutaja.DEFAULT_VASTUTAJA;
		this.aktiivne = Kasutaja.DEFAULT_AKTIIVNE;
		this.setProjektID(0);
		this.setKasutajaID(0);
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public static int lisaKasutajaAndmebaasi(Kasutaja kasutaja){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "INSERT INTO kasutajad (kasutajaNimi) VALUES ('"+kasutaja.getKasutajaNimi()+"')";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Kasutaja.VIGA_JUBA_EKSISTEERIB;
		}
		
		return Kasutaja.KÕIK_OKEI;
	}
	
	public static int kustutaKasutajaAndmebaasist(Kasutaja kasutaja){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String kasutajaNimi = "";
		ResultSet logile;
		
		try {
			Statement stmt = con.createStatement();
			String query = "SELECT kasutajaNimi FROM kasutajad WHERE kasutajaID="+kasutaja.getKasutajaID();
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			kasutajaNimi = rs.getString("kasutajaNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "SELECT projekt_ID FROM projektikasutajad WHERE kasutaja_ID = "+kasutaja.getKasutajaID();
			logile = stmt2.executeQuery(query2);
			
		}catch(Exception c){
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "DELETE FROM kasutajad WHERE kasutajaID = "+kasutaja.getKasutajaID();
			stmt.executeUpdate(query);
			
			Statement stmt2 = con.createStatement();
			String query2 = "DELETE FROM projektikasutajad WHERE kasutaja_ID = "+kasutaja.getKasutajaID();
			stmt2.executeUpdate(query2);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try{
			while(logile.next()){
				int projektID = logile.getInt("projekt_ID");

				try {
					Statement stmt3 = con.createStatement();
					String query3 = "INSERT INTO logid (projekt_ID, sonum) VALUES ("+projektID+","+"'"+"Kasutaja"+" eemaldas kustutaks töötaja, "+kasutajaNimi+", ning ta eemaldati automaatselt projektist')";
					stmt3.executeUpdate(query3);
				} catch (SQLException e) {
					e.printStackTrace();
					return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
				}
			}

		}catch(Exception x){
			x.printStackTrace();
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		return Kasutaja.KÕIK_OKEI;
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\

	public String getKasutajaNimi() {
		return kasutajaNimi;
	}
	public void setKasutajaNimi(String nimi) {
		this.kasutajaNimi = nimi;
	}
	public Date getAeg() {
		return aeg;
	}
	public void setAeg(Date aeg) {
		this.aeg = aeg;
	}
	public Double getKuupalk() {
		return kuupalk;
	}
	public void setKuupalk(Double kuupalk) {
		this.kuupalk = kuupalk;
	}
	public Double getOsalus() {
		return osalus;
	}
	public void setOsalus(Double osalus) {
		this.osalus = osalus;
	}

	public boolean isVastutaja() {
		return vastutaja;
	}

	public void setVastutaja(boolean vastutaja) {
		this.vastutaja = vastutaja;
	}

	public boolean isAktiivne() {
		return aktiivne;
	}

	public void setAktiivne(boolean aktiivne) {
		this.aktiivne = aktiivne;
	}

	public int getKasutajaID() {
		return kasutajaID;
	}

	public void setKasutajaID(int kasutajaID) {
		this.kasutajaID = kasutajaID;
	}

	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}
	
}
