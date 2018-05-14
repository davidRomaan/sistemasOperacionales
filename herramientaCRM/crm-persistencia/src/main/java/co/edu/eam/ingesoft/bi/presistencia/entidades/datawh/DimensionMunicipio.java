package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

public class DimensionMunicipio implements Serializable{

	private int id;
	private String nombre;
	private String departamento;
	
	public DimensionMunicipio() {
		// TODO Auto-generated constructor stub
	}

	public DimensionMunicipio(int id, String nombre, String departamento) {
		super();
		this.id = id;
		this.nombre = nombre;
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

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	
	
}
