package bizint.andmebaas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.stereotype.Component;

@Component
public class Mysql {
	
	static{
	    try {
			Class.forName("com.mysql.jdbc.Driver");
			
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection connection;
	
	@Resource(name="andmebaasiProperties")
	private Properties properties;
	
	@PostConstruct
	public void setConnection(){

		String dbms = properties.getProperty("dbms");
		String nimi = properties.getProperty("serveriNimi");
		String port = properties.getProperty("serveriPort");
		String schema = properties.getProperty("schema");
		String kasutaja = properties.getProperty("kasutajaNimi");
		String parool = properties.getProperty("kasutajaParool");
		
		try {
			connection =  DriverManager.getConnection("jdbc:" + dbms + "://" + nimi + ":" + port + "/" + schema, kasutaja, parool);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

}
