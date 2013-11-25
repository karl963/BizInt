package bizint.app.alam.rahaline;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bizint.andmebaas.Mysql;

public class Kulu {
	
	public static int VIGA_ANDMEBAASIGA_ÜHENDUSMISEL = 0, KÕIK_OKEI = 1;
	
	private static String DEFAULT_NIMI = "";
	private static Double DEFAULT_SUMMA = 0.0;
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd.MM.yyyy");
	
	private String kuluNimi;
	private Object summa;
	private Date aeg;
	private int projektID;
	private String stringAeg;
	private String kasutajaNimi = "";
	private boolean korduv = false;
	private int kuluID;
	private String käibemaksuArvestatakse = "ei";
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\

	public Kulu(){
		this.kuluNimi = Kulu.DEFAULT_NIMI;
		this.summa = Kulu.DEFAULT_SUMMA;
		this.aeg = new Date();
		this.projektID = 0;
		stringAeg = Kulu.AJAFORMAAT.format(new Date());
	}
	
	public Kulu(String kuluNimi, Date aeg, Double summa){
		this.kuluNimi = kuluNimi;
		this.aeg = aeg;
		this.summa = summa;
		this.projektID = 0;
		stringAeg = Kulu.AJAFORMAAT.format(new Date());
	}
	
	public Kulu(String kuluNimi, Date aeg, Double summa,String kaibemaks){
		this.kuluNimi = kuluNimi;
		this.aeg = aeg;
		this.summa = summa;
		this.projektID = 0;
		stringAeg = Kulu.AJAFORMAAT.format(new Date());
		this.käibemaksuArvestatakse = kaibemaks;
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public String getFormaaditudAeg(){
		return Kulu.AJAFORMAAT.format(aeg);
	}
	
	public static Date muudaStringAjaks(String aeg){
		
		try{
			
			aeg=aeg.replaceAll("[,;-_/]", ".");
			
			return Kulu.AJAFORMAAT.parse(aeg);
		}
		catch(Exception x){
			return null;
		}
	}
	
	public static int lisaYldKuluAndmebaasi(Kulu kulu,boolean korduv, int juhtID){
		
		int vastus = KÕIK_OKEI;
		
		Connection con = (new Mysql()).getConnection();
		if(con == null){
			return Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL;
		}
		
		try{
			
			Timestamp aeg = new Timestamp(kulu.getAeg().getTime());
			
			Statement stmt = con.createStatement();
			String query = "INSERT INTO yldkulud (yldkuluNimi,yldkulu,algusAeg,korduv,kaibemaksuArvestatakse,juhtID) VALUES ('"+kulu.getKuluNimi()+"',"+kulu.getSumma()+",'"+aeg+"',"+korduv+","+kulu.getkasArvestaKaibemaksu()+","+juhtID+")";
			stmt.executeUpdate(query);
			
		}catch(Exception x){
			vastus = Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL;
			x.printStackTrace();
		}finally{
			if(con!=null){try{con.close();}catch(Exception x){}}
		}
		
		return vastus;
	}
	
	
	public static int muudaYldKuludAndmebaasis(List<Kulu> kulud, int juhtID){
		
		int vastus = KÕIK_OKEI;
		
		Connection con = (new Mysql()).getConnection();
		if(con == null){
			return Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL;
		}
		
		try{
			
			for(Kulu kulu : kulud){

				Timestamp aeg = new Timestamp(kulu.getAeg().getTime());

				Statement stmt = con.createStatement();
				String query = "UPDATE yldkulud SET yldkuluNimi='"+kulu.getKuluNimi()+"', yldkulu="+kulu.getSumma()+", algusAeg='"+aeg+"', korduv="+kulu.isKorduv()+", kaibemaksuArvestatakse = "+kulu.getkasArvestaKaibemaksu()+" WHERE juhtID="+juhtID+" AND yldkuluID="+kulu.getKuluID();
				stmt.executeUpdate(query);
			}
			
		}catch(Exception x){
			vastus = Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL;
			x.printStackTrace();
		}finally{
			if(con!=null){try{con.close();}catch(Exception x){}}
		}
		
		return vastus;
	}
	
	public static int kustutaKuluAndmebaasist(int kuluID, int juhtID){
		
		int vastus = KÕIK_OKEI;
		
		Connection con = (new Mysql()).getConnection();
		if(con == null){
			return Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL;
		}
		
		try{
			Statement stmt = con.createStatement();
			String query = "DELETE FROM yldkulud WHERE juhtID="+juhtID+" AND yldkuluID="+kuluID;
			stmt.executeUpdate(query);
		}catch(Exception x){
			vastus = Kulu.VIGA_ANDMEBAASIGA_ÜHENDUSMISEL;
			x.printStackTrace();
		}finally{
			if(con!=null){try{con.close();}catch(Exception x){}}
		}
		
		return vastus;
		
	}
	
	  ///////////\\\\\\\\\\\\
	 // getters and setters \\
	/////////////\\\\\\\\\\\\\\
	
	public String getKuluNimi() {
		return kuluNimi;
	}
	public void setKuluNimi(String kuluNimi) {
		this.kuluNimi = kuluNimi;
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

	public String getKasutajaNimi() {
		return kasutajaNimi;
	}

	public void setKasutajaNimi(String kasutajaNimi) {
		this.kasutajaNimi = kasutajaNimi;
	}

	public boolean isKorduv() {
		return korduv;
	}

	public void setKorduv(boolean korduv) {
		this.korduv = korduv;
	}

	public int getKuluID() {
		return kuluID;
	}

	public void setKuluID(int kuluID) {
		this.kuluID = kuluID;
	}
	
	public String getkäibemaksuArvestatakse(){
		return käibemaksuArvestatakse;
	}
	
	public void setkäibemaksuArvestatakse(String k){
		this.käibemaksuArvestatakse = k;
	}
	
	public boolean getkasArvestaKaibemaksu(){
		if(käibemaksuArvestatakse.contains("jah")){
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
