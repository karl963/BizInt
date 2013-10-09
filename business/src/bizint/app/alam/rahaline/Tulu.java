package bizint.app.alam.rahaline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tulu {
	
	private static String DEFAULT_NIMI = "";
	private static Double DEFAULT_SUMMA = 0.0;
	private static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.mm.yyyy");
	
	private String tuluNimi;
	private Double summa;
	private Date aeg;
	private int projektID;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Tulu(){
		this.tuluNimi = Tulu.DEFAULT_NIMI;
		this.summa = Tulu.DEFAULT_SUMMA;
		this.aeg = new Date();
		this.projektID = 0;
	}
	
	public Tulu(String tuluNimi, Date aeg, Double summa){
		this.tuluNimi = tuluNimi;
		this.aeg = aeg;
		this.summa = summa;
		this.projektID = 0;
	}
	
	public Tulu(Double summa){
		this.tuluNimi = Tulu.DEFAULT_NIMI;
		this.aeg = new Date();
		this.summa = summa;
		this.projektID = 0;
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public String getFormaaditudAeg(){
		try{
			return Tulu.AJAFORMAAT.format(aeg);
		}catch(Exception x){
			return "-";
		}
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public String getTuluNimi() {
		return tuluNimi;
	}
	public void setTuluNimi(String tuluNimi) {
		this.tuluNimi = tuluNimi;
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
