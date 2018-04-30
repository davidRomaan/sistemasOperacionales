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
@Table(name = "INVENTARIO_PRODUCTO")
@NamedQueries({ @NamedQuery(name = InventarioProducto.LISTAR, query = "SELECT ip FROM InventarioProducto ip"),
		@NamedQuery(name = InventarioProducto.BUSCAR_INVENTARIO_PRODUCTO, query = "SELECT ip FROM InventarioProducto ip "
				+ "WHERE ip.productoId = ?1") })
public class InventarioProducto implements Serializable {

	/**
	 * Lista los inventarios de los productos
	 */
	public static final String LISTAR = "ip.listar";

	/**
	 * Busca el inventario de un producto por producto ?1 el producto
	 */
	public static final String BUSCAR_INVENTARIO_PRODUCTO = "ip.buscarProducto";

	@Id
	@JoinColumn(name = "inventario_id")
	@ManyToOne(cascade={})
	private Inventario inventarioId;

	@Id
	@JoinColumn(name = "producto_id")
	@ManyToOne(cascade={})
	private Producto productoId;

	@Column(name = "cantidad", length = 30)
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((inventarioId == null) ? 0 : inventarioId.hashCode());
		result = prime * result + ((productoId == null) ? 0 : productoId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		InventarioProducto other = (InventarioProducto) obj;
		if (inventarioId == null) {
			if (other.inventarioId != null)
				return false;
		} else if (!inventarioId.equals(other.inventarioId))
			return false;
		if (productoId == null) {
			if (other.productoId != null)
				return false;
		} else if (!productoId.equals(other.productoId))
			return false;
		return true;
	}

}
