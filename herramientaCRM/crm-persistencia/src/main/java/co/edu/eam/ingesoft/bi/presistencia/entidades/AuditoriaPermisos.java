package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "AUDITORIA_PERMISOS")
public class AuditoriaPermisos implements Serializable {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "accion", length = 30, nullable = false)
	private String accion;

	@Column(name = "fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHora;

	@Column(name = "dispositivo", length = 30, nullable = false)
	private String dispositivo;

	@Column(name = "navegador", length = 30, nullable = false)
	private String navegador;

	@JoinColumns({ @JoinColumn(name = "modulo_id", referencedColumnName = "codigo_modulo"),
			@JoinColumn(name = "tipoUsiario_id", referencedColumnName = "codigo_tipoUsuario") })
	private ModulosUsuarioPK modulosUsuario;
	
	
	public AuditoriaPermisos(){
		
	}


	public AuditoriaPermisos(String accion, Date fechaHora, String dispositivo, String navegador,
			ModulosUsuarioPK modulosUsuario) {
		super();
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.modulosUsuario = modulosUsuario;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getAccion() {
		return accion;
	}


	public void setAccion(String accion) {
		this.accion = accion;
	}


	public Date getFechaHora() {
		return fechaHora;
	}


	public void setFechaHora(Date fechaHora) {
		this.fechaHora = fechaHora;
	}


	public String getDispositivo() {
		return dispositivo;
	}


	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}


	public String getNavegador() {
		return navegador;
	}


	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}


	public ModulosUsuarioPK getModulosUsuario() {
		return modulosUsuario;
	}


	public void setModulosUsuario(ModulosUsuarioPK modulosUsuario) {
		this.modulosUsuario = modulosUsuario;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accion == null) ? 0 : accion.hashCode());
		result = prime * result + ((dispositivo == null) ? 0 : dispositivo.hashCode());
		result = prime * result + ((fechaHora == null) ? 0 : fechaHora.hashCode());
		result = prime * result + id;
		result = prime * result + ((modulosUsuario == null) ? 0 : modulosUsuario.hashCode());
		result = prime * result + ((navegador == null) ? 0 : navegador.hashCode());
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuditoriaPermisos other = (AuditoriaPermisos) obj;
		if (accion == null) {
			if (other.accion != null)
				return false;
		} else if (!accion.equals(other.accion))
			return false;
		if (dispositivo == null) {
			if (other.dispositivo != null)
				return false;
		} else if (!dispositivo.equals(other.dispositivo))
			return false;
		if (fechaHora == null) {
			if (other.fechaHora != null)
				return false;
		} else if (!fechaHora.equals(other.fechaHora))
			return false;
		if (id != other.id)
			return false;
		if (modulosUsuario == null) {
			if (other.modulosUsuario != null)
				return false;
		} else if (!modulosUsuario.equals(other.modulosUsuario))
			return false;
		if (navegador == null) {
			if (other.navegador != null)
				return false;
		} else if (!navegador.equals(other.navegador))
			return false;
		return true;
	}
	
	
	

}
