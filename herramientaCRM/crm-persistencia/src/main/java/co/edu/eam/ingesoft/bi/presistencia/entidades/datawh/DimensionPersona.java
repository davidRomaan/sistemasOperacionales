package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

public class DimensionPersona implements Serializable {

	private String cedula;
	private String nombre;
	private String apellido;
	private String genero;
	private short edad;
	private String tipoPersona;
	
	public DimensionPersona() {
		// TODO Auto-generated constructor stub
	}

	public DimensionPersona(String cedula, String nombre, String apellido, String genero, short edad,
			String tipoPersona) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;
		this.edad = edad;
		this.tipoPersona = tipoPersona;
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public short getEdad() {
		return edad;
	}

	public void setEdad(short edad) {
		this.edad = edad;
	}

	public String getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(String tipoPersona) {
		this.tipoPersona = tipoPersona;
	}
	
	
	
}
