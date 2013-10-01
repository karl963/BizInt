package bizint.app.alam;

import java.util.List;

public class Staatus {
	
	private List<Projekt> projektid;
	private String nimetus;
	
	public List<Projekt> getProjektid(){
		return projektid;
	}
	
	public void setProjektid(List<Projekt> projektid){
		this.projektid = projektid;
	}

	public String getNimetus() {
		return nimetus;
	}

	public void setNimetus(String nimetus) {
		this.nimetus = nimetus;
	}

}
