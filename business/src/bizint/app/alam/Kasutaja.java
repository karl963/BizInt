package bizint.app.alam;

import java.util.Date;

public class Kasutaja {
	
	public static String DEFAULT_NIMI = "nimetu";
	public static Double DEFAULT_KUUPALK = 0.0;
	public static Double DEFAULT_T��MAHT = 0.0;
	public static boolean DEFAULT_VASTUTAJA = false;
	public static boolean DEFAULT_AKTIIVNE = false;
	
	private String nimi;
	private Date aeg;
	private Double kuupalk, t��maht;
	private boolean vastutaja, aktiivne;

	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Kasutaja(){
		this.nimi = Kasutaja.DEFAULT_NIMI;
		this.aeg = new Date();
		this.kuupalk = Kasutaja.DEFAULT_KUUPALK;
		this.t��maht = Kasutaja.DEFAULT_T��MAHT;
		this.vastutaja = Kasutaja.DEFAULT_VASTUTAJA;
		this.aktiivne = Kasutaja.DEFAULT_AKTIIVNE;
	}
	
	public Kasutaja(String nimi){
		this.nimi = nimi;
		this.aeg = new Date();
		this.kuupalk = Kasutaja.DEFAULT_KUUPALK;
		this.t��maht = Kasutaja.DEFAULT_T��MAHT;
		this.vastutaja = Kasutaja.DEFAULT_VASTUTAJA;
		this.aktiivne = Kasutaja.DEFAULT_AKTIIVNE;
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
	public Double getT��maht() {
		return t��maht;
	}
	public void setT��maht(Double t��maht) {
		this.t��maht = t��maht;
	}

	public boolean isVastutaja() {
		return vastutaja;
	}

	public void setVastutaja(boolean vastutaja) {
		this.vastutaja = vastutaja;
	}

	public boolean isAktiivne() {
		return aktiivne;
	}

	public void setAktiivne(boolean aktiivne) {
		this.aktiivne = aktiivne;
	}
	
}
