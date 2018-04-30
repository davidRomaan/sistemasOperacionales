package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "PERSONA")
@NamedQueries({
	@NamedQuery(name=Persona.LISTA_PERSONA, query="SELECT p FROM Persona p")
})
public class Persona implements Serializable{
	
	public static final String LISTA_PERSONA = "lista.persona";
	
	@Id
	@Column(name = "cedula", nullable = false)
	private String cedula;

	@Column(name = "nombre", nullable = false)
	protected String nombre;

	@Column(name = "apellido", nullable = false)
	protected String apellido;

	@Column(name = "telefono")
	protected String telefono;

	@Column(name = "correo")
	protected String correo;

	@Column(name = "fecha_nacimiento")
	protected String fechaNacimiento;

	@JoinColumn(name = "minicipio_id")
	@ManyToOne
	protected Municipio municipio;

	@Enumerated(EnumType.STRING)
	@Column(name = "genero")
	protected Genero genero;

	public Persona() {
		// TODO Auto-generated constructor stub
	}

	public Persona(String cedula, String nombre, String apellido, String telefono, String correo, String fechaNacimiento,
			Municipio municipio, Genero genero) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.correo = correo;
		this.fechaNacimiento = fechaNacimiento;
		this.municipio = municipio;
		this.genero = genero;
	}

	public String getTelefono() {
		return telefono;
	}


	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public String getCorreo() {
		return correo;
	}


	public void setCorreo(String correo) {
		this.correo = correo;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}


	public Genero getGenero() {
		return genero;
	}


	public void setGenero(Genero genero) {
		this.genero = genero;
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

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}


}
