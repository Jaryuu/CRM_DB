
/*
 *	Universidad del Valle de Guatemala 
 * 	Bases de Datos
 * 	Proyecto 2
 * 	Julio Ayala, Diego Perez, Ricardo Zepeda
 * 	Controlador de Cliente
 */package controladores;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import modelos.Aseguradora;
import modelos.Empresa;
import modelos.Pais;
import conexion.Conexion;


public class ControladorCliente {
	//Metodo para obtener todos los clientes de la tabla
	public static ResultSet getAllClientes()throws Exception{
		ResultSet st=Conexion.executeQuery("Select * from cliente");
		return st;
	}
	//metodos para obteneer los clientes por filtro
	//deben ser String[2] donde el indice 0 es la columna y el indice 1 es el valor
	public static ResultSet getFilteredClientes(ArrayList<String[]> filtros)throws Exception{
		String query="SELECT * FROM cliente ";
		for(int i=0;i<filtros.size();i++){
			String[] actual=filtros.get(i);
			
			ArrayList<Pais> paises = ControladorCatalogo.findAllPaises();
			ArrayList<Aseguradora> aseguradoras = ControladorCatalogo.findAllAseguradoras();
			ArrayList<Empresa> empresas = ControladorCatalogo.findAllEmpresas();	


			if(actual[0].equals("idpais")){
				for (Pais p : paises){
					if (actual[1].equals(p.getNombre())){
						actual[1]=p.getId()+"";
						break;
					}
				}
			}
			else if(actual[0].equals("idempresa")){
				for (Empresa e : empresas){
					if (actual[1].equals(e.getNombre())){
						actual[1]=e.getId()+"";
						break;
					}
				}
			}
			else if(actual[0].equals("idaseguradora")){
				for (Aseguradora a : aseguradoras){	
					if (actual[1].equals(a.getNombre())){
						actual[1]=a.getId()+"";
						break;
					}
				}
			}
			
			if(i==0){
				query+="WHERE "+actual[0]+" = '"+actual[1]+"'";
			}
			else{
				query+=" AND "+actual[0]+" = '"+actual[1]+"'";
			}
		}
		ResultSet st=Conexion.executeQuery(query);
		System.out.println("query: "+query);
		return st;
		
	}
	//metodo para insertar clientes
	public static int insertCliente(ArrayList<String> values)throws Exception{
		String query ="INSERT INTO cliente VALUES (";
		//valor de int
		int nit=-1;
		try{
			nit=Integer.parseInt(values.get(0));
		}
		catch(Exception e){
			throw new Exception("El nit debe ser un numero");
		}
		for(int i=0;i<values.size();i++){
			
			ArrayList<Pais> paises = ControladorCatalogo.findAllPaises();
			ArrayList<Aseguradora> aseguradoras = ControladorCatalogo.findAllAseguradoras();
			ArrayList<Empresa> empresas = ControladorCatalogo.findAllEmpresas();	


			if(i==11){
				for (Pais p : paises){
					if (values.get(i).equals(p.getNombre())){
							values.set(i, p.getId()+"");
							break;
						}
					}
				}
			else if(i==13){
				for (Empresa e : empresas){
					if (values.get(i).equals(e.getNombre())){
						values.set(i, e.getId()+"");
						break;
					}
				}
			}
			else if(i==14){
				for (Aseguradora a : aseguradoras){	
					if (values.get(i).equals(a.getNombre())){
						values.set(i, a.getId()+"");
						break;
					}
				}
			}
			
			
			if(i!=0){
				query+=",";
			}
			query+="'"+values.get(i)+"'";
		}
		query+=")";
		//obtener los tweets
		MongoDBController mongo = new MongoDBController("CRM");
		mongo.setCollection("Cliente");
		TwitterController twitter = new TwitterController("cf5fLMuGEIW8vZmHzhsXJLocx",
															"Rk9Wgc8wJRcNAjBdDe02V5EpePOctZbXlTgIczlTNgmqgp8702",
															"783415302-y3DwvNRfWIBM0AtjTuv93QpvhulDjlbmy37pX0b0",
															"suvmZ44xdgfJrUkRCWn7VvhwerEuUCvjPXrSaWqvbuWsk");
		Random ran = new Random();
		String[] topics = {"#NoLeToca", "#RenunciaYa","Game of Thrones", "science"};
		ArrayList<String> tweets = twitter.searchTweets(topics[ran.nextInt(3)], 15);
		System.out.println(values.get(0));
		mongo.insert(nit,tweets);
		
		return Conexion.executeUpdate(query);
	}
	//hacer update para cliente
	public static int updateCliente(String nit, ArrayList<String[]> values)throws Exception{
		String query="UPDATE cliente ";
		int nitv=-1;
		try{
			nitv=Integer.parseInt(values.get(0)[1]);
		}
		catch(Exception e){
			throw new Exception("El nit debe ser un numero");
		}
		for(int i=0;i<values.size();i++){
			String[] actual=values.get(i);
			
			ArrayList<Pais> paises = ControladorCatalogo.findAllPaises();
			ArrayList<Aseguradora> aseguradoras = ControladorCatalogo.findAllAseguradoras();
			ArrayList<Empresa> empresas = ControladorCatalogo.findAllEmpresas();
			
			if(actual[0].equals("idpais")){
				for (Pais p : paises){
					if (actual[1].equals(p.getNombre())){
						actual[1]=p.getId()+"";
						break;
					}
				}
			}
			else if(actual[0].equals("idempresa")){
				for (Empresa e : empresas){
					if (actual[1].equals(e.getNombre())){
						actual[1]=e.getId()+"";
						break;
					}
				}
			}
			else if(actual[0].equals("idaseguradora")){
				for (Aseguradora a : aseguradoras){	
					if (actual[1].equals(a.getNombre())){
						actual[1]=a.getId()+"";
						break;
					}
				}
			}
			
			if(i==0){
				query+="SET "+actual[0]+" = '"+actual[1]+"'";
			}
			else{
				query+=", "+actual[0]+" = '"+actual[1]+"'";
			}
		}
		query+=" WHERE nit = "+nit;
		int st=Conexion.executeUpdate(query);
		System.out.println("query: "+query);
		
		//Actualizar en mongo
		MongoDBController mongo = new MongoDBController("CRM");
		mongo.setCollection("Cliente");
		
		try{
			if(nitv!=-1){
				mongo.update(nitv, Integer.parseInt(values.get(0)[1]));
			}
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		
		
		return st;
	}
	//metodo para borrar un cliente con el nit
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
	//metodo para crear un campo
	public static boolean addCampo(String nombre, String tipo)throws Exception{
		String query="ALTER TABLE cliente ADD COLUMN "+nombre+" "+tipo;
		boolean res=Conexion.execute(query);
		System.out.println(query);
		return res;
	}
	//metodo para eliminar un campo
	public static boolean deleteCampo(String nombre)throws Exception{
		String query="ALTER TABLE cliente DROP COLUMN "+nombre;
		boolean res=Conexion.execute(query);
		System.out.println(query);
		return res;
	}
	//metodo para obtener los tweets de un cliente
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
