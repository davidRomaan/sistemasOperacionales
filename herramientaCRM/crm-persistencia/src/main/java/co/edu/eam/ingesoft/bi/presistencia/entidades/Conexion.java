package co.edu.eam.ingesoft.bi.presistencia.entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CONEXION")
public class Conexion {
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="base_datos")
	private String baseDatos;
	
	public Conexion() {
		// TODO Auto-generated constructor stub
	}

	public Conexion(int id, String baseDatos) {
		super();
		this.id = id;
		this.baseDatos = baseDatos;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getBaseDatos() {
		return baseDatos;
	}

	public void setBaseDatos(String baseDatos) {
		this.baseDatos = baseDatos;
	}
	

}
