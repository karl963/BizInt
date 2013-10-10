package bizint.post;

public class UusProjektiNimi {
	
	private String uusNimi;
	private int projektID;
	
	public UusProjektiNimi(String nimi,int id){
		this.uusNimi = nimi;
		this.projektID = id;
	}
	
	public UusProjektiNimi(){
		this.uusNimi = "";
		this.projektID = 1;
	}

	public String getUusNimi() {
		return uusNimi;
	}

	public void setUusNimi(String nimi) {
		this.uusNimi = nimi;
	}

	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

}
