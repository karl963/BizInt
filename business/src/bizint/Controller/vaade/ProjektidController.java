package bizint.Controller.vaade;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import bizint.app.alam.Kasutaja;
import bizint.app.alam.Projekt;
import bizint.app.alam.Staatus;
import bizint.app.alam.rahaline.Tulu;

@Controller
public class ProjektidController {
	
	List<Staatus> staatused;
	
	@RequestMapping("/vaadeProjektid.htm")
	public ModelAndView annaAndmed(){
		
		List<Object> staatused = new ArrayList<Object>();

		Staatus s1 = new Staatus("Suvalised",0);
		
		Projekt p1 = new Projekt("Veebileht");
		Kasutaja ka1 = new Kasutaja("Mehike");
		ka1.setVastutaja(true);
		p1.setKasutajad(Arrays.asList(ka1));
		//p1.setVastutaja(new Kasutaja("Dolan"));
		List<Tulu> tulud1 = new ArrayList<Tulu>();
		tulud1.add(new Tulu(123.4));
		tulud1.add(new Tulu(234.57));
		p1.setTulud(tulud1);
		
		Projekt p2 = new Projekt("Spring mvc");
		Kasutaja ka2 = new Kasutaja("Kalle");
		ka2.setVastutaja(true);
		p2.setKasutajad(Arrays.asList(ka2));
		//p2.setVastutaja(new Kasutaja("Springer"));
		List<Tulu> tulud2 = new ArrayList<Tulu>();
		tulud2.add(new Tulu(9999.99));
		p1.setTulud(tulud2);
		
		Projekt p3 = new Projekt("Kodukas");
		//p3.setVastutaja(new Kasutaja("Tark mees"));

		List<Tulu> tulud3 = new ArrayList<Tulu>();
		tulud3.add(new Tulu(0.01));
		p1.setTulud(tulud3);
		s1.addProjekt(p1);
		s1.addProjekt(p2);
		s1.addProjekt(p3);
		
		Staatus s2 = new Staatus("Teine staatus",1);
		
		Projekt p11 = new Projekt("Mõttetu projekt");
		//p11.setVastutaja(new Kasutaja("kaval Pea"));
		List<Tulu> tulud11 = new ArrayList<Tulu>();
		tulud11.add(new Tulu(1111.1));
		p11.setTulud(tulud11);
		
		Projekt p21 = new Projekt("Lahe projekt");
		p21.setKasutajad(Arrays.asList(ka2));
		//p21.setVastutaja(new Kasutaja("jobu"));
		List<Tulu> tulud21 = new ArrayList<Tulu>();
		tulud21.add(new Tulu(412.0));
		p11.setTulud(tulud21);
		
		s2.addProjekt(p11);
		s2.addProjekt(p21);
		s2.setSumma(123.32);
		
		s1.setSumma(55532.2);
		
		staatused.add(s1);
		staatused.add(s2);


		return new ModelAndView("vaadeProjektid", "staatused", staatused);
	}


}
