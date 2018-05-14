package co.edu.eam.ingesoft.bi.presistencia.entidades.datawh;

import java.io.Serializable;

public class HechoVentas implements Serializable {

	private int unidades;
	private double subtotal;
	private DimensionPersona persona;
	private DimensionFactura factura;
	private DimensionMunicipio municipio;
	private DimensionProducto producto;
	
	public HechoVentas() {
		// TODO Auto-generated constructor stub
	}

	public HechoVentas(int unidades, double subtotal, DimensionPersona persona, DimensionFactura factura,
			DimensionMunicipio municipio, DimensionProducto producto) {
		super();
		this.unidades = unidades;
		this.subtotal = subtotal;
		this.persona = persona;
		this.factura = factura;
		this.municipio = municipio;
		this.producto = producto;
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

	public DimensionFactura getFactura() {
		return factura;
	}

	public void setFactura(DimensionFactura factura) {
		this.factura = factura;
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
	
	
}
