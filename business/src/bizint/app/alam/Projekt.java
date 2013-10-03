package bizint.app.alam;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import bizint.app.alam.muu.Kommentaar;
import bizint.app.alam.muu.Logi;
import bizint.app.alam.rahaline.Kulu;
import bizint.app.alam.rahaline.Tulu;

public class Projekt {
	
	private static String DEFAULT_NIMI = "uus projekt";
	private static String DEFAULT_KIRJELDUS = "lahe projekt";
	private static int DEFAULT_REITING = 0;
	
	private String nimi,kirjeldus;
	private List<Kasutaja> kasutajad;
	private List<Tulu> tulud;
	private List<Kulu> kulud;
	private List<Kommentaar> kommentaarid;
	private List<Logi> logi;
	private int reiting;
	
	  ///////////\\\\\\\\\\\\
	 ///// constructors \\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public Projekt(){
		this.nimi = Projekt.DEFAULT_NIMI;
		this.kirjeldus = Projekt.DEFAULT_KIRJELDUS;
		this.kasutajad = new ArrayList<Kasutaja>();
		this.tulud = new ArrayList<Tulu>();
		this.kulud = new ArrayList<Kulu>(); 
		this.kommentaarid = new ArrayList<Kommentaar>(); 
		this.logi = new ArrayList<Logi>();
		this.reiting = Projekt.DEFAULT_REITING;
	}
	
	public Projekt(String nimi){
		this.nimi = nimi;
		this.kirjeldus = Projekt.DEFAULT_KIRJELDUS;
		this.kasutajad = new ArrayList<Kasutaja>();
		this.tulud = new ArrayList<Tulu>();
		this.kulud = new ArrayList<Kulu>(); 
		this.kommentaarid = new ArrayList<Kommentaar>(); 
		this.logi = new ArrayList<Logi>();
		this.reiting = Projekt.DEFAULT_REITING;
	}
	
	  ///////////\\\\\\\\\\\\
	 ///////// methods \\\\\\\
	/////////////\\\\\\\\\\\\\\
	
	public void addTulu(Tulu tulu){
		tulud.add(tulu);
	}
	public void addKulu(Kulu kulu){
		kulud.add(kulu);
	}
	
	public Double getKogutulu(){
		Double summa = 0.0;
		for(Tulu t : tulud){
			summa+=t.getSumma();
		}
		return summa;
	}
	
	public Double getKogukulu(){
		Double summa = 0.0;
		for(Kulu k : kulud){
			summa+=k.getSumma();
		}
		return summa;
	}
	
	public Kasutaja getVastutaja(){
		for(Kasutaja k : kasutajad){
			if(k.isVastutaja()){
				return k;
			}
		}
		return null;
	}
	
	public static boolean muudaKasutajaKuupalkaAndmebaasis(Projekt projekt, Kasutaja kasutaja, Double Kuupalk, Date aeg){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean muudaKasutajaTöömahtuAndmebaasis(Projekt projekt, Kasutaja kasutaja, Double töömaht){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean muudaProjektiStaatustAndmebaasis(Projekt projekt, Staatus vanaStaatus, Staatus uusStaatus){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean muudaProjektiNimeAndmebaasis(Projekt projekt, String uusProjektiNimi){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean muudaReitingutAndmebaasis(Projekt projekt, int reiting){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean lisaLogiAndmebaasi(Projekt projekt, Logi logi){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean lisaKommentaarAndmebaasi(Projekt projekt, Kommentaar kommentaar){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}

	public static boolean lisaKuluAndmebaasi(Projekt projekt, Kulu kulu){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	public static boolean lisaTuluAndmebaasi(Projekt projekt,Tulu tulu){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean eemaldaTuluAndmebaasist(Projekt projekt,Tulu tulu){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
	}
	
	public static boolean eemaldaKuluAndmebaasist(Projekt projekt,Kulu kulu){
		
		/******************************************************************
		 ******************************************************************
		 ***************************** ANDMEBAAS **************************
		 ******************************************************************
		 ******************************************************************
		 */
		
		return false;
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
	public String getKirjeldus() {
		return kirjeldus;
	}
	public void setKirjeldus(String kirjeldus) {
		this.kirjeldus = kirjeldus;
	}
	public List<Kasutaja> getKasutajad() {
		return kasutajad;
	}
	public void setKasutajad(List<Kasutaja> kasutajad) {
		this.kasutajad = kasutajad;
	}
	public List<Tulu> getTulud() {
		return tulud;
	}
	public void setTulud(List<Tulu> tulud) {
		this.tulud = tulud;
	}
	public List<Kulu> getKulud() {
		return kulud;
	}
	public void setKulud(List<Kulu> kulud) {
		this.kulud = kulud;
	}
	public List<Kommentaar> getKommentaarid() {
		return kommentaarid;
	}
	public void setKommentaarid(List<Kommentaar> kommentaarid) {
		this.kommentaarid = kommentaarid;
	}
	public List<Logi> getLogi() {
		return logi;
	}
	public void setLogi(List<Logi> logi) {
		this.logi = logi;
	}
	public int getReiting() {
		return reiting;
	}
	public void setReiting(int reiting) {
		this.reiting = reiting;
	}
	
}
