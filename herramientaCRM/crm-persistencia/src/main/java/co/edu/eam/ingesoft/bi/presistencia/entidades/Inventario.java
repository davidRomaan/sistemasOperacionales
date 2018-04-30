package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Inventario")
@NamedQueries({ @NamedQuery(name = Inventario.LISTAR, query = "SELECT i FROM Inventario i"),
		@NamedQuery(name = Inventario.BUSCAR_NOMBRE, query = "SELECT i FROM Inventario i WHERE i.nombre = ?1") })
public class Inventario implements Serializable {

	public final static String LISTAR = "inventario.listar";

	/**
	 * Busca un inventario por el nombre ?1 nombre
	 */
	public final static String BUSCAR_NOMBRE = "Inventario.buscarNombre";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "nombre")
	private String nombre;

	@Column(name = "descripcion")
	private String descripcion;

	public Inventario(String nombre, String descripcion) {
		super();
		this.nombre = nombre;
		this.descripcion = descripcion;
	}

	public Inventario() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	
	
	

}
