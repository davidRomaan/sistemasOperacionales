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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AUDITORIA_PERSONA")
@NamedQueries({
	@NamedQuery(name=AuditoriaPersona.LISTA, query="SELECT p FROM AuditoriaPersona p")
})
public class AuditoriaPersona implements Serializable {
	
	public static final String LISTA = "lista";

	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="accion", length=30)
	private String accion;
	
	@Column(name="fecha_hora")
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar fechaHora;
	
	@Column(name="dispositivo", length=30)
	private String dispositivo;
	
	@Column(name="navegador", length=30)
	private String navegador;
	
	@Column(name="persona_id")
	private String persona;
	
	public AuditoriaPersona() {
		// TODO Auto-generated constructor stub
	}

	

	public AuditoriaPersona(int id, String accion, Calendar fechaHora, String dispositivo, String navegador,
			String persona) {
		super();
		this.id = id;
		this.accion = accion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.persona = persona;
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



	public String getPersona() {
		return persona;
	}



	public void setPersona(String persona) {
		this.persona = persona;
	}



	
	
	
	
}
