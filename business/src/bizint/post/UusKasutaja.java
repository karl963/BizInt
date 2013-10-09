package bizint.post;

public class UusKasutaja {
	
	private int projektID;
	private String kasutajaNimi;
	
	public UusKasutaja(){
		kasutajaNimi = "";
		projektID = 0;
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

}
