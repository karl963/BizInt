package bizint.post;

public class UusKirjeldus {

	private String kirjeldus;
	private int projektID;
	
	public UusKirjeldus(){
		setKirjeldus("");
		projektID = 0;
	}

	public String getKirjeldus() {
		return kirjeldus;
	}

	public void setKirjeldus(String kirjeldus) {
		this.kirjeldus = kirjeldus;
	}

	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}
	
}
