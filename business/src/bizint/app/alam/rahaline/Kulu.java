package bizint.app.alam.rahaline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kulu {
	
	private static String DEFAULT_NIMI = "";
	private static Double DEFAULT_SUMMA = 0.0;
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private String kuluNimi;
	private Double summa;
	private Date aeg;
	private int projektID;
	private String stringAeg;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\

	public Kulu(){
		this.kuluNimi = Kulu.DEFAULT_NIMI;
		this.summa = Kulu.DEFAULT_SUMMA;
		this.aeg = new Date();
		this.projektID = 0;
		stringAeg = Kulu.AJAFORMAAT.format(new Date());
	}
	
	public Kulu(String kuluNimi, Date aeg, Double summa){
		this.kuluNimi = kuluNimi;
		this.aeg = aeg;
		this.summa = summa;
		this.projektID = 0;
		stringAeg = Kulu.AJAFORMAAT.format(new Date());
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public String getFormaaditudAeg(){
		return Kulu.AJAFORMAAT.format(aeg);
	}
	
	public static Date muudaStringAjaks(String aeg){
		
		try{
			
			aeg=aeg.replaceAll("[,;-_/]", ".");
			
			return Kulu.AJAFORMAAT.parse(aeg);
		}
		catch(Exception x){
			return null;
		}
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public String getKuluNimi() {
		return kuluNimi;
	}
	public void setKuluNimi(String kuluNimi) {
		this.kuluNimi = kuluNimi;
	}
	public Date getAeg() {
		return aeg;
	}
	public void setAeg(Date aeg) {
		this.aeg = aeg;
	}

	public Double getSumma() {
		return summa;
	}

	public void setSumma(Double summa) {
		this.summa = summa;
	}
	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

	public String getStringAeg() {
		return stringAeg;
	}

	public void setStringAeg(String stringAeg) {
		this.stringAeg = stringAeg;
	}

}
