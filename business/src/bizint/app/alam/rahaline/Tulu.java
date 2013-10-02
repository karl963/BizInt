package bizint.app.alam.rahaline;

import java.util.Date;

public class Tulu {
	
	private static String DEFAULT_NIMETUS = "tundmatu tulu";
	private static Double DEFAULT_SUMMA = 0.0;
	
	private String nimetus;
	private Double summa;
	private Date aeg;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Tulu(){
		this.nimetus = Tulu.DEFAULT_NIMETUS;
		this.summa = Tulu.DEFAULT_SUMMA;
		this.aeg = new Date();
	}
	
	public Tulu(String nimetus, Date aeg, Double summa){
		this.nimetus = nimetus;
		this.aeg = aeg;
		this.summa = summa;
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
