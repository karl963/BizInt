package bizint.app.alam;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bizint.andmebaas.Mysql;

public class Staatus {
	
	public static final String DEFAULT_NIMI = "lahe staatus";
	public static final int DEFAULT_JÄRJEKORRA_NUMBER = 0;
	public static final  int ERROR_JUBA_EKSISTEERIB = 0, VIGA_ANDMEBAASIGA_ÜHENDUMISEL = 1, KÕIK_OKEI = 2;
	
	private List<Projekt> projektid;
	private String nimi;
	private int järjekorraNumber;
	private int id;
	
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
	
	public static int kustutaStaatusAndmebaasist(int staatusID){
		
		int[] projektid = null;
				
		Connection con = Mysql.connection;
		if(con==null){
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "SELECT COUNT(*) AS mituProjekti FROM staatused WHERE staatusID="+staatusID;
			ResultSet rs = stmt.executeQuery(query);
			
			rs.next();
			
			projektid = new int[rs.getInt("mituProjekti")];
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "SELECT projekt_ID FROM staatused WHERE staatusID="+staatusID;
			ResultSet rs = stmt.executeQuery(query);
			
			int i = 0;
			while(rs.next()){
				projektid[i] = rs.getInt("projekt_ID");
				i++;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM staatused WHERE staatusID="+staatusID;
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			for(int projektID : projektid){
				Statement stmt0 = con.createStatement();
				String query0 = "DELETE FROM projektid WHERE projektID="+projektID;
				stmt0.executeUpdate(query0);

				Statement stmt = con.createStatement();
				String query = "DELETE FROM projektikasutajad WHERE projekt_ID="+projektID;
				stmt.executeUpdate(query);
				
				Statement stmt2 = con.createStatement();
				String query2 = "DELETE FROM tulud WHERE projekt_ID="+projektID;
				stmt2.executeUpdate(query2);
				
				Statement stmt3 = con.createStatement();
				String query3 = "DELETE FROM logid WHERE projekt_ID="+projektID;
				stmt3.executeUpdate(query3);
				
				Statement stmt4 = con.createStatement();
				String query4 = "DELETE FROM kulud WHERE projekt_ID="+projektID;
				stmt4.executeUpdate(query4);
				
				Statement stmt5 = con.createStatement();
				String query5 = "DELETE FROM kommentaarid WHERE projekt_ID="+projektID;
				stmt5.executeUpdate(query5);
			}
		} catch (SQLException e) {
			return Projekt.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaStaatusAndmebaasi(Staatus staatus){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "INSERT INTO staatused (staatusNimi) VALUES ('"+staatus.getNimi()+"')";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Staatus.ERROR_JUBA_EKSISTEERIB;
		}
		
		return Staatus.KÕIK_OKEI;
	}
	
	public static boolean muudaStaatuseNimeAndmebaasis(Staatus vanaStaatus, Staatus uusStaatus){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
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

}
