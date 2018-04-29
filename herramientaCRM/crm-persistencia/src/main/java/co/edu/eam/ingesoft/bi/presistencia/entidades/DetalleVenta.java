package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(DetalleVentaPK.class)
@Table(name="DETALLE_VENTA")
public class DetalleVenta implements Serializable{
	
	@Id
	@JoinColumn(name="factura_venta_id")
	@ManyToOne
	private FacturaVenta facturaVenta;
	
	@Id
	@JoinColumn(name="producto_id")
	@ManyToOne
	private Producto producto;
	
	@Column(name="cantidad")
	private int cantidad;

	

	public DetalleVenta(FacturaVenta facturaVenta, Producto producto, int cantidad) {
		super();
		this.facturaVenta = facturaVenta;
		this.producto = producto;
		this.cantidad = cantidad;
	}



	public DetalleVenta() {
		super();
	}



	public FacturaVenta getFacturaVentaId() {
		return facturaVenta;
	}



	public void setFacturaVentaId(FacturaVenta facturaVentaId) {
		this.facturaVenta = facturaVentaId;
	}



	public Producto getProductoId() {
		return producto;
	}



	public void setProductoId(Producto productoId) {
		this.producto = productoId;
	}



	public int getCantidad() {
		return cantidad;
	}



	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	
	
	
	
	

}
