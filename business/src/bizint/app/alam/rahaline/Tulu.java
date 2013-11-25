package bizint.app.alam.rahaline;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Tulu {
	
	private static String DEFAULT_NIMI = "";
	private static Double DEFAULT_SUMMA = 0.0;
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private String tuluNimi;
	private Object summa;
	private Date aeg;
	private int projektID;
	private String stringAeg;
	private String käibemaksuArvestatakse = "ei";
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Tulu(){
		this.tuluNimi = Tulu.DEFAULT_NIMI;
		this.summa = Tulu.DEFAULT_SUMMA;
		this.aeg = new Date();
		this.projektID = 0;
		stringAeg = Tulu.AJAFORMAAT.format(new Date());
	}
	
	public Tulu(String tuluNimi, Date aeg, Double summa){
		this.tuluNimi = tuluNimi;
		this.aeg = aeg;
		this.summa = summa;
		this.projektID = 0;
		stringAeg = Tulu.AJAFORMAAT.format(new Date());
	}
	
	public Tulu(Double summa){
		this.tuluNimi = Tulu.DEFAULT_NIMI;
		this.aeg = new Date();
		this.summa = summa;
		this.projektID = 0;
		stringAeg = Tulu.AJAFORMAAT.format(new Date());
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
	
	public static Date muudaStringAjaks(String aeg){
		
		try{
			
			aeg=aeg.replaceAll("[,;-_/]", ".");
			
			return Tulu.AJAFORMAAT.parse(aeg);
		}
		catch(Exception x){
			return null;
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
	
	public Object getSumma() {
		return summa;
	}
	
	public void setSumma(Object summa){
		
		try{
			this.summa = Double.valueOf(String.valueOf(summa).replace(",", "."));
		}catch(Exception x){
			this.summa = 0.0;
		}
	}
	
	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

	public String getStringAeg() {
		return stringAeg;
	}

	public void setStringAeg(String stringAeg) {
		this.stringAeg = stringAeg;
	}
	
	public String getkäibemaksuArvestatakse(){
		return käibemaksuArvestatakse;
	}
	
	public void setkäibemaksuArvestatakse(String k){
		this.käibemaksuArvestatakse = k;
	}
	
	public boolean getkasArvestaKaibemaksu(){
		if(käibemaksuArvestatakse.equals("jah")){
			return true;
		}
		else{
			return false;
		}
	}

	public Object getKaibemaks(){
		return (Double)summa*0.2;
	}
}
