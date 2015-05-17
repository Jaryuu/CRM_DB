package modelos;

public class Carro {
	private int modelo;
	private String marca;
	private String color;
	private String transmision;
	
	public Carro(int modelo, String marca, String color, String transmision) {
		this.modelo = modelo;
		this.marca = marca;
		this.color = color;
		this.transmision = transmision;
	}
	public int getModelo() {
		return modelo;
	}
	public void setModelo(int modelo) {
		this.modelo = modelo;
	}
	public String getMarca() {
		return marca;
	}
	public void setMarca(String marca) {
		this.marca = marca;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getTransmision() {
		return transmision;
	}
	public void setTransmision(String transmision) {
		this.transmision = transmision;
	}
	
}
