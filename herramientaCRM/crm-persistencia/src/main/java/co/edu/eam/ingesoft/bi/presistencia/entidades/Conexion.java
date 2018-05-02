package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="CONEXION")
public class Conexion implements Serializable{
	
	@Id
	@Column(name="id")
	private int id;
	
	@Column(name="base_datos")
	private String baseDatos;
	
	@Column(name="codigo")
	private int codigo;
	
	public Conexion() {
		// TODO Auto-generated constructor stub
	}

	public Conexion(int id, String baseDatos, int codigo) {
		super();
		this.id = id;
		this.baseDatos = baseDatos;
		this.codigo = codigo;
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

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	

}
