package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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
		} else {
			System.out.println("Failed to make connection!");
		}
	}
	
	public static void close(){
		
		if (connection != null) {	
			try {
				connection.close();
				connection=null;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static ResultSet executeQuery(String st)throws Exception{
		fetchConnection();
		PreparedStatement ps=null;
		ResultSet res=null;
		try {
			ps=connection.prepareStatement(st);
			res = ps.executeQuery();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			String message=e.getMessage();
			System.out.println(e.getMessage());
			if(message.contains("para tipo date")){
				message="Ingrese una fecha valida";
			}
			else if(message.contains("para integer")){
				message="Ingrese un numero valido";
			}
			throw new Exception(message);
		}
		close();
		return res;
	}
	public static int executeUpdate(String st)throws Exception{
		fetchConnection();
		PreparedStatement ps=null;
		int res=-1;
		try{
		ps=connection.prepareStatement(st);
		res=ps.executeUpdate();
		}
		catch(Exception e){
			e.printStackTrace();
			String message=e.getMessage();
			System.out.println(e.getMessage());
			if(message.contains("para tipo date")){
				message="Ingrese una fecha valida";
			}
			else if(message.contains("para integer")){
				message="Ingrese un numero valido";
			}
			throw new Exception(message);
		}
		close();
		return res;
	}
	public static boolean execute(String st)throws Exception{
		fetchConnection();
		PreparedStatement ps=null;
		ps=connection.prepareStatement(st);
		boolean res=ps.execute();
		close();
		return res;
	}
}