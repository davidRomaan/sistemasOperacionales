package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;
import java.util.Calendar;

public class HechoVentas implements Serializable {

	private int unidades;
	private double subtotal;
	private DimensionPersona persona;
	private DimensionMunicipio municipio;
	private DimensionProducto producto;
	private DimensionPersona empleado;
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

	public void setFecha(Calendar fecha) {
		this.fecha = fecha;
	}
	
	
}
