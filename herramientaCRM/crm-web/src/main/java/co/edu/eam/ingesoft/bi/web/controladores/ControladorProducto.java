package co.edu.eam.ingesoft.bi.web.controladores;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;

@Named("controladorProducto")
@SessionScoped
public class ControladorProducto {

	private String nombre;
	private String descripcion;
	private double peso;
	private double dimension;
	private double valor;
	private Lote lote;
	
	
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	public double getDimension() {
		return dimension;
	}
	public void setDimension(double dimension) {
		this.dimension = dimension;
	}
	public double getValor() {
		return valor;
	}
	public void setValor(double valor) {
		this.valor = valor;
	}
	public Lote getLote() {
		return lote;
	}
	public void setLote(Lote lote) {
		this.lote = lote;
	}
	
	
	
}
