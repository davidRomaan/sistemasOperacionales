package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

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
@Table(name="MUNICIPIO")
@NamedQueries({
	@NamedQuery(name=Municipio.LISTAR_MUNICIPIO_DEPTO, query="SELECT m FROM Municipio m WHERE m.departamento = ?1"),
	@NamedQuery(name=Municipio.LISTAR_MUNICIPIO, query="SELECT m FROM Municipio m"),
	@NamedQuery(name=Municipio.LISTAR_MUNICIPIO_NOM, query="SELECT m FROM Municipio m WHERE m.nombre = ?1")
})
public class Municipio implements Serializable{
	
	/**
	 * Lista los municipios de un departamento registrados en la base de datos
	 * ?1 codigo del departamento
	 */
	public static final String LISTAR_MUNICIPIO_DEPTO = "municipio.listar";
	
	public static final String LISTAR_MUNICIPIO_NOM = "municipioNom.listar";
	
	public static final String LISTAR_MUNICIPIO = "municipioTodos.listar";
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;

	@Column(name="nombre")
	private String nombre;
	
	@JoinColumn(name="departamento_id")
	@ManyToOne
	private Departamento departamento;

	
	public Municipio(){
		
	}
	
	public Municipio(int id, String nombre, Departamento departamento) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.departamento = departamento;
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

	public Departamento getDepartamento() {
		return departamento;
	}

	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((departamento == null) ? 0 : departamento.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Municipio other = (Municipio) obj;
		if (departamento == null) {
			if (other.departamento != null)
				return false;
		} else if (!departamento.equals(other.departamento))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}
	
	
}
