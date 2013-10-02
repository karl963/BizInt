package bizint.app.alam.muu;

import java.util.Date;

import bizint.app.alam.Kasutaja;

public class Logi {
	
	public static String DEFAULT_S�NUM = "s�num puudub";
	
	private Kasutaja kasutaja;
	private String s�num;
	private Date aeg;

	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Logi(){
		this.kasutaja = new Kasutaja("nimetu");
		this.s�num = Logi.DEFAULT_S�NUM;
		this.aeg = new Date();
	}
	
	public Logi(Kasutaja kasutaja, String s�num){
		this.kasutaja = kasutaja;
		this.s�num = s�num;
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
	public String getS�num() {
		return s�num;
	}
	public void setS�num(String s�num) {
		this.s�num = s�num;
	}
	public Date getAeg() {
		return aeg;
	}
	public void setAeg(Date aeg) {
		this.aeg = aeg;
	}

}
