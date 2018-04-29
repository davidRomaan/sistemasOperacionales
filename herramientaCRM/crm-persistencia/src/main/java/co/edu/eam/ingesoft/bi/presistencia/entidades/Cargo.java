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

@NamedQueries({
	@NamedQuery(name=Cargo.LISTAR_CARGOS,query="SELECT c FROM Cargo c")
})
@Entity
@Table(name="CARGO")
public class Cargo implements Serializable {
	
	/**
	 * Lista los cargos registrados
	 */
	public static final String LISTAR_CARGOS = "Cargo.listar";
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="descripcion", length=30)
	private String descripcion;
	
	@Column(name="salario")
	private double salario;
	
	public Cargo() {
		// TODO Auto-generated constructor stub
	}
	
	public Cargo(int id, String descripcion, double salario) {
		super();
		this.id = id;
		this.descripcion = descripcion;
		this.salario = salario;
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

	public double getSalario() {
		return salario;
	}

	public void setSalario(double salario) {
		this.salario = salario;
	}
	
	

}
