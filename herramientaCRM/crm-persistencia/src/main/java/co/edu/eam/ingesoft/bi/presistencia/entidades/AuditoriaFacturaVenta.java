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

@Entity
@Table(name="AUDITORIA_FACTURA_VENTA")
@NamedQueries({
	@NamedQuery(name=AuditoriaFacturaVenta.LISTA_FACTURA_VENTA, query="SELECT p FROM AuditoriaFacturaVenta p")
})
public class AuditoriaFacturaVenta implements Serializable{
	
	public static final String LISTA_FACTURA_VENTA = "lista.facturaVenta";

	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="fecha_hora")
	private Calendar fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="accion")
	private String accion;
	
	@Column(name="navegador")
	private String navegador;
	
	@Column(name="factura_venta")
	private String facturaVenta;
	
	public AuditoriaFacturaVenta() {
		// TODO Auto-generated constructor stub
	}

	

	public AuditoriaFacturaVenta(int id, Calendar fechaHora, String dispositivo, String accion, String navegador,
			String facturaVenta) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.accion = accion;
		this.navegador = navegador;
		this.facturaVenta = facturaVenta;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
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



	public String getFacturaVenta() {
		return facturaVenta;
	}



	public void setFacturaVenta(String facturaVenta) {
		this.facturaVenta = facturaVenta;
	}



	
	
	
}
