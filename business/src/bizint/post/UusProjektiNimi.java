package bizint.post;

public class UusProjektiNimi {
	
	private String nimi;
	private int projektID;
	
	public UusProjektiNimi(String nimi,int id){
		this.nimi = nimi;
		this.projektID = id;
	}

	public String getNimi() {
		return nimi;
	}

	public void setNimi(String nimi) {
		this.nimi = nimi;
	}

	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

}
