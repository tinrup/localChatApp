package data.localDataBase;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class ConnectToDB {
	
	 public static Connection getConnection() throws Exception {

	      try {

	          String driver = "org.mariadb.jdbc.Driver";
	          String url ="jdbc:mariadb://your_server_ip_address/message_app";
			String username = "username";
	          String password = "password";
	  
	          Connection conn = DriverManager.getConnection(url, username, password);
	          
	      
	          return conn;
	      } catch (Exception e) {

	          System.out.println("Not connected : " + e);
	      }

	      return null;
	      
	  }
}
