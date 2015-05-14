package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Conexion {
	private static Connection connection=null;
	public static Connection getConexion(){
		if(connection==null){
			fetchConnection();
		}
		return connection;
	}
	public static void fetchConnection () {
 
		System.out.println("-------- PostgreSQL "
				+ "JDBC Connection Testing ------------");
 
		try {
 
			Class.forName("org.postgresql.Driver");
 
		} catch (ClassNotFoundException e) {
 
			System.out.println("Where is your PostgreSQL JDBC Driver? "
					+ "Include in your library path!");
			e.printStackTrace();
			return;
 
		}
 
		System.out.println("PostgreSQL JDBC Driver Registered!");
 
		connection = null;
 
		try {
 
			connection = DriverManager.getConnection(
					"jdbc:postgresql://127.0.0.1:5432/CRM", "postgres",
					"123456");
 
		} catch (SQLException e) {
 
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
 
		}
		if (connection != null) {
			System.out.println("You made it, take control your database now!");
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	
	public static void close(){
		connection=null;
		if (connection != null) {	
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}