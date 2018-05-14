package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.sql.Date;

public class DimensionFactura implements Serializable {

	private int id;
	private double totalVenta;
	private Date fecha;
	
	public DimensionFactura() {
		// TODO Auto-generated constructor stub
	}

	public DimensionFactura(int id, double totalVenta, Date fecha) {
		super();
		this.id = id;
		this.totalVenta = totalVenta;
		this.fecha = fecha;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(double totalVenta) {
		this.totalVenta = totalVenta;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	
	
	
}
