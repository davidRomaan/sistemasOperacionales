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
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "FACTURA_VENTA")
@NamedQueries({
		@NamedQuery(name = FacturaVenta.OBTENER_ULTIMA_REGISTRADA, query = "SELECT MAX(fv.id) FROM FacturaVenta fv "
				+ "WHERE fv.clienteId.cedula = ?1"),
		@NamedQuery(name=FacturaVenta.LISTAR_POR_FECHA, query="SELECT fv FROM FacturaVenta fv "
				+ "WHERE fv.dia = ?1 AND fv.mes = ?2 AND fv.anio = ?3"),
		@NamedQuery(name=FacturaVenta.FACTURAS_FECHA_INICIO_FIN, query="SELECT fv FROM FacturaVenta fv WHERE fv.fechaVenta BETWEEN ?1 AND ?2")
		})
public class FacturaVenta implements Serializable {

	public static final String OBTENER_ULTIMA_REGISTRADA = "facturaVenta.obtener";
	
	
	/**
	 * Lista las facturas por fecha
	 * ?1 fecha
	 */
	public static final String LISTAR_POR_FECHA = "facturaVenta.listarPorFecha";
	
	public static final String FACTURAS_FECHA_INICIO_FIN = "Factura.fechaInicioFin";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name="dia")
	private int dia;
	
	@Column(name="mes")
	private int mes;
	
	@Column(name="anio")
	private int anio;
	
	@Column(name="fecha_venta")
	@Temporal(TemporalType.DATE)
	private Calendar fechaVenta;

	@Column(name = "total")
	private double total;

	@JoinColumn(name = "cliente_id")
	@ManyToOne(cascade = {})
	private Persona clienteId;

	@JoinColumn(name = "empleado_id")
	@ManyToOne(cascade = {})
	private Persona empleadoId;

	public FacturaVenta(int id, int dia, int mes, int anio, double total, Persona clienteId, Persona empleadoId) {
		super();
		this.id = id;
		this.total = total;
		this.clienteId = clienteId;
		this.empleadoId = empleadoId;
		this.dia = dia;
		this.mes = mes;
		this.anio = anio;
	}

	public FacturaVenta() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDia() {
		return dia;
	}

	public void setDia(int dia) {
		this.dia = dia;
	}

	public int getMes() {
		return mes;
	}

	public void setMes(int mes) {
		this.mes = mes;
	}

	public int getAnio() {
		return anio;
	}

	public void setAnio(int anio) {
		this.anio = anio;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}

	public Persona getClienteId() {
		return clienteId;
	}

	public void setClienteId(Persona clienteId) {
		this.clienteId = clienteId;
	}

	public Persona getEmpleadoId() {
		return empleadoId;
	}

	public void setEmpleadoId(Persona empleadoId) {
		this.empleadoId = empleadoId;
	}

	public Calendar getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Calendar fechaVenta) {
		this.fechaVenta = fechaVenta;
	}
	
	

}
