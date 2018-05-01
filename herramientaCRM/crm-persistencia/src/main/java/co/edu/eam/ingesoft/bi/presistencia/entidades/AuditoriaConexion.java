package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AUDITORIA_CONEXION")
@NamedQueries({
	@NamedQuery(name=AuditoriaConexion.LISTA_AuditoriaConexion, query="SELECT p FROM AuditoriaConexion p")
})
public class AuditoriaConexion implements Serializable {
	
	public static final String LISTA_AuditoriaConexion = "lista.AuditoriaConexion";

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="seleccion", length=30)
	private String seleccion;
	
	@Column(name="fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHora;
	
	@Column(name="dispositivo", length=30)
	private String dispositivo;
	
	@Column(name="navegador", length=30)
	private String navegador;
	
	@Column(name="conexion")
	private String conexion;	
	
	public AuditoriaConexion() {
		// TODO Auto-generated constructor stub
	}

	public AuditoriaConexion(int id, String seleccion, Date fechaHora, String dispositivo, String navegador,
			String conexion) {
		super();
		this.id = id;
		this.seleccion = seleccion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.conexion = conexion;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSeleccion() {
		return seleccion;
	}

	public void setSeleccion(String seleccion) {
		this.seleccion = seleccion;
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

	public String getConexion() {
		return conexion;
	}

	public void setConexion(String conexion) {
		this.conexion = conexion;
	}

	
	
	
}
