package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@IdClass(DetalleVentaPK.class)
@Table(name = "DETALLE_VENTA")
@NamedQueries({
		@NamedQuery(name = DetalleVenta.LISTAR_DETALLES_FACTURA, query = "SELECT dv FROM DetalleVenta dv"
				+ " WHERE dv.facturaVenta = ?1") })
public class DetalleVenta implements Serializable {

	public static final String LISTAR_DETALLES_FACTURA = "detalle.listarDetalleFactura";

	@Id
	@JoinColumn(name = "factura_venta_id")
	@ManyToOne(cascade = {})
	private FacturaVenta facturaVenta;

	@Id
	@JoinColumn(name = "producto_id")
	@ManyToOne(cascade = {})
	private Producto producto;

	@Column(name = "cantidad")
	private int cantidad;

	@Column(name = "subtotal")
	private double subtotal;

	public DetalleVenta(FacturaVenta facturaVenta, Producto producto, int cantidad) {
		super();
		this.facturaVenta = facturaVenta;
		this.producto = producto;
		this.cantidad = cantidad;
	}

	public DetalleVenta() {
		super();
	}

	public FacturaVenta getFacturaVenta() {
		return facturaVenta;
	}

	public void setFacturaVenta(FacturaVenta facturaVenta) {
		this.facturaVenta = facturaVenta;
	}

	public Producto getProducto() {
		return producto;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

}
