package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.hibernate.annotations.Generated;

@Entity
@Table(name="DIMENSION_MUNICIPIO")
@NamedQueries({
	@NamedQuery(name = DimensionMunicipio.BUSCAR_NOMBRE, query = "SELECT m FROM DimensionMunicipio m WHERE m.nombre = ?1")	
})
public class DimensionMunicipio implements Serializable{

	public static final String BUSCAR_NOMBRE = "DimensionMunicipio.buscar_nombre";
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="NOMBRE")
	private String nombre;
	
	@Column(name="DEPARTAMENTO")
	private String departamento;
	
	public DimensionMunicipio() {
		// TODO Auto-generated constructor stub
	}

	public DimensionMunicipio(int id, String nombre, String departamento) {
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

	public String getDepartamento() {
		return departamento;
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}
	
	
	
}