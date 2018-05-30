package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "HECHO_AUDITORIA")
@NamedQuery(name = HechoAuditoria.LISTAR, query = "SELECT ha FROM HechoAuditoria ha")
public class HechoAuditoria implements Serializable {

	/**
	 * Lista los hechos de auditoria registrados en la bd
	 */
	public static final String LISTAR = "HechoAuditoria.listar";

	@Id
	@Column(name = "ID")
	private int id;

	@Column(name = "ACCION")
	private String accion;

	@Column(name = "DISPOSITIVO")
	private String dispositivo;

	@Column(name = "NAVEGADOR")
	private String navegador;

	@Column(name = "FECHA")
	@Temporal(TemporalType.DATE)
	private Calendar fecha;

	@JoinColumn(name = "CEDULA_USUARIO")
	@ManyToOne
	private DimensionUsuario usuario;

	public HechoAuditoria() {
		// TODO Auto-generated constructor stub
	}

	public HechoAuditoria(String accion, String dispositivo, String navegador, Calendar fecha,
			DimensionUsuario usuario) {
		super();
		this.accion = accion;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.fecha = fecha;
		this.usuario = usuario;
	}

	public String getFechaHora() {

		int mes = this.fecha.get(Calendar.MONTH) + 1;
		return this.fecha.get(Calendar.YEAR) + "/" + mes + "/" + this.fecha.get(Calendar.DAY_OF_MONTH);
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
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

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public DimensionUsuario getUsuario() {
		return usuario;
	}

	public void setUsuario(DimensionUsuario usuario) {
		this.usuario = usuario;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
