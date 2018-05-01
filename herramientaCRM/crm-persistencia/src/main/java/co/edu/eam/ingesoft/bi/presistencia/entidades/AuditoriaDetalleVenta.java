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
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="AUDITORIA_DETALLE_VENTA")
@NamedQueries({
	@NamedQuery(name=AuditoriaDetalleVenta.LISTA_AuditoriaDetalleVenta, query="SELECT p FROM AuditoriaDetalleVenta p")
})
public class AuditoriaDetalleVenta implements Serializable{
	
	public static final String LISTA_AuditoriaDetalleVenta = "lista.AuditoriaDetalleVenta";
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="seleccion")
	private String seleccion;
	
	@Column(name="fecha_hora")
	private Calendar fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="navegador")
	private String navegador;
	
	@Column(name="detalle_venta")
	private String detalleVenta;

	


	public AuditoriaDetalleVenta(int id, String seleccion, Calendar fechaHora, String dispositivo, String navegador,
			String detalleVenta) {
		super();
		this.id = id;
		this.seleccion = seleccion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.detalleVenta = detalleVenta;
	}


	public AuditoriaDetalleVenta() {
		super();
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


	public String getDetalleVenta() {
		return detalleVenta;
	}


	public void setDetalleVenta(String detalleVenta) {
		this.detalleVenta = detalleVenta;
	}


	
	
	

}
