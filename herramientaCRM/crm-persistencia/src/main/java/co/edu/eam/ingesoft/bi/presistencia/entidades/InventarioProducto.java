package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@IdClass(InventarioProductoPK.class)
@Table(name="INVENTARIO_PRODUCTO")
public class InventarioProducto implements Serializable{
	
	@Id
	@JoinColumn(name="inventario_id")
	@ManyToOne
	private Inventario inventarioId;
	
	@Id
	@JoinColumn(name="producto_id")
	@ManyToOne
	private Producto productoId;

	public InventarioProducto() {
		// TODO Auto-generated constructor stub
	}

	public InventarioProducto(Inventario inventarioId, Producto productoId) {
		super();
		this.inventarioId = inventarioId;
		this.productoId = productoId;
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
