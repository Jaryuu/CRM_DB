package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class main {
 
	public static void main(String[] argv) {
		Connection connection = Conexion.getConexion();
		Conexion.close();
		connection=Conexion.getConexion();
		Conexion.close();
		
		
	}
 
}