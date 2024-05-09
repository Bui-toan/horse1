package SQL;
import java.sql.*;
public class Connect {
	   public static Connection getConnection() {
	    	  Connection c=null;
	    	  try {
				DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
				String url="jdbc:mySQL://localhost:3306/horse";
				String username="root";
				String password="";
				c=DriverManager.getConnection(url, username, password);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
	    	  return c;
	       }
	       public static void closeConnection(Connection c) {
	    	  try {
	    		  if(c!=null) {
	    			  c.close();
	    		  }
	    	  }catch(Exception e){
	    		  e.printStackTrace();
	    	  }
	       }  
	      
	       }
	    	  
	      
	      
	


