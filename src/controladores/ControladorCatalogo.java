package controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelos.Aseguradora;
import conexion.Conexion;

public class ControladorCatalogo {
	//metodos para uso con aseguradora
	public static ArrayList<Aseguradora> findAllAseguradoras(){
		ArrayList<Aseguradora> res= new ArrayList<Aseguradora>();
		ResultSet queryRes=Conexion.executeQuery("SELECT * FROM aseguradora");
		try {
			while(queryRes.next()){
				res.add(new Aseguradora(0, null, null, 0));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	//public static 
	//metodos para uso con carro
	
	//metodos para uso con empresa
	
	//metodos para uso con pais
	
}
