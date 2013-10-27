package bizint.app.alam.kuuAndmed;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class TabeliData {
	
	public static SimpleDateFormat AJAFORMAAT = new SimpleDateFormat("dd");
	
	private Double[] palgad = new Double[3];
	private int[] kuud = new int[3];
	private int[] palgaKuupäevad = new int[3];
	
	private Map<String,Double> tuludMap1 = new HashMap<String, Double>();
	private Map<String,Double> tuludMap2 = new HashMap<String, Double>();
	private Map<String,Double> tuludMap3 = new HashMap<String, Double>();
	
	private List<Double> tulud1 = new ArrayList<Double>();
	private List<Double> tulud2 = new ArrayList<Double>();
	private List<Double> tulud3 = new ArrayList<Double>();
	
	public void lisaTuludMapistListi(){
		
		Iterator<Entry<String, Double>> it = tuludMap1.entrySet().iterator();
	    while (it.hasNext()) {
	        Map.Entry<String,Double> pairs = (Map.Entry<String,Double>)it.next();
	        tulud1.add(pairs.getValue());
	    }

		Iterator<Entry<String, Double>> it2 = tuludMap2.entrySet().iterator();
	    while (it2.hasNext()) {
	        Map.Entry<String,Double> pairs = (Map.Entry<String,Double>)it2.next();
	        tulud2.add(pairs.getValue());
	    }

		Iterator<Entry<String, Double>> it3 = tuludMap3.entrySet().iterator();
	    while (it3.hasNext()) {
	        Map.Entry<String,Double> pairs = (Map.Entry<String,Double>)it3.next();
	        tulud3.add(pairs.getValue());
	    }
		
	}
	
	public Double[] getPalgad() {
		return palgad;
	}
	public void setPalgad(Double[] palgad) {
		this.palgad = palgad;
	}
	public List<Double> getTulud1() {
		return tulud1;
	}
	public void setTulud1(List<Double> tulud1) {
		this.tulud1 = tulud1;
	}
	public List<Double> getTulud2() {
		return tulud2;
	}
	public void setTulud2(List<Double> tulud2) {
		this.tulud2 = tulud2;
	}
	public List<Double> getTulud3() {
		return tulud3;
	}
	public void setTulud3(List<Double> tulud3) {
		this.tulud3 = tulud3;
	}
	public Map<String, Double> getTuludMap1() {
		return tuludMap1;
	}
	public void setTuludMap1(Map<String, Double> tuludMap1) {
		this.tuludMap1 = tuludMap1;
	}
	public Map<String, Double> getTuludMap2() {
		return tuludMap2;
	}
	public void setTuludMap2(Map<String, Double> tuludMap2) {
		this.tuludMap2 = tuludMap2;
	}
	public Map<String, Double> getTuludMap3() {
		return tuludMap3;
	}
	public void setTuludMap3(Map<String, Double> tuludMap3) {
		this.tuludMap3 = tuludMap3;
	}
	public int[] getKuud() {
		return kuud;
	}
	public void setKuud(int[] kuud) {
		this.kuud = kuud;
	}

	public int[] getPalgaKuupäevad() {
		return palgaKuupäevad;
	}

	public void setPalgaKuupäevad(int[] palgaKuupäevad) {
		this.palgaKuupäevad = palgaKuupäevad;
	}
	
	/*
	private String kuu;
	private int kuuNumber, aasta;
	private Double palk = 0.0;
	private Double tulu = 0.0;
	
	public TabeliData(){
	}
	
	public String getKuu() {
		return kuu;
	}
	public void setKuu(String kuu) {
		this.kuu = kuu;
	}
	public Double getPalk() {
		return palk;
	}
	public void setPalk(Double palk) {
		this.palk = palk;
	}
	public Double getTulu() {
		return tulu;
	}
	public void setTulu(Double tulu) {
		this.tulu = tulu;
	}

	public int getKuuNumber() {
		return kuuNumber;
	}

	public void setKuuNumber(int kuuNumber) {
		this.kuuNumber = kuuNumber;
	}

	public int getAasta() {
		return aasta;
	}

	public void setAasta(int aasta) {
		this.aasta = aasta;
	}
	*/
	

}
