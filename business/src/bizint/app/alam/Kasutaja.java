package bizint.app.alam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import bizint.andmebaas.Mysql;
import bizint.app.alam.kuuAndmed.TabeliData;
import bizint.post.UusKasutaja;

public class Kasutaja {
	
	public static String DEFAULT_NIMI = "nimetu";
	public static Double DEFAULT_OSALUS = 0.0;
	public static boolean DEFAULT_VASTUTAJA = false;
	public static boolean DEFAULT_AKTIIVNE = false;
	public static int VIGA_ANDMEBAASIGA_�HENDUMISEL = 0, VIGA_JUBA_EKSISTEERIB = 1, K�IK_OKEI = 2;
	
	private String kasutajaNimi;
	private Date aeg;
	private Double osalus;
	private boolean vastutaja, aktiivne;
	private int projektID,kasutajaID;
	private TabeliData tabeliAndmed;

	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kasutaja(){
		this.kasutajaNimi = Kasutaja.DEFAULT_NIMI;
		this.aeg = new Date();
		this.osalus = Kasutaja.DEFAULT_OSALUS;
		this.vastutaja = Kasutaja.DEFAULT_VASTUTAJA;
		this.aktiivne = Kasutaja.DEFAULT_AKTIIVNE;
		this.setProjektID(0);
		this.setKasutajaID(0);
	}
	
	public Kasutaja(String nimi){
		this.kasutajaNimi = nimi;
		this.aeg = new Date();
		this.osalus = Kasutaja.DEFAULT_OSALUS;
		this.vastutaja = Kasutaja.DEFAULT_VASTUTAJA;
		this.aktiivne = Kasutaja.DEFAULT_AKTIIVNE;
		this.setProjektID(0);
		this.setKasutajaID(0);
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public static int muudaKasutajaNimeAndmebaasis(int kasutajaID, String uusNimi){

		Connection con = new Mysql().getConnection();
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try{
			
			Statement stmt = con.createStatement();
			String query = "UPDATE kasutajad SET kasutajaNimi='"+uusNimi+"' WHERE kasutajaID="+kasutajaID;
			stmt.executeUpdate(query);

			try{stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Kasutaja.VIGA_JUBA_EKSISTEERIB;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Kasutaja.K�IK_OKEI;
	}
	
	public static int lisaKasutajaAndmebaasi(Kasutaja kasutaja){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}

		try{

			Statement stmt = con.createStatement();
			String query = "SELECT t��tab FROM kasutajad WHERE kasutajaNimi='"+kasutaja.getKasutajaNimi()+"'";
			ResultSet rs = stmt.executeQuery(query);
			
			// kui kasutaja selle nimega on olemas, aga ta on kustutatud vahepeal t��lisete seast, siis paneme ta sinna tagasi
			
			if(rs.next()){
				if(rs.getBoolean("t��tab")){
					if (con!=null) try {con.close();}catch (Exception ignore) {}
					return Kasutaja.VIGA_JUBA_EKSISTEERIB;
				}
				else{
					String query2 = "UPDATE kasutajad SET t��tab=1 WHERE  kasutajaNimi = '"+kasutaja.getKasutajaNimi()+"'";
					
					try {
						Statement stmt2 = con.createStatement();
						stmt2.executeUpdate(query2);
						try{stmt2.close();}catch(Exception x){}
					} catch (SQLException e) {
						if (con!=null) try {con.close();}catch (Exception ignore) {}
						return Kasutaja.VIGA_JUBA_EKSISTEERIB;
					}
				}
			}
			else{
				String query2 = "INSERT INTO kasutajad (kasutajaNimi) VALUES ('"+kasutaja.getKasutajaNimi()+"')";
				
				try {
					Statement stmt2 = con.createStatement();
					stmt2.executeUpdate(query2);
					try{stmt2.close();}catch(Exception x){}
				} catch (SQLException e) {
					return Kasutaja.VIGA_JUBA_EKSISTEERIB;
				}
			}

			try{rs.close();stmt.close();}catch(Exception x){}
			
		}catch(Exception x){
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Kasutaja.K�IK_OKEI;
	}
	
	public static int muudaKasutajaT��tuksAndmebaasis(Kasutaja kasutaja){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try {
			Statement stmt = con.createStatement();
			String query = "UPDATE kasutajad SET t��tab=0 WHERE kasutajaID = "+kasutaja.getKasutajaID();
			stmt.executeUpdate(query);
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Kasutaja.K�IK_OKEI;
	}
	
	public static int muudaKasutajatePalkasidAndmebaasis(List<Kasutaja> kasutajad,int aasta){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try {
			
			for(Kasutaja k : kasutajad){
				
				Statement stmt0 = con.createStatement();
				String query0 = "SELECT kasutajaID FROM kasutajad WHERE kasutajaNimi = '"+k.getKasutajaNimi()+"'";
				ResultSet rs0 = stmt0.executeQuery(query0);
				
				rs0.next();
				
				k.setKasutajaID(rs0.getInt("kasutajaID"));
				
				try{rs0.close();stmt0.close();}catch(Exception x){}
				
				for(int i = 0;i<3;i++){
					
					Double palk = k.getTabeliAndmed().getPalgad()[i];
					int kuu = k.getTabeliAndmed().getKuud()[i];
					int p�ev = k.getTabeliAndmed().getPalgaKuup�evad()[i];
					
					Statement stmt = con.createStatement();
					String query = "SELECT palk FROM palgad WHERE kasutaja_ID = "+k.getKasutajaID()+" AND kuu="+kuu+" AND aasta="+aasta;
					ResultSet rs = stmt.executeQuery(query);
					
					if(rs.next()){ // kui meil oli juba sissekanne, siis uuendame
						Statement stmt2 = con.createStatement();
						String query2 = "UPDATE palgad SET palk="+palk+", p�ev="+p�ev+" WHERE kuu="+kuu+" AND aasta="+aasta+" AND kasutaja_ID = "+k.getKasutajaID();
						stmt2.executeUpdate(query2);
						try{stmt2.close();}catch(Exception x){}
					}
					else{ // kui palka polnud lisatud, siis lisame
						Statement stmt2 = con.createStatement();
						String query2 = "INSERT INTO palgad (kasutaja_ID,palk,kuu,aasta,p�ev) VALUES ("+k.getKasutajaID()+","+palk+","+kuu+","+aasta+","+p�ev+")";
						stmt2.executeUpdate(query2);
						try{stmt2.close();}catch(Exception x){}
					}
					
					try{rs.close();stmt.close();}catch(Exception x){}
					
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Kasutaja.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Kasutaja.K�IK_OKEI;
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

	public TabeliData getTabeliAndmed() {
		return tabeliAndmed;
	}

	public void setTabeliAndmed(TabeliData tabeliAndmed) {
		this.tabeliAndmed = tabeliAndmed;
	}
	
}
