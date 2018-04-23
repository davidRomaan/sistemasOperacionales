package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
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
	@JoinColumn(name="facturaVentaId")
	@ManyToOne
	private FacturaVenta facturaVentaId;
	
	@Id
	@JoinColumn(name="productoId")
	@ManyToOne
	private Producto productoId;
	
	@JoinColumn(name="cantidad")
	private float cantidad;

	

	public DetalleVenta(FacturaVenta facturaVentaId, Producto productoId, float cantidad) {
		super();
		this.facturaVentaId = facturaVentaId;
		this.productoId = productoId;
		this.cantidad = cantidad;
	}



	public DetalleVenta() {
		super();
	}



	public FacturaVenta getFacturaVentaId() {
		return facturaVentaId;
	}



	public void setFacturaVentaId(FacturaVenta facturaVentaId) {
		this.facturaVentaId = facturaVentaId;
	}



	public Producto getProductoId() {
		return productoId;
	}



	public void setProductoId(Producto productoId) {
		this.productoId = productoId;
	}



	public float getCantidad() {
		return cantidad;
	}



	public void setCantidad(float cantidad) {
		this.cantidad = cantidad;
	}

	
	
	
	
	

}
