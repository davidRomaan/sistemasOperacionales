package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name="FOTO")
public class Foto implements Serializable{
	
	@Id
	@Column(name="ID")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int id;
	
	@Column(name="DESCRIPCION")
	private String descripcion;
	
	@Column(name="FECHA")
	@Temporal(TemporalType.DATE)
	protected Date fecha;
	
	@Column(name="RESULTADO")
	@Lob()
	private byte[] resultado;
	
	public Foto() {
		// TODO Auto-generated constructor stub
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public byte[] getResultado() {
		return resultado;
	}

	public void setResultado(byte[] resultado) {
		this.resultado = resultado;
	}
	
}
