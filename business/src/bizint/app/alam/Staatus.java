package bizint.app.alam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bizint.andmebaas.Mysql;

public class Staatus {
	
	public static final String DEFAULT_NIMI = "uus staatus";
	public static final int DEFAULT_J�RJEKORRA_NUMBER = 0;
	public static final  int ERROR_JUBA_EKSISTEERIB = 0, VIGA_ANDMEBAASIGA_�HENDUMISEL = 1, K�IK_OKEI = 2, ERROR_STAATUS_POLE_T�HI = 3;
	
	private List<Projekt> projektid;
	private String nimi;
	private int j�rjekorraNumber;
	private int id;
	private String kustuta;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Staatus(){
		this.nimi = Staatus.DEFAULT_NIMI;
		this.projektid = new ArrayList<Projekt>();
		this.setJ�rjekorraNumber(Staatus.DEFAULT_J�RJEKORRA_NUMBER);
	}
	
	public Staatus(String nimi){
		this.nimi = nimi;
		this.projektid = new ArrayList<Projekt>();
		this.setJ�rjekorraNumber(Staatus.DEFAULT_J�RJEKORRA_NUMBER);
	}
	
	public Staatus(String nimi, int j�rjekorraNumber){
		this.nimi = nimi;
		this.projektid = new ArrayList<Projekt>();
		this.setJ�rjekorraNumber(j�rjekorraNumber);
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Double getBilanss(){
		return getKogutulu()-getKogukulu();
	}
	
	public static List<Staatus> paneJ�rjekorda(List<Staatus> staatused){
		
		List<Staatus> sorteeritudStaatused = new ArrayList<Staatus>();
		
		while(staatused.size() > 0 ){
			int v�ikseimNumber = staatused.get(0).getJ�rjekorraNumber();
			int index = 0;
			int i = 0;
			
			for(Staatus s : staatused){
				if(s.getJ�rjekorraNumber() < v�ikseimNumber){
					v�ikseimNumber = s.getJ�rjekorraNumber();
					index = i;
				}
				i++;
			}
			
			sorteeritudStaatused.add(staatused.get(index));
			staatused.remove(index);
		}
		
		return sorteeritudStaatused;
		
	}
	
	
	public void addProjekt(Projekt projekt){
		projektid.add(projekt);
	}
	
	public Double getKogutulu(){
		Double summa = 0.0;
		for(Projekt p : projektid){
			summa+=p.getKogutulu();
		}
		return summa;
	}
	
	public Double getKogukulu(){
		Double summa = 0.0;
		for(Projekt p : projektid){
			summa+=p.getKogukulu();
		}
		return summa;
	}
	
	public int getProjektideArv(){
		return projektid.size();
	}
	
	public static int kustutaStaatusAndmebaasist(int staatusID, int juhtID){
				
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "SELECT projektID FROM projektid WHERE staatus_ID="+staatusID+" AND juhtID="+juhtID+" LIMIT 1";
			ResultSet rs = stmt.executeQuery(query);

			if(rs.next()){
				if (con!=null) try {con.close();}catch (Exception ignore) {}
				return Staatus.ERROR_STAATUS_POLE_T�HI;
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try {
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT j�rjekorraNR AS jNR FROM staatused WHERE staatusID="+staatusID+" AND juhtID="+juhtID;
			ResultSet rs0 = stmt0.executeQuery(query0);
			
			rs0.next();
			int j�rjekorraNR = rs0.getInt("jNR");
			
			Statement stmt1 = con.createStatement();
			String query1 = "UPDATE staatused SET j�rjekorraNR=j�rjekorraNR - 1 WHERE j�rjekorraNR>"+j�rjekorraNR+" AND juhtID="+juhtID;
			stmt1.executeUpdate(query1);
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM staatused WHERE staatusID="+staatusID+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			try{stmt0.close();stmt1.close();stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}

		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.K�IK_OKEI;
	}
	
	public static int lisaStaatusAndmebaasi(Staatus staatus, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try {
			
			int j�rjekorraNR = 1;
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT MAX(j�rjekorraNR) AS max FROM staatused WHERE juhtID="+juhtID;
			ResultSet rs = stmt0.executeQuery(query0);
			
			if(rs.next()){
				j�rjekorraNR = rs.getInt("max") + 1;
			}
			
			Statement stmt = con.createStatement();
			String query = "INSERT INTO staatused (staatusNimi, j�rjekorraNR,juhtID) VALUES ('"+staatus.getNimi()+"',"+j�rjekorraNR+","+juhtID+")";
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.ERROR_JUBA_EKSISTEERIB;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Staatus.K�IK_OKEI;
	}
	
	public static int muudaStaatuseNimeAndmebaasis(Staatus staatus, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE staatused SET staatusNimi = '"+staatus.getNimi()+"' WHERE staatusID="+staatus.getId()+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Staatus.K�IK_OKEI;
	}
	
	public static int muudaStaatuseJ�rjekordaAndmebaasis( int staatuseJ�rjekorraNR, int staatusVanaID, int staatuseVanaJ�rjekorraNR, int juhtID){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		Statement stmt;
		try {
			if(staatuseVanaJ�rjekorraNR-staatuseJ�rjekorraNR<0){
				Statement stmt1 = con.createStatement();
				String query1 = "UPDATE staatused SET j�rjekorraNR=j�rjekorraNR +1 WHERE j�rjekorraNR>"+staatuseJ�rjekorraNR+" AND juhtID="+juhtID;
				stmt1.executeUpdate(query1);
				try{stmt1.close();}catch(Exception x){}

			
				Statement stmt2 = con.createStatement();
				String query2 = "UPDATE staatused SET j�rjekorraNR=j�rjekorraNR -1 WHERE j�rjekorraNR>"+staatuseVanaJ�rjekorraNR+" AND juhtID="+juhtID;
				stmt2.executeUpdate(query2);
			}
			else{
				Statement stmt1 = con.createStatement();
				String query1 = "UPDATE staatused SET j�rjekorraNR=j�rjekorraNR +1 WHERE j�rjekorraNR>="+staatuseJ�rjekorraNR+" AND juhtID="+juhtID;
				stmt1.executeUpdate(query1);
				try{stmt1.close();}catch(Exception x){}

			
				Statement stmt2 = con.createStatement();
				String query2 = "UPDATE staatused SET j�rjekorraNR=j�rjekorraNR -1 WHERE j�rjekorraNR>"+staatuseVanaJ�rjekorraNR+" AND juhtID="+juhtID;
				stmt2.executeUpdate(query2);
			}
			stmt = con.createStatement();
			String query = "UPDATE staatused SET j�rjekorraNR="+ staatuseJ�rjekorraNR +" WHERE staatusID=" + staatusVanaID+" AND juhtID="+juhtID;
			stmt.executeUpdate(query);
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		try {
			Statement stmt9 = con.createStatement();
			String query9 = "SELECT kasutajaNimi FROM juhid WHERE juhtID="+juhtID;
			ResultSet rs9 = stmt9.executeQuery(query9);
			rs9.next();
			String kasutajanimi = rs9.getString("kasutajaNimi");
			
			Statement stmt1 = con.createStatement();
			String query1 = "SELECT staatusNimi FROM staatused WHERE staatusID="+staatusVanaID+" AND juhtID="+juhtID;
			ResultSet rs = stmt1.executeQuery(query1);
			
			rs.next();
			
			String staatusnimi = rs.getString("staatusNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (staatusVanaID, sonum,juhtID) VALUES ("+staatusVanaID+","+"'"+kasutajanimi+" muutis staatuse j�rjekorda : "+staatusnimi+"',"+juhtID+")";
			stmt2.executeUpdate(query2);
			
			try{rs.close();stmt.close();}catch(Exception x){}
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.K�IK_OKEI;
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public List<Projekt> getProjektid(){
		return projektid;
	}
	
	public void setProjektid(List<Projekt> projektid){
		this.projektid = projektid;
	}

	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public int getJ�rjekorraNumber() {
		return j�rjekorraNumber;
	}

	public void setJ�rjekorraNumber(int j�rjekorraNumber) {
		this.j�rjekorraNumber = j�rjekorraNumber;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKustuta() {
		return kustuta;
	}

	public void setKustuta(String kustuta) {
		this.kustuta = kustuta;
	}

}
