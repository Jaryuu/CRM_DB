package modelos;

import java.util.ArrayList;

public class Pais {
	private int id;
	private String nombre;
	private String continente;
	private int codarea;
	public Pais(int id, String nombre, String continente, int codarea) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.continente = continente;
		this.codarea = codarea;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getContinente() {
		return continente;
	}
	public void setContinente(String continente) {
		this.continente = continente;
	}
	public int getCodarea() {
		return codarea;
	}
	public void setCodarea(int codarea) {
		this.codarea = codarea;
	}
	
	public String getAtr(String atr){
		switch(atr){
			case "id":
				return ""+id;
			case "nombre":
				return nombre;
			case "continente":
				return continente;
			case "codarea":
				return ""+codarea;
		}
		return "";
	}
}
