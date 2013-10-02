package bizint.app.alam.muu;

import java.util.Date;

import bizint.app.alam.Kasutaja;

public class Kommentaar {
	
	public static String DEFAULT_SÕNUM = "tühi kommentaar";
	
	private Kasutaja kasutaja;
	private String sõnum;
	private Date aeg;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kommentaar(){
		this.kasutaja = new Kasutaja("nimetu");
		this.sõnum = Logi.DEFAULT_SÕNUM;
		this.aeg = new Date();
	}
	
	public Kommentaar(Kasutaja kasutaja, String sõnum){
		this.kasutaja = kasutaja;
		this.sõnum = sõnum;
		this.aeg = new Date();
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public Kasutaja getKasutaja() {
		return kasutaja;
	}
	public void setKasutaja(Kasutaja kasutaja) {
		this.kasutaja = kasutaja;
	}
	public String getSõnum() {
		return sõnum;
	}
	public void setSõnum(String sõnum) {
		this.sõnum = sõnum;
	}
	public Date getAeg() {
		return aeg;
	}
	public void setAeg(Date aeg) {
		this.aeg = aeg;
	}

}
