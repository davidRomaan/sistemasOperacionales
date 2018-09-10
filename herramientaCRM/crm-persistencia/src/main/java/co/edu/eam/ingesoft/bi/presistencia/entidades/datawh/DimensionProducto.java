package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="DIMENSION_PRODUCTO")
public class DimensionProducto implements Serializable {

	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="PRECIO")
	private double precio;
	
	@Column(name="TIPO_PRODUCTO")
	private String tipoProducto;
	
	public DimensionProducto() {
		// TODO Auto-generated constructor stub
	}

	public DimensionProducto(int id, String nombre, double precio, String tipoProducto) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.precio = precio;
		this.tipoProducto = tipoProducto;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getPrecio() {
		return precio;
	}

	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public String getTipoProducto() {
		return tipoProducto;
	}

	public void setTipoProducto(String tipoProducto) {
		this.tipoProducto = tipoProducto;
	}
	
	
}
