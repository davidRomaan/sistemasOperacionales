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
	@JoinColumn(name="inventarioId")
	@ManyToOne
	private Modulo inventarioId;
	
	@Id
	@JoinColumn(name="productoId")
	@ManyToOne
	private TipoUsuario productoId;

	public InventarioProducto(Modulo inventarioId, TipoUsuario productoId) {
		super();
		this.inventarioId = inventarioId;
		this.productoId = productoId;
	}

	public InventarioProducto() {
		super();
	}

	public Modulo getInventarioId() {
		return inventarioId;
	}

	public void setInventarioId(Modulo inventarioId) {
		this.inventarioId = inventarioId;
	}

	public TipoUsuario getProductoId() {
		return productoId;
	}

	public void setProductoId(TipoUsuario productoId) {
		this.productoId = productoId;
	}
	
	

}
