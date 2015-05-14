package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.swing.UIManager;


public class Main {
 
	public static void main(String[] argv) {
		try { 
		    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
		    e.printStackTrace();
		}
		
		try {
			GUI frame = new GUI();
			frame.setVisible(true);
			frame.setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);
			//frame.getTextPane().requestFocus();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Connection connection = Conexion.getConexion();
		Conexion.close();
		connection=Conexion.getConexion();
		Conexion.close();
		
		
	}
 
}