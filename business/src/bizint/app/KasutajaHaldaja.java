package bizint.app;

import java.util.ArrayList;
import java.util.List;

import bizint.app.alam.Staatus;

public class KasutajaHaldaja {
	
	private List<Staatus> staatused = new ArrayList<Staatus>();
	
	public List<Staatus> getStaatused(){
		return staatused;
	}
	
	public void setStaatused(List<Staatus> staatused){
		this.staatused = staatused;
	}

}
