package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Entity
@Table(name="AUDITORIA_INVENTARIO")
@NamedQueries({
	@NamedQuery(name=AuditoriaInventario.LISTA_AuditoriaInventario, query="SELECT p FROM AuditoriaInventario p")
})
public class AuditoriaInventario implements Serializable{
	
	public static final String LISTA_AuditoriaInventario = "lista.AuditoriaInventario";
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="nombre_producto")
	private String nombreProducto;
	
	@Column(name="fecha_hora")
	private Calendar fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="accion")
	private String accion;
	
	@Column(name="navegador")
	private String navegador;
	
	@Column(name="id_Inventario")
	private String inventario;

	

	public AuditoriaInventario(int id, String nombreProducto, Calendar fechaHora, String dispositivo, String accion,
			String navegador, String inventario) {
		super();
		this.id = id;
		this.nombreProducto = nombreProducto;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.accion = accion;
		this.navegador = navegador;
		this.inventario = inventario;
	}


	public AuditoriaInventario() {
		super();
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombreProducto() {
		return nombreProducto;
	}


	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
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


	public String getInventario() {
		return inventario;
	}


	public void setInventario(String inventario) {
		this.inventario = inventario;
	}
	

	
	

}
