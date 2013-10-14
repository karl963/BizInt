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

		try{

			Statement stmt = con.createStatement();
			String query = "SELECT töötab FROM kasutajad WHERE kasutajaNimi='"+kasutaja.getKasutajaNimi()+"'";
			ResultSet rs = stmt.executeQuery(query);
			
			// kui kasutaja selle nimega on olemas, aga ta on kustutatud vahepeal töölisete seast, siis paneme ta sinna tagasi
			
			if(rs.next()){
				if(rs.getBoolean("töötab")){
					return Kasutaja.VIGA_JUBA_EKSISTEERIB;
				}
				else{
					String query2 = "UPDATE kasutajad SET töötab=1 WHERE  kasutajaNimi = '"+kasutaja.getKasutajaNimi()+"'";
					
					try {
						Statement stmt2 = con.createStatement();
						stmt2.executeUpdate(query2);
					} catch (SQLException e) {
						return Kasutaja.VIGA_JUBA_EKSISTEERIB;
					}
				}
			}
			else{
				String query2 = "INSERT INTO kasutajad (kasutajaNimi) VALUES ('"+kasutaja.getKasutajaNimi()+"')";
				
				try {
					Statement stmt2 = con.createStatement();
					stmt2.executeUpdate(query2);
				} catch (SQLException e) {
					return Kasutaja.VIGA_JUBA_EKSISTEERIB;
				}
			}

		}catch(Exception x){
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		return Kasutaja.KÕIK_OKEI;
	}
	
	public static int muudaKasutajaTöötuksAndmebaasis(Kasutaja kasutaja){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "UPDATE kasutajad SET töötab=0 WHERE kasutajaID = "+kasutaja.getKasutajaID();
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
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
