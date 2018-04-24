package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;

@Entity
@Table(name="USUARIO")
public class Usuario extends Persona implements Serializable {

	
	@Column(name="contrasenia", nullable= false)
	private String contrasenia;
	
	@Column(name="nombre_usuario", nullable= false, unique=true)
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
	
	
	public Usuario(){
		
	}


	public Usuario(String cedula, String nombre, String apellido, String telefono, String correo, Date fechaNacimiento,
			boolean activo, Municipio municipio, Genero genero, String contrasenia, String nombreUsuario,
			TipoUsuario tipoUsuario, Area area, Date fechaIngreso, Cargo cargo) {
		super(cedula, nombre, apellido, telefono, correo, fechaNacimiento, activo, municipio, genero);
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


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result + ((cargo == null) ? 0 : cargo.hashCode());
		result = prime * result + ((contrasenia == null) ? 0 : contrasenia.hashCode());
		result = prime * result + ((fechaIngreso == null) ? 0 : fechaIngreso.hashCode());
		result = prime * result + ((nombreUsuario == null) ? 0 : nombreUsuario.hashCode());
		result = prime * result + ((tipoUsuario == null) ? 0 : tipoUsuario.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.equals(other.cargo))
			return false;
		if (contrasenia == null) {
			if (other.contrasenia != null)
				return false;
		} else if (!contrasenia.equals(other.contrasenia))
			return false;
		if (fechaIngreso == null) {
			if (other.fechaIngreso != null)
				return false;
		} else if (!fechaIngreso.equals(other.fechaIngreso))
			return false;
		if (nombreUsuario == null) {
			if (other.nombreUsuario != null)
				return false;
		} else if (!nombreUsuario.equals(other.nombreUsuario))
			return false;
		if (tipoUsuario == null) {
			if (other.tipoUsuario != null)
				return false;
		} else if (!tipoUsuario.equals(other.tipoUsuario))
			return false;
		return true;
	}
	
	
	
}
