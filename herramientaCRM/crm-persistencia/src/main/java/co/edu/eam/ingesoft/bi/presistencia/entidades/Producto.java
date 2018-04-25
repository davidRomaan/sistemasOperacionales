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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="PRODUCTO")
@NamedQueries({
	@NamedQuery(name=Producto.LISTAR, query="SELECT p FROM Producto p")
})
public class Producto implements Serializable{
	
	/**
	 * Lista los productos registrados
	 */
	public static final String LISTAR = "producto.listar";

	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="descripcion", length=100)
	private String descripcion;
	
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
	private Lote lote;
	
	
	@JoinColumn(name="usuario_persona_id")
	@ManyToOne
	private Persona usuairoPersonaId;


	


	public Producto(int id, String nombre, String descripcion, Date fechaIngreso, double peso, double dimension,
			double valorProducto, Lote loteId, Persona usuairoPersonaId) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
		this.fechaIngreso = fechaIngreso;
		this.peso = peso;
		this.dimension = dimension;
		this.valorProducto = valorProducto;
		this.lote = loteId;
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


	public Lote getLote() {
		return lote;
	}


	public void setLote(Lote lote) {
		this.lote = lote;
	}


	public Persona getUsuairoPersonaId() {
		return usuairoPersonaId;
	}


	public void setUsuairoPersonaId(Persona usuairoPersonaId) {
		this.usuairoPersonaId = usuairoPersonaId;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	
}
