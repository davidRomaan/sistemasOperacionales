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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "FACTURA_VENTA")
@NamedQueries({
		@NamedQuery(name = FacturaVenta.OBTENER_ULTIMA_REGISTRADA, query = "SELECT MAX(fv.id) FROM FacturaVenta fv "
				+ "WHERE fv.clienteId.cedula = ?1") })
public class FacturaVenta implements Serializable {

	public static final String OBTENER_ULTIMA_REGISTRADA = "facturaVenta.obtener";

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "fecha_venta")
	private Date fechaVenta;

	@Column(name = "total")
	private double total;

	@JoinColumn(name = "cliente_id")
	@ManyToOne(cascade = {})
	private Persona clienteId;

	@JoinColumn(name = "empleado_id")
	@ManyToOne(cascade = {})
	private Persona empleadoId;

	public FacturaVenta(int id, Date fechaVenta, double total, Persona clienteId, Persona empleadoId) {
		super();
		this.id = id;
		this.fechaVenta = fechaVenta;
		this.total = total;
		this.clienteId = clienteId;
		this.empleadoId = empleadoId;
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

	public Date getFechaVenta() {
		return fechaVenta;
	}

	public void setFechaVenta(Date fechaVenta) {
		this.fechaVenta = fechaVenta;
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

}
