package modelos;

public class Empresa {
	private int id;
	private String nombre;
	private String direccion;
	private String pais;
	private String departamento;
	public Empresa(int id, String nombre, String direccion, String pais, String departamento) {
		this.id = id;
		this.nombre = nombre;
		this.direccion = direccion;
		this.pais = pais;
		this.departamento = departamento;
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
	public String getDireccion() {
		return direccion;
	}
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	public String getPais() {
		return pais;
	}
	public void setPais(String pais) {
		this.pais = pais;
	}
	public String getDepartamento() {
		return departamento;
	}
	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
}
