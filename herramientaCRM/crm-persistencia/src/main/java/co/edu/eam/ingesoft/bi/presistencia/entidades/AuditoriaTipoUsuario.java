package co.edu.eam.ingesoft.bi.presistencia.entidades;

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
@Table(name="AUDITORIA_TIPO_USUARIO")
public class AuditoriaTipoUsuario {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="accion", length=30)
	private String accion;
	
	@Column(name="fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHora;
	
	@Column(name="dispositivo", length=30)
	private String dispositivo;
	
	@Column(name="navegador", length=30)
	private String navegador;
	
	@JoinColumn(name="tipo_usuario_id")
	@ManyToOne
	private TipoUsuario tipoUsuario;
	
	public AuditoriaTipoUsuario() {
		// TODO Auto-generated constructor stub
	}

	public AuditoriaTipoUsuario(int id, String accion, Date fechaHora, String dispositivo, String navegador,
			TipoUsuario tipoUsuario) {
		super();
		this.id = id;
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.tipoUsuario = tipoUsuario;
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

	public TipoUsuario getTipoUsuario() {
		return tipoUsuario;
	}

	public void setTipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	
	

}
