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
import javax.persistence.Table;

@Entity
@Table(name="AUDITORIA_DETALLE_VENTA")
public class AuditoriaDetalleVenta implements Serializable{
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="seleccion")
	private String seleccion;
	
	@Column(name="fecha_hora")
	private Date fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="navegador")
	private String navegador;
	
	@JoinColumn(name="producto_id")
	@ManyToOne
	private DetalleVenta productoId;
	
	
	@JoinColumn(name="factura_venta_id")
	@ManyToOne
	private DetalleVenta facturaVentaId;


	public AuditoriaDetalleVenta(int id, String seleccion, Date fechaHora, String dispositivo, String navegador,
			DetalleVenta productoId, DetalleVenta facturaVentaId) {
		super();
		this.id = id;
		this.seleccion = seleccion;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.navegador = navegador;
		this.productoId = productoId;
		this.facturaVentaId = facturaVentaId;
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


	public DetalleVenta getProductoId() {
		return productoId;
	}


	public void setProductoId(DetalleVenta productoId) {
		this.productoId = productoId;
	}


	public DetalleVenta getFacturaVentaId() {
		return facturaVentaId;
	}


	public void setFacturaVentaId(DetalleVenta facturaVentaId) {
		this.facturaVentaId = facturaVentaId;
	}
	
	

}
