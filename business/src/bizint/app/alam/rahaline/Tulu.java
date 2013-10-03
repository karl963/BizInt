package bizint.app.alam.rahaline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tulu {
	
	private static String DEFAULT_NIMI = "tundmatu tulu";
	private static Double DEFAULT_SUMMA = 0.0;
	private static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.mm.yyyy");
	
	private String nimi;
	private Double summa;
	private Date aeg;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Tulu(){
		this.nimi = Tulu.DEFAULT_NIMI;
		this.summa = Tulu.DEFAULT_SUMMA;
		this.aeg = new Date();
	}
	
	public Tulu(String nimi, Date aeg, Double summa){
		this.nimi = nimi;
		this.aeg = aeg;
		this.summa = summa;
	}
	
	public Tulu(Double summa){
		this.nimi = Tulu.DEFAULT_NIMI;
		this.aeg = new Date();
		this.summa = summa;
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public String getFormaaditudAeg(){
		return Tulu.AJAFORMAAT.format(aeg);
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public String getNimi() {
		return nimi;
	}
	public void setNimi(String nimi) {
		this.nimi = nimi;
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
