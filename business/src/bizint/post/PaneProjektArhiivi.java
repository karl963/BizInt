package bizint.post;

public class PaneProjektArhiivi {

	private int projektID;
	private int juhtID;
	private int arhiivis;
	
	public PaneProjektArhiivi(int id, int juht){
		this.projektID=id;
		this.juhtID=juht;
		this.arhiivis=1;
	}
	
	public PaneProjektArhiivi(int id){
		this.projektID=id;
		this.arhiivis=1;
	}
	
	public PaneProjektArhiivi(){
		
		this.projektID=projektID;
		this.juhtID=1;
		this.arhiivis=0;
		
	}

	
	public int getProjektID() {
		return projektID;
	}

	public void setProjektID(int projektID) {
		this.projektID = projektID;
	}
	
}
