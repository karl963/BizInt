package bizint.app.alam;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import bizint.andmebaas.Mysql;

public class Staatus {
	
	public static final String DEFAULT_NIMI = "lahe staatus";
	public static final int DEFAULT_J�RJEKORRA_NUMBER = 0;
	public static final  int ERROR_JUBA_EKSISTEERIB = 0, VIGA_ANDMEBAASIGA_�HENDUMISEL = 1, K�IK_OKEI = 2;
	
	private List<Projekt> projektid;
	private String nimi;
	private int j�rjekorraNumber;
	private int id;
	
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
	
	public static boolean kustutaStaatusAndmebaasist(Staatus staatus){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static int lisaStaatusAndmebaasi(Staatus staatus){
		
		Connection con = Mysql.connection;
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Staatus.VIGA_ANDMEBAASIGA_�HENDUMISEL;
		}
		
		String query = "INSERT INTO staatused (staatusNimi) VALUES ('"+staatus.getNimi()+"')";
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Staatus.ERROR_JUBA_EKSISTEERIB;
		}
		
		return Staatus.K�IK_OKEI;
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

}
