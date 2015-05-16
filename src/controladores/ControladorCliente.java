package controladores;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import conexion.Conexion;

public class ControladorCliente {
	
	public static void main(String[] args){
		//addCampo("hola","int");
		deleteCampo("hola");
	}
	
	public static ResultSet getAllClientes(){
		ResultSet st=Conexion.executeQuery("Select * from cliente");
		return st;
	}
	//deben ser String[2] donde el indice 0 es la columna y el indice 1 es el valor
	public static ResultSet getFilteredClientes(ArrayList<String[]> filtros){
		String query="SELECT * FROM cliente ";
		for(int i=0;i<filtros.size();i++){
			String[] actual=filtros.get(i);
			if(i==0){
				query+="WHERE "+actual[0]+" = "+actual[1];
			}
			else{
				query+=" AND "+actual[0]+" = "+actual[1];
			}
		}
		ResultSet st=Conexion.executeQuery(query);
		System.out.println("query: "+query);
		return st;
		
	}
	
	public static int insertCliente(ArrayList<String> values){
		String query ="INSERT INTO cliente VALUES (";
		for(int i=0;i<values.size();i++){
			if(i!=0){
				query+=",";
			}
			query+=values.get(i);
		}
		return Conexion.executeUpdate(query);
	}
	
	public static int updateCliente(String nit, ArrayList<String[]> values){
		String query="UPDATE cliente ";
		for(int i=0;i<values.size();i++){
			String[] actual=values.get(i);
			if(i==0){
				query+="SET "+actual[0]+" = "+actual[1];
			}
			else{
				query+=", "+actual[0]+" = "+actual[1];
			}
		}
		query+=" WHERE nit = "+nit;
		int st=Conexion.executeUpdate(query);
		System.out.println("query: "+query);
		return st;
	}
	
	public static int deleteCliente(String nit){
		String query="DELETE FROM cliente WHERE nit = "+nit;
		int st=Conexion.executeUpdate(query);
		System.out.println("query: "+query);
		return st;
	}
	
	public static boolean addCampo(String nombre, String tipo){
		String query="ALTER TABLE cliente ADD COLUMN "+nombre+" "+tipo;
		boolean res=Conexion.execute(query);
		System.out.println(query);
		return res;
	}
	public static boolean deleteCampo(String nombre){
		String query="ALTER TABLE cliente DROP COLUMN "+nombre;
		boolean res=Conexion.execute(query);
		System.out.println(query);
		return res;
	}
}
