package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="AUDITORIA")
@NamedQueries({
	@NamedQuery(name=Auditoria.LISTA_AUDITORIA, query="SELECT a FROM Auditoria a WHERE a.referencia = ?1"),
	@NamedQuery(name=Auditoria.LISTA_AUDITORIA_FECHA_ACT, query="SELECT a FROM Auditoria a WHERE a.fechaHora = ?1")
})
public class Auditoria implements Serializable{
	
	public static final String LISTA_AUDITORIA = "lista_auditoria";
	public static final String LISTA_AUDITORIA_FECHA_ACT = "fecha_actual";


	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="fecha_hora")
	@Temporal(TemporalType.DATE)
	private Calendar fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="accion")
	private String accion;
	
	@Column(name="navegador")
	private String navegador;
	
	@Column(name="descripcion")
	private String descripcion;
	
	@Column(name="referencia")
	private String referencia;
	
	@Column(name="usuario_acciones")
	private String usuario;
	
	public Auditoria() {
		// TODO Auto-generated constructor stub
	}

	

	public Auditoria(int id, Calendar fechaHora, String dispositivo, String accion, String navegador,
			String descripcion, String referencia, String usuario) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.accion = accion;
		this.navegador = navegador;
		this.descripcion = descripcion;
		this.referencia = referencia;
		this.usuario = usuario;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFecha() {
		
		return this.fechaHora.get(Calendar.YEAR) + "/" + this.fechaHora.get(Calendar.MONTH) + "/" + this.fechaHora.get(Calendar.DAY_OF_MONTH);
	}
	
	public Calendar getFechaHora(){
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

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getNavegador() {
		return navegador;
	}

	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}



	public String getUsuario() {
		return usuario;
	}



	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	
	
}
