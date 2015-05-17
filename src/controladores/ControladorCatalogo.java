package controladores;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import modelos.Aseguradora;
import modelos.Carro;
import modelos.Empresa;
import modelos.Pais;
import conexion.Conexion;

public class ControladorCatalogo {
	//metodos para uso con aseguradora
	public static ArrayList<Aseguradora> findAllAseguradoras(){
		ArrayList<Aseguradora> res= new ArrayList<Aseguradora>();
		ResultSet queryRes=Conexion.executeQuery("SELECT * FROM aseguradora");
		try {
			while(queryRes.next()){
				res.add(new Aseguradora(queryRes.getInt("id"),queryRes.getString("nombre"),queryRes.getString("descripcion"),queryRes.getInt("precio")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static Aseguradora findAseguradora(int id){
		ArrayList<Aseguradora> lista=findAllAseguradoras();
		for(int i=0;i<lista.size();i++){
			Aseguradora actual=lista.get(i);
			if(actual.getId()==id){
				return actual;
			}
		}
		return null;
	}
	//metodos para uso con carro
	public static ArrayList<Carro> findAllCarros(){
		ArrayList<Carro> res= new ArrayList<Carro>();
		ResultSet queryRes=Conexion.executeQuery("SELECT * FROM carro");
		try {
			while(queryRes.next()){
				res.add(new Carro(queryRes.getInt("modelo"),queryRes.getString("marca"),queryRes.getString("color"),queryRes.getString("transmision")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static Carro findCarro(int modelo){
		ArrayList<Carro> lista=findAllCarros();
		for(int i=0;i<lista.size();i++){
			Carro actual=lista.get(i);
			if(actual.getModelo()==modelo){
				return actual;
			}
		}
		return null;
	}
	//metodos para uso con empresa
	public static ArrayList<Empresa> findAllEmpresas(){
		ArrayList<Empresa> res= new ArrayList<Empresa>();
		ResultSet queryRes=Conexion.executeQuery("SELECT * FROM empresa");
		try {
			while(queryRes.next()){
				res.add(new Empresa(queryRes.getInt("id"),queryRes.getString("nombre"),queryRes.getString("direccion"),queryRes.getString("pais"),queryRes.getString("departamento")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static Empresa findEmpresa(int id){
		ArrayList<Empresa> lista=findAllEmpresas();
		for(int i=0;i<lista.size();i++){
			Empresa actual=lista.get(i);
			if(actual.getId()==id){
				return actual;
			}
		}
		return null;
	}
	//metodos para uso con pais
	public static ArrayList<Pais> findAllPaises(){
		ArrayList<Pais> res= new ArrayList<Pais>();
		ResultSet queryRes=Conexion.executeQuery("SELECT * FROM pais");
		try {
			while(queryRes.next()){
				res.add(new Pais(queryRes.getInt("id"),queryRes.getString("nombre"),queryRes.getString("continente"),queryRes.getInt("codarea")));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return res;
	}
	public static Pais findPais(int id){
		ArrayList<Pais> lista=findAllPaises();
		for(int i=0;i<lista.size();i++){
			Pais actual=lista.get(i);
			if(actual.getId()==id){
				return actual;
			}
		}
		return null;
	}
}
