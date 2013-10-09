package bizint.post;

public class UusKommentaar {
	
	private String sonum;
	private int projektID;
	
	public UusKommentaar(){
		this.sonum = "";
		this.projektID = 0;
	}
	
	public String getSonum() {
		return sonum;
	}
	public void setSonum(String sonum) {
		this.sonum = sonum;
	}
	public int getProjektID() {
		return projektID;
	}
	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}

	
}
