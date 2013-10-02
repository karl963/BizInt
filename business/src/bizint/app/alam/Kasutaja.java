package bizint.app.alam;

import java.util.Date;

public class Kasutaja {
	
	public static String DEFAULT_NIMI = "nimetu";
	public static Double DEFAULT_KUUPALK = 0.0;
	public static Double DEFAULT_TÖÖMAHT = 0.0;
	
	private String nimi;
	private Date aeg;
	private Double kuupalk, töömaht;

	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kasutaja(){
		this.nimi = Kasutaja.DEFAULT_NIMI;
		this.aeg = new Date();
		this.kuupalk = Kasutaja.DEFAULT_KUUPALK;
		this.töömaht = Kasutaja.DEFAULT_TÖÖMAHT;
	}
	
	public Kasutaja(String nimi){
		this.nimi = nimi;
		this.aeg = new Date();
		this.kuupalk = Kasutaja.DEFAULT_KUUPALK;
		this.töömaht = Kasutaja.DEFAULT_TÖÖMAHT;
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
	public Double getKuupalk() {
		return kuupalk;
	}
	public void setKuupalk(Double kuupalk) {
		this.kuupalk = kuupalk;
	}
	public Double getTöömaht() {
		return töömaht;
	}
	public void setTöömaht(Double töömaht) {
		this.töömaht = töömaht;
	}
	
}
