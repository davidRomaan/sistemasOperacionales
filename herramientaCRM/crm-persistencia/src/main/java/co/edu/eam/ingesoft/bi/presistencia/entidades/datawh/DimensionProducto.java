package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

public class DimensionProducto implements Serializable {

	private int id;
	private String nombre;
	private double precio;
	private String tipoProducto;
	
	public DimensionProducto() {
		// TODO Auto-generated constructor stub
	}

	public DimensionProducto(int id, String nombre, double precio, String tipoProducto) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.tipoProducto = tipoProducto;
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

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	
	
	
}
