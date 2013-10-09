package bizint.app.alam.rahaline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kulu {
	
	private static String DEFAULT_NIMI = "";
	private static Double DEFAULT_SUMMA = 0.0;
	private static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.mm.yyyy");
	
	private String kuluNimi;
	private Double summa;
	private Date aeg;
	private int projektID;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\

	public Kulu(){
		this.kuluNimi = Kulu.DEFAULT_NIMI;
		this.summa = Kulu.DEFAULT_SUMMA;
		this.aeg = new Date();
		this.projektID = 0;
	}
	
	public Kulu(String kuluNimi, Date aeg, Double summa){
		this.kuluNimi = kuluNimi;
		this.aeg = aeg;
		this.summa = summa;
		this.projektID = 0;
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public String getFormaaditudAeg(){
		return Kulu.AJAFORMAAT.format(aeg);
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

}
