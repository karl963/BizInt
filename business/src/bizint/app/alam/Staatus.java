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
				return Staatus.ERROR_STAATUS_POLE_TÜHI;
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		try {
			
			Statement stmt = con.createStatement();
			String query = "DELETE FROM staatused WHERE staatusID="+staatusID;
			stmt.executeUpdate(query);
			
		} catch (SQLException e) {
			e.printStackTrace();
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}

		
		return Projekt.KÕIK_OKEI;
	}
	
	public static int lisaStaatusAndmebaasi(Staatus staatus){
		
		Connection con = new Mysql().getConnection();
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
	
	public static int muudaStaatuseNimeAndmebaasis(Staatus staatus){
		
		Connection con = new Mysql().getConnection();
		if(con==null){
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		Statement stmt;
		try {
			stmt = con.createStatement();
		} catch (SQLException e) {
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		String query = "UPDATE staatused SET staatusNimi = '"+staatus.getNimi()+"' WHERE staatusID="+staatus.getId();
		
		try {
			stmt.executeUpdate(query);
		} catch (SQLException e) {
			return Staatus.VIGA_ANDMEBAASIGA_ÜHENDUMISEL;
		}
		
		return Staatus.KÕIK_OKEI;
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
