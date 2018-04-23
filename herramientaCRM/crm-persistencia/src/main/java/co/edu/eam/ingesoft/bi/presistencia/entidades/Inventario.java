package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="Inventario")
public class Inventario implements Serializable{
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="cantidad", length=30)
	private int cantidad;
	
	@Column(name="descripcion")
	private String descripcion;

	public Inventario(int id, int cantidad, String descripcion) {
		super();
		this.id = id;
		this.cantidad = cantidad;
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

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
    
	
	

}
