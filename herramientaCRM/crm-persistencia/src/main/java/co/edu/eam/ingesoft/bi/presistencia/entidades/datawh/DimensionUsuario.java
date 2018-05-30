package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DIMENSION_USUARIO")
public class DimensionUsuario implements Serializable {

	
	@Id
	@Column(name="CEDULA")
	private String cedula;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="APELLIDO")
	private String apellido;
	
	@Column(name="GENERO")
	private String genero;
	
	@Column(name="EDAD")
	private int edad;
	
	@Column(name="TIPO_USUARIO")
	private String tipoUsuario;
	
	@Column(name="CARGO")
	private String cargo;
	
	public DimensionUsuario() {
		// TODO Auto-generated constructor stub
	}

	public DimensionUsuario(String cedula, String nombre, String apellido, String genero, int edad,
			String tipoUsuario, String cargo) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.genero = genero;
		this.edad = edad;
		this.tipoUsuario = tipoUsuario;
		this.cargo = cargo;
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

	public int getEdad() {
		return edad;
	}

	public void setEdad(int edad) {
		this.edad = edad;
	}

	public String getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(String tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}
	
	
	
}
