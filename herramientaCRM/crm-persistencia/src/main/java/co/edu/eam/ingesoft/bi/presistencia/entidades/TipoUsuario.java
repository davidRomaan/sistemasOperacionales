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
@Table(name = "TIPO_USUARIO")
@NamedQueries({
	@NamedQuery(name=TipoUsuario.BUSCAR_NOMBRE, query="SELECT tp FROM TipoUsuario tp WHERE tp.nombre = ?1"),
	@NamedQuery(name=TipoUsuario.LISTAR, query="SELECT tp FROM TipoUsuario tp")
})
public class TipoUsuario implements Serializable{

	/**
	 * Busca un tipo de usuario por el nombre
	 * 1? nombre del tipo usuario a buscar
	 */
	public static final String BUSCAR_NOMBRE = "TipoUsuario.buscarNombre";
	
	/**
	 * Lista los tipos de usuario registrados
	 */
	public static final String LISTAR = "TipoUsuario.listar";
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;

	@Column(name = "nombre", length = 30)
	private String nombre;

	@Column(name = "descripcion", length = 100)
	private String descripcion;

	public TipoUsuario() {
		// TODO Auto-generated constructor stub
	}

	public TipoUsuario(String nombre, String descripcion) {
		super();
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
