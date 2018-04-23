package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import javax.persistence.Table;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Table(name="LOTE")
public class Lote implements Serializable{
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="descripcion")
	private String descripcion;

	public Lote(int id, String descripcion) {
		super();
		this.id = id;
		this.descripcion = descripcion;
	}

	public Lote() {
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
	
	

}
