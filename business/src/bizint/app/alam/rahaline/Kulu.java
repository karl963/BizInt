package bizint.app.alam.rahaline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Kulu {
	
	private static String DEFAULT_NIMETUS = "tundmatu kulu";
	private static Double DEFAULT_SUMMA = 0.0;
	private static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.mm.yyyy");
	
	private String nimetus;
	private Double summa;
	private Date aeg;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kulu(){
		this.nimetus = Kulu.DEFAULT_NIMETUS;
		this.summa = Kulu.DEFAULT_SUMMA;
		this.aeg = new Date();
	}
	
	public Kulu(String nimetus, Date aeg, Double summa){
		this.nimetus = nimetus;
		this.aeg = aeg;
		this.summa = summa;
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
	
	public String getNimetus() {
		return nimetus;
	}
	public void setNimetus(String nimetus) {
		this.nimetus = nimetus;
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

}
