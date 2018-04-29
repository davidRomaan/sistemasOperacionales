package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

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
@IdClass(InventarioProductoPK.class)
@Table(name="INVENTARIO_PRODUCTO")
@NamedQueries({
	@NamedQuery(name=InventarioProducto.LISTAR, query="SELECT ip FROM InventarioProducto ip")
})
public class InventarioProducto implements Serializable{
	
	/**
	 * Lista los inventarios de los productos
	 */
	public static final String LISTAR = "ip.listar";
	
	@Id
	@JoinColumn(name="inventario_id")
	@ManyToOne
	private Inventario inventarioId;
	
	@Id
	@JoinColumn(name="producto_id")
	@ManyToOne
	private Producto productoId;
	
	@Column(name="cantidad", length=30)
	private int cantidad;

	public InventarioProducto() {
		// TODO Auto-generated constructor stub
	}

	public InventarioProducto(Inventario inventarioId, int cantidad, Producto productoId) {
		super();
		this.inventarioId = inventarioId;
		this.cantidad = cantidad;
		this.productoId = productoId;
	}
	
	

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Inventario getInventarioId() {
		return inventarioId;
	}

	public void setInventarioId(Inventario inventarioId) {
		this.inventarioId = inventarioId;
	}

	public Producto getProductoId() {
		return productoId;
	}

	public void setProductoId(Producto productoId) {
		this.productoId = productoId;
	}
	
	
	

}
