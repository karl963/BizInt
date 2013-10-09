package bizint.app.alam.muu;

import java.text.SimpleDateFormat;
import java.util.Date;

import bizint.app.alam.Kasutaja;

public class Kommentaar {
	
	public static String DEFAULT_SONUM = "t�hi kommentaar";
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.mm.yyyy 'kell' hh:mm:ss");
	
	private Kasutaja kasutaja;
	private String sonum;
	private Date aeg;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kommentaar(){
		this.kasutaja = new Kasutaja("nimetu");
		this.sonum = Kommentaar.DEFAULT_SONUM;
		this.aeg = new Date();
	}
	
	public Kommentaar(Kasutaja kasutaja, String sonum){
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
	public void setSonum(String s�num) {
		this.sonum = s�num;
	}
	public Date getAeg() {
		return aeg;
	}
	public void setAeg(Date aeg) {
		this.aeg = aeg;
	}

}
