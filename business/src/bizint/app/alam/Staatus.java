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
	public static final int DEFAULT_JÄRJEKORRA_NUMBER = 0;
	public static final  int ERROR_JUBA_EKSISTEERIB = 0, VIGA_ANDMEBAASIGA_ÜHENDUMISEL = 1, KÕIK_OKEI = 2, ERROR_STAATUS_POLE_TÜHI = 3;
	
	private List<Projekt> projektid;
	private String nimi;
	private int järjekorraNumber;
	private int id;
	private String kustuta;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Staatus(){
		this.nimi = Staatus.DEFAULT_NIMI;
		this.projektid = new ArrayList<Projekt>();
		this.setJärjekorraNumber(Staatus.DEFAULT_JÄRJEKORRA_NUMBER);
	}
	
	public Staatus(String nimi){
		this.nimi = nimi;
		this.projektid = new ArrayList<Projekt>();
		this.setJärjekorraNumber(Staatus.DEFAULT_JÄRJEKORRA_NUMBER);
	}
	
	public Staatus(String nimi, int järjekorraNumber){
		this.nimi = nimi;
		this.projektid = new ArrayList<Projekt>();
		this.setJärjekorraNumber(järjekorraNumber);
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Double getBilanss(){
		return getKogutulu()-getKogukulu();
	}
	
	public static List<Staatus> paneJärjekorda(List<Staatus> staatused){
		
		List<Staatus> sorteeritudStaatused = new ArrayList<Staatus>();
		
		while(staatused.size() > 0 ){
			int väikseimNumber = staatused.get(0).getJärjekorraNumber();
			int index = 0;
			int i = 0;
			
			for(Staatus s : staatused){
				if(s.getJärjekorraNumber() < väikseimNumber){
					väikseimNumber = s.getJärjekorraNumber();
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
	
	public static int kustutaStaatusAndmebaasist(int staatusID){
				
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "SELECT projektID FROM projektid WHERE staatus_ID="+staatusID+" LIMIT 1";
			ResultSet rs = stmt.executeQuery(query);

			if(rs.next()){
				if (con!=null) try {con.close();}catch (Exception ignore) {}
				return Staatus.ERROR_STAATUS_POLE_TÜHI;
			}
			
			try{rs.close();stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT järjekorraNR AS jNR FROM staatused WHERE staatusID="+staatusID;
			ResultSet rs0 = stmt0.executeQuery(query0);
			
			rs0.next();
			int järjekorraNR = rs0.getInt("jNR");
			
			Statement stmt1 = con.createStatement();
			String query1 = "UPDATE staatused SET järjekorraNR=järjekorraNR - 1 WHERE järjekorraNR>"+järjekorraNR;
			stmt1.executeUpdate(query1);
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM staatused WHERE staatusID="+staatusID;
			stmt.executeUpdate(query);
			
			try{stmt0.close();stmt1.close();stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}

		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaStaatusAndmebaasi(Staatus staatus){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			int järjekorraNR = 1;
			
			Statement stmt0 = con.createStatement();
			String query0 = "SELECT MAX(järjekorraNR) AS max FROM staatused";
			ResultSet rs = stmt0.executeQuery(query0);
			
			if(rs.next()){
				järjekorraNR = rs.getInt("max") + 1;
			}
			
			Statement stmt = con.createStatement();
			String query = "INSERT INTO staatused (staatusNimi, järjekorraNR) VALUES ('"+staatus.getNimi()+"',"+järjekorraNR+")";
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			e.printStackTrace();
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.ERROR_JUBA_EKSISTEERIB;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Staatus.KÕIK_OKEI;
	}
	
	public static int muudaStaatuseNimeAndmebaasis(Staatus staatus){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
			String query = "UPDATE staatused SET staatusNimi = '"+staatus.getNimi()+"' WHERE staatusID="+staatus.getId();
			stmt.executeUpdate(query);
			
			try{stmt.close();}catch(Exception x){}
		} catch (SQLException e) {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Staatus.KÕIK_OKEI;
	}
	
	public static int muudaStaatuseJärjekordaAndmebaasis(int staatusID, int staatuseJärjekorraNR, int staatusVanaID, int staatuseVanaJärjekorraNR){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			if(staatuseJärjekorraNR < 0){
				staatuseJärjekorraNR = 1;
				Statement stmt3 = con.createStatement();
				String query3 = "SELECT MAX(järjekorraNR) AS max FROM staatused";
				ResultSet rs = stmt3.executeQuery(query3);
				rs.next();
				staatuseJärjekorraNR = rs.getInt("max")+1;
				try{stmt3.close();}catch(Exception x){}
			}
			
			if(staatusID == staatusVanaID){
				if(staatuseJärjekorraNR != 1 && staatuseVanaJärjekorraNR-staatuseJärjekorraNR<0){
					staatuseJärjekorraNR -= 1;
					Statement stmt1 = con.createStatement();
					String query1 = "UPDATE staatused SET järjekorraNR=staatuseJärjekorraNR +1 WHERE järjekorraNR>"+staatuseJärjekorraNR;
					stmt1.executeUpdate(query1);
					try{stmt1.close();}catch(Exception x){}
				}
				else{
					Statement stmt1 = con.createStatement();
					String query1 = "UPDATE staatused SET järjekorraNR=staatuseJärjekorraNR +1 WHERE järjekorraNR>="+staatuseJärjekorraNR;
					stmt1.executeUpdate(query1);
					try{stmt1.close();}catch(Exception x){}
				}
			}
			else{
				Statement stmt1 = con.createStatement();
				String query1 = "UPDATE staatused SET järjekorraNR=järjekorraNR +1 WHERE järjekorraNR>="+staatuseJärjekorraNR;
				stmt1.executeUpdate(query1);
				try{stmt1.close();}catch(Exception x){}
			}
			
			Statement stmt2 = con.createStatement();
			String query2 = "UPDATE staatused SET järjekorraNR=järjekorraNR -1 WHERE järjekorraNR>"+staatuseVanaJärjekorraNR;
			stmt2.executeUpdate(query2);
			
			if(staatuseVanaJärjekorraNR-staatuseJärjekorraNR<0){
				staatuseJärjekorraNR -= 1;
				stmt = con.createStatement();
				String query = "UPDATE staatused SET järjekorraNR="+ staatuseJärjekorraNR +" WHERE staatusID=" + staatusVanaID;
				stmt.executeUpdate(query);
				try{stmt.close();}catch(Exception x){}
			}
			else{
				stmt = con.createStatement();
				String query = "UPDATE staatused SET järjekorraNR="+ staatuseJärjekorraNR +" WHERE staatusID=" + staatusVanaID;
				stmt.executeUpdate(query);
				try{stmt.close();}catch(Exception x){}
			}
			
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
			if (con!=null) try {con.close();}catch (Exception ignore) {}
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			Statement stmt1 = con.createStatement();
			String query1 = "SELECT staatusNimi FROM staatused WHERE staatusID="+staatusVanaID;
			ResultSet rs = stmt1.executeQuery(query1);
			
			rs.next();
			
			String staatusnimi = rs.getString("staatusNimi");
			
			Statement stmt2 = con.createStatement();
			String query2 = "INSERT INTO logid (staatusID, sonum) VALUES ("+staatuseVanaJärjekorraNR+","+"'"+"Kasutaja"+" muutis staatuse järjekorda : "+staatusnimi+"')";
			stmt2.executeUpdate(query2);
			
			try{rs.close();stmt.close();}catch(Exception x){}
			try{stmt2.close();}catch(Exception x){}
		} catch (SQLException e) {
		}
		
		if (con!=null) try {con.close();}catch (Exception ignore) {}
		return Projekt.KÕIK_OKEI;
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

	public int getJärjekorraNumber() {
		return järjekorraNumber;
	}

	public void setJärjekorraNumber(int järjekorraNumber) {
		this.järjekorraNumber = järjekorraNumber;
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
