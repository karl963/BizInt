package bizint.andmebaas;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.sql.DataSource;

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
    public void init() throws SQLException {
		
		String schema = properties.getProperty("schema");
		
        try {
            // Get DataSource
        	Context initContext = new InitialContext();
        	Context envContext  = (Context)initContext.lookup("java:/comp/env");
        	DataSource datasource = (DataSource)envContext.lookup("jdbc/" + schema);
        	connection = datasource.getConnection();
 
        } catch (NamingException e) {
            e.printStackTrace();
        }
    }
}
