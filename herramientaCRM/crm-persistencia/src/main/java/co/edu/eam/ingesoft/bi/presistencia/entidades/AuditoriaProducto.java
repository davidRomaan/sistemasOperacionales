package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name="AUDITORIA_PRODUCTO")
@NamedQueries({
	@NamedQuery(name=AuditoriaProducto.LISTA_AuditoriaProducto, query="SELECT p FROM AuditoriaProducto p")
})
public class AuditoriaProducto implements Serializable{
	
	public static final String LISTA_AuditoriaProducto = "lista_AuditoriaProducto";

	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="fecha_hora")
	private Calendar fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="accion")
	private String accion;
	
	@Column(name="navegador")
	private String navegador;
	
	@Column(name="producto_id")
	private String producto;
	
	public AuditoriaProducto() {
		// TODO Auto-generated constructor stub
	}

	
	
	public AuditoriaProducto(int id, Calendar fechaHora, String dispositivo, String accion, String navegador,
			String producto) {
		super();
		this.id = id;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.accion = accion;
		this.navegador = navegador;
		this.producto = producto;
	}



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public Calendar getFechaHora() {
		return fechaHora;
	}



	public void setFechaHora(Calendar fechaHora) {
		this.fechaHora = fechaHora;
	}



	public String getDispositivo() {
		return dispositivo;
	}



	public void setDispositivo(String dispositivo) {
		this.dispositivo = dispositivo;
	}



	public String getAccion() {
		return accion;
	}



	public void setAccion(String accion) {
		this.accion = accion;
	}



	public String getNavegador() {
		return navegador;
	}



	public void setNavegador(String navegador) {
		this.navegador = navegador;
	}



	public String getProducto() {
		return producto;
	}



	public void setProducto(String producto) {
		this.producto = producto;
	}



	
	
}
