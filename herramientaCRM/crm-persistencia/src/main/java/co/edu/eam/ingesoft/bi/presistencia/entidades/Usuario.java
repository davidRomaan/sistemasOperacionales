package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;


@NamedQueries({
	@NamedQuery(name="Usuario.buscarUsuario",query="SELECT u FROM Usuario u WHERE u.nombreUsuario=?1")
})
@Entity
@Table(name="USUARIO")
public class Usuario extends Persona implements Serializable {

	
	@Column(name="contrasenia")
	private String contrasenia;
	
	@Column(name="nombre_usuario", unique=true)
	private String nombreUsuario;
	
	@JoinColumn(name="tipo_usuario")
	@ManyToOne
	private TipoUsuario tipoUsuario;
	
	@JoinColumn(name="area")
	@ManyToOne
	private Area area;
	
	@Column(name = "fecha_Ingreso")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaIngreso;
	
	@JoinColumn(name="cargo")
	@ManyToOne
	private Cargo cargo;
	
	@Column(name = "activo")
	private boolean activo;
	
	public Usuario(){
		
	}


	public Usuario(String cedula, String nombre, String apellido, String telefono, String correo, Date fechaNacimiento,
			boolean activo, Municipio municipio, Genero genero, String contrasenia, String nombreUsuario,
			TipoUsuario tipoUsuario, Area area, Date fechaIngreso, Cargo cargo) {
		super(cedula, nombre, apellido, telefono, correo, fechaNacimiento, municipio, genero);
		this.contrasenia = contrasenia;
		this.nombreUsuario = nombreUsuario;
		this.tipoUsuario = tipoUsuario;
		this.area = area;
		this.fechaIngreso = fechaIngreso;
		this.cargo = cargo;
	}


	public String getContrasenia() {
		return contrasenia;
	}


	public void setContrasenia(String contrasenia) {
		this.contrasenia = contrasenia;
	}


	public String getNombreUsuario() {
		return nombreUsuario;
	}


	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}


	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}


	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}


	public Area getArea() {
		return area;
	}


	public void setArea(Area area) {
		this.area = area;
	}


	public Date getFechaIngreso() {
		return fechaIngreso;
	}


	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}


	public Cargo getCargo() {
		return cargo;
	}


	public void setCargo(Cargo cargo) {
		this.cargo = cargo;
	}


	public boolean isActivo() {
		return activo;
	}


	public void setActivo(boolean activo) {
		this.activo = activo;
	}
	
}
