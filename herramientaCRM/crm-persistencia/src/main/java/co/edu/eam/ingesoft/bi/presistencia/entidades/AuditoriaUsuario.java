package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AUDITORIA_USUARIO")
public class AuditoriaUsuario implements Serializable {

	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="accion", length=30, nullable = false)
	private String accion;
	
	@Column(name="fecha_hora", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaHora;
	
	@Column(name="dispositivo", length=30,nullable = false)
	private String dispositivo;
	
	@Column(name="navegador", length=30,nullable = false)
	private String navegador;
	
	@JoinColumn(name="usuarioId")
	@ManyToOne
	private Usuario usuarioId;
	
	@Column(name="resultadoAccion")
	private String resultadoAccion;
	
	
	public AuditoriaUsuario(){
		
	}


	public AuditoriaUsuario(String accion, Calendar fechaHora, String dispositivo, String navegador,
			Usuario usuarioId, String resultadoAccion) {
		super();
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.usuarioId = usuarioId;
		this.resultadoAccion = resultadoAccion;
	}


	public String getAccion() {
		return accion;
	}


	public void setAccion(String accion) {
		this.accion = accion;
	}


	public Calendar getFechaHora() {
		return fechaHora;
	}


	public void setFechaHora(Calendar fechaHora) {
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


	public Usuario getUsuarioId() {
		return usuarioId;
	}


	public void setUsuarioId(Usuario usuarioId) {
		this.usuarioId = usuarioId;
	}


	public String getResultadoAccion() {
		return resultadoAccion;
	}


	public void setResultadoAccion(String resultadoAccion) {
		this.resultadoAccion = resultadoAccion;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accion == null) ? 0 : accion.hashCode());
		result = prime * result + ((dispositivo == null) ? 0 : dispositivo.hashCode());
		result = prime * result + ((fechaHora == null) ? 0 : fechaHora.hashCode());
		result = prime * result + id;
		result = prime * result + ((navegador == null) ? 0 : navegador.hashCode());
		result = prime * result + ((resultadoAccion == null) ? 0 : resultadoAccion.hashCode());
		result = prime * result + ((usuarioId == null) ? 0 : usuarioId.hashCode());
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
		AuditoriaUsuario other = (AuditoriaUsuario) obj;
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
		if (navegador == null) {
			if (other.navegador != null)
				return false;
		} else if (!navegador.equals(other.navegador))
			return false;
		if (resultadoAccion == null) {
			if (other.resultadoAccion != null)
				return false;
		} else if (!resultadoAccion.equals(other.resultadoAccion))
			return false;
		if (usuarioId == null) {
			if (other.usuarioId != null)
				return false;
		} else if (!usuarioId.equals(other.usuarioId))
			return false;
		return true;
	}
	
	
}
