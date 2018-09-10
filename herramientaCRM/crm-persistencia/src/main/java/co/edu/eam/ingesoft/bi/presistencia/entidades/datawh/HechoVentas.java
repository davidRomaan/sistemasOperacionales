package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.ManyToAny;

@Entity
@Table(name = "HECHO_VENTAS")
@NamedQuery(name = HechoVentas.LISTAR, query = "SELECT hv FROM HechoVentas hv")
public class HechoVentas implements Serializable {

	/**
	 * Lista los hechos de ventas registrados en la bd
	 */
	public static final String LISTAR = "HechoVentas.listar";

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "UNIDADES")
	private int unidades;

	@Column(name = "SUBTOTAL")
	private double subtotal;

	@JoinColumn(name = "ID_CLIENTE")
	@ManyToOne
	private DimensionPersona persona;

	@JoinColumn(name = "MUNICIPIO_ID")
	@ManyToOne
	private DimensionMunicipio municipio;

	@JoinColumn(name = "PRODUCTO_ID")
	@ManyToOne
	private DimensionProducto producto;

	@JoinColumn(name = "ID_EMPLEADO")
	@ManyToOne
	private DimensionPersona empleado;

	@Column(name="FECHA_VENTA")
	@Temporal(TemporalType.DATE)
	private Calendar fecha;

	public HechoVentas() {
		// TODO Auto-generated constructor stub
	}

	public HechoVentas(int unidades, double subtotal, DimensionPersona persona, DimensionPersona empleado,
			DimensionMunicipio municipio, DimensionProducto producto, Calendar fecha) {
		super();
		this.unidades = unidades;
		this.subtotal = subtotal;
		this.persona = persona;
		this.municipio = municipio;
		this.producto = producto;
		this.empleado = empleado;
		this.fecha = fecha;
	}

	public int getUnidades() {
		return unidades;
	}

	public void setUnidades(int unidades) {
		this.unidades = unidades;
	}

	public double getSubtotal() {
		return subtotal;
	}

	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	public DimensionPersona getPersona() {
		return persona;
	}

	public void setPersona(DimensionPersona persona) {
		this.persona = persona;
	}

	public DimensionMunicipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(DimensionMunicipio municipio) {
		this.municipio = municipio;
	}

	public DimensionProducto getProducto() {
		return producto;
	}

	public void setProducto(DimensionProducto producto) {
		this.producto = producto;
	}

	public DimensionPersona getEmpleado() {
		return empleado;
	}

	public void setEmpleado(DimensionPersona empleado) {
		this.empleado = empleado;
	}

	public Calendar getFecha() {
		return fecha;
	}

	public String getFechaVenta() {
		int mes = this.fecha.get(Calendar.MONTH) + 1;
		return this.fecha.get(Calendar.YEAR) + "/" + mes + "/" + this.fecha.get(Calendar.DAY_OF_MONTH);
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
