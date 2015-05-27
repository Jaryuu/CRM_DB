package controladores;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import conexion.Conexion;


public class ControladorCliente {
	
	public static void main(String[] args){
		Object blah = new ArrayList();
		if(blah instanceof ArrayList){
			System.out.println("true");
		}
		else{
			System.out.println("false");
		}
	}
	
	public static ResultSet getAllClientes()throws Exception{
		ResultSet st=Conexion.executeQuery("Select * from cliente");
		return st;
	}
	//deben ser String[2] donde el indice 0 es la columna y el indice 1 es el valor
	public static ResultSet getFilteredClientes(ArrayList<String[]> filtros)throws Exception{
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
	
	public static int insertCliente(ArrayList<String> values)throws Exception{
		String query ="INSERT INTO cliente VALUES (";
		for(int i=0;i<values.size();i++){
			if(i!=0){
				query+=",";
			}
			query+=values.get(i);
		}
		query+=")";
		
		MongoDBController mongo = new MongoDBController("CRM");
		mongo.setCollection("Cliente");
		TwitterController twitter = new TwitterController("cf5fLMuGEIW8vZmHzhsXJLocx",
															"Rk9Wgc8wJRcNAjBdDe02V5EpePOctZbXlTgIczlTNgmqgp8702",
															"783415302-y3DwvNRfWIBM0AtjTuv93QpvhulDjlbmy37pX0b0",
															"suvmZ44xdgfJrUkRCWn7VvhwerEuUCvjPXrSaWqvbuWsk");
		ArrayList<String> tweets = twitter.searchTweets("game of thrones", 15);
		
		mongo.insert(Integer.parseInt(values.get(0)),tweets);
		
		return Conexion.executeUpdate(query);
	}
	
	public static int updateCliente(String nit, ArrayList<String[]> values)throws Exception{
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
		
		//Actualizar en mongo
		MongoDBController mongo = new MongoDBController("CRM");
		mongo.setCollection("Cliente");
		
		try{
			mongo.update(Integer.parseInt(nit), Integer.parseInt(values.get(0)[1]));
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		
		
		return st;
	}
	
	public static int deleteCliente(String nit)throws Exception{
		String query="DELETE FROM cliente WHERE nit = "+nit;
		int st=Conexion.executeUpdate(query);
		System.out.println("query: "+query);
		
		MongoDBController mongo = new MongoDBController("CRM");
		mongo.setCollection("Cliente");
		
		try{
			mongo.delete(Integer.parseInt(nit));
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		return st;
	}
	
	public static boolean addCampo(String nombre, String tipo)throws Exception{
		String query="ALTER TABLE cliente ADD COLUMN "+nombre+" "+tipo;
		boolean res=Conexion.execute(query);
		System.out.println(query);
		return res;
	}
	public static boolean deleteCampo(String nombre)throws Exception{
		String query="ALTER TABLE cliente DROP COLUMN "+nombre;
		boolean res=Conexion.execute(query);
		System.out.println(query);
		return res;
	}
	
	public static ArrayList<String> getTweets(int nit)throws Exception{
		MongoDBController mongo = new MongoDBController("CRM");
		mongo.setCollection("Cliente");
		ArrayList<String> results = new ArrayList<String>();
		try{
			results = mongo.find(nit);
		}
		catch(Exception e){
			System.out.println(e);
		}
		return results;
	}
}
