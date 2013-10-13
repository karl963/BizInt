package bizint.post;

public class UusKasutaja {
	
	private int projektID;
	private String kasutajaNimi;
	private String kustuta;
	
	public UusKasutaja(){
		kasutajaNimi = "";
		projektID = 0;
		this.setKustuta("ei");
	}

	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

	public String getKasutajaNimi() {
		return kasutajaNimi;
	}

	public void setKasutajaNimi(String kasutajaNimi) {
		this.kasutajaNimi = kasutajaNimi;
	}

	public String getKustuta() {
		return kustuta;
	}

	public void setKustuta(String kustuta) {
		this.kustuta = kustuta;
	}

}
