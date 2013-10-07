package bizint.app.alam;

import java.util.ArrayList;
import java.util.List;

public class Staatus {
	
	public static String DEFAULT_NIMI = "lahe staatus";
	public static int DEFAULT_J�RJEKORRA_NUMBER = 0;
	
	private List<Projekt> projektid;
	private String nimi;
	private int j�rjekorraNumber;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Staatus(){
		this.nimi = Staatus.DEFAULT_NIMI;
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
	
	public static boolean lisaStaatusAndmebaasi(Staatus staatus){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
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

}
