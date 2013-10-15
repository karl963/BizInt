package bizint.app.alam.kuuAndmed;

public class TabeliData {
	
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
	
	

}
