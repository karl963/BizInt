package bizint.app.alam.kuuAndmed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Kuud {
	
	public static List<List<String>> KUUD = new ArrayList<List<String>>();
	public static List<List<String>> KUUD_LYHEND = new ArrayList<List<String>>();
	
	static{

		KUUD.add(Arrays.asList("Jaanuar","Veebruar","Märts"));
		KUUD.add(Arrays.asList("Aprill","Mai","Juuni"));
		KUUD.add(Arrays.asList("Juuli","August","September"));
		KUUD.add(Arrays.asList("Oktoober","November","Detsember"));
		
		KUUD_LYHEND.add(Arrays.asList("Jaan.","Veebr.","Mär."));
		KUUD_LYHEND.add(Arrays.asList("Apr.","Mai","Juun."));
		KUUD_LYHEND.add(Arrays.asList("Juul.","Aug.","Sept."));
		KUUD_LYHEND.add(Arrays.asList("Okt.","Nov.","Dets."));
	
	}
	
	public static List<String> getKuudKvartalis(int index){
		return KUUD_LYHEND.get(index);
	}

}
