package modelos;

public class Aseguradora {
	private int id;
	private String nombre;
	private String descripcion;
	private float precio;
	
	public Aseguradora(int id, String nombre, String descripcion, float precio) {
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.precio = precio;
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public float getPrecio() {
		return precio;
	}
	public void setPrecio(float precio) {
		this.precio = precio;
	}
	public String getAtr(String atr){
		switch(atr){
			case "id":
				return ""+id;
			case "nombre":
				return nombre;
			case "descripcion":
				return descripcion;
			case "precio":
				return ""+precio;
		}
		return "";
	}
}
