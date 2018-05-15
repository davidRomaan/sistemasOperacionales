package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.sql.Date;
import java.util.Calendar;

public class DimensionFactura implements Serializable {

	private int id;
	private double totalVenta;
	private Calendar fecha;
	
	public DimensionFactura() {
		// TODO Auto-generated constructor stub
	}

	public DimensionFactura(int id, double totalVenta, Calendar fecha) {
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

	public Calendar getFecha() {
		return fecha;
	}

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	
	
	
}
