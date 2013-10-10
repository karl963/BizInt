package bizint.app.alam.muu;

import java.text.SimpleDateFormat;
import java.util.Date;

import bizint.app.alam.Kasutaja;

public class Logi {
	
	public static String DEFAULT_SONUM = "tühi sonum";
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy '-' HH:mm:ss");
	
	private Kasutaja kasutaja;
	private String sonum;
	private Date aeg;

	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Logi(){
		this.kasutaja = new Kasutaja("nimetu");
		this.sonum = Logi.DEFAULT_SONUM;
		this.aeg = new Date();
	}
	
	public Logi(Kasutaja kasutaja, String sonum){
		this.kasutaja = kasutaja;
		this.sonum = sonum;
		this.aeg = new Date();
	}
	
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public String getFormaaditudAeg(){
		return AJAFORMAAT.format(aeg);
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
	public String getSonum() {
		return sonum;
	}
	public void setSonum(String sonum) {
		this.sonum = sonum;
	}
	public Date getAeg() {
		return aeg;
	}
	public void setAeg(Date aeg) {
		this.aeg = aeg;
	}

}
