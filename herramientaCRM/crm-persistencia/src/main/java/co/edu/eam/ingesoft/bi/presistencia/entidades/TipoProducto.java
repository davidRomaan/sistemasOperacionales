package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="TIPO_PRODUCTO")
@NamedQueries({
	@NamedQuery(name=TipoProducto.LISTAR, query="SELECT tp FROM TipoProducto tp")
})
public class TipoProducto implements Serializable {

	/**
	 * Carga la lista de tipos de producto registrados en la base de datos
	 */
	public static final String LISTAR = "tipoProducto.listar";
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="nombre")
	private String nombre;
	
	@Column(name="descripcion")
	private String descripcion;
	
	public TipoProducto() {
		// TODO Auto-generated constructor stub
	}

	public TipoProducto(int id, String nombre, String descripcion) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.descripcion = descripcion;
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

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
}
