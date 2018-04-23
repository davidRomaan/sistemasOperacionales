package co.edu.eam.ingesoft.bi.presistencia.entidades;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="AUDITORIA_INVENTARIO")
public class AuditoriaInventario implements Serializable{
	
	@Id
	@Column(name="id")
	@GeneratedValue(strategy= GenerationType.AUTO)
	private int id;
	
	@Column(name="nombre_producto")
	private String nombreProducto;
	
	@Column(name="fecha_hora")
	private Date fechaHora;
	
	@Column(name="dispositivo")
	private String dispositivo;
	
	@Column(name="accion")
	private String accion;
	
	@Column(name="navegador")
	private String navegador;
	
	@JoinColumn(name="inventarioId")
	@ManyToOne
	private InventarioProducto inventarioId;
	
	@JoinColumn(name="productoId")
	@ManyToOne
	private InventarioProducto productoId;

	public AuditoriaInventario(int id, String nombreProducto, Date fechaHora, String dispositivo, String accion,
			String navegador, Departamento inventarioId, Departamento productoId) {
		super();
		this.id = id;
		this.nombreProducto = nombreProducto;
		this.fechaHora = fechaHora;
		this.dispositivo = dispositivo;
		this.accion = accion;
		this.navegador = navegador;
		this.inventarioId = inventarioId;
		this.productoId = productoId;
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

	public Date getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(Date fechaHora) {
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

	public Departamento getInventarioId() {
		return inventarioId;
	}

	public void setInventarioId(Departamento inventarioId) {
		this.inventarioId = inventarioId;
	}

	public Departamento getProductoId() {
		return productoId;
	}

	public void setProductoId(Departamento productoId) {
		this.productoId = productoId;
	}
	
	
	

}
