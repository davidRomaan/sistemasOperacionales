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
@Table(name="PRODUCTO")
public class Producto implements Serializable{

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="fecha_ingreso")
	private Date fechaIngreso;
	
	@Column(name="peso")
	private double peso;
	
	@Column(name="dimension")
	private double dimension;
	
	@Column(name="valor_producto")
	private double valorProducto;
	
	@JoinColumn(name="lote_id")
	@ManyToOne
	private Lote loteId;
	
	
	@JoinColumn(name="usuario_persona_id")
	@ManyToOne
	private Persona usuairoPersonaId;


	public Producto(int id, Date fechaIngreso, double peso, double dimension, double valorProducto, Lote loteId,
			Persona usuairoPersonaId) {
		super();
		this.id = id;
		this.fechaIngreso = fechaIngreso;
		this.peso = peso;
		this.dimension = dimension;
		this.valorProducto = valorProducto;
		this.loteId = loteId;
		this.usuairoPersonaId = usuairoPersonaId;
	}


	public Producto() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public Date getFechaIngreso() {
		return fechaIngreso;
	}


	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}


	public double getPeso() {
		return peso;
	}


	public void setPeso(double peso) {
		this.peso = peso;
	}


	public double getDimension() {
		return dimension;
	}


	public void setDimension(double dimension) {
		this.dimension = dimension;
	}


	public double getValorProducto() {
		return valorProducto;
	}


	public void setValorProducto(double valorProducto) {
		this.valorProducto = valorProducto;
	}


	public Lote getLoteId() {
		return loteId;
	}


	public void setLoteId(Lote loteId) {
		this.loteId = loteId;
	}


	public Persona getUsuairoPersonaId() {
		return usuairoPersonaId;
	}


	public void setUsuairoPersonaId(Persona usuairoPersonaId) {
		this.usuairoPersonaId = usuairoPersonaId;
	}
	
	
	
}
