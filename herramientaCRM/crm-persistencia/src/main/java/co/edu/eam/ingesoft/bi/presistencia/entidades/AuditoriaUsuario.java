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
	
	@Column(name="usuarioId")
	private String usuarioId;
	
	@Column(name="resultadoAccion")
	private String resultadoAccion;
	
	
	public AuditoriaUsuario(){
		
	}


	


	public AuditoriaUsuario(int id, String accion, Calendar fechaHora, String dispositivo, String navegador,
			String usuarioId, String resultadoAccion) {
		super();
		this.id = id;
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.usuarioId = usuarioId;
		this.resultadoAccion = resultadoAccion;
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





	public String getUsuarioId() {
		return usuarioId;
	}





	public void setUsuarioId(String usuarioId) {
		this.usuarioId = usuarioId;
	}





	public String getResultadoAccion() {
		return resultadoAccion;
	}





	public void setResultadoAccion(String resultadoAccion) {
		this.resultadoAccion = resultadoAccion;
	}





	
	
	
}
