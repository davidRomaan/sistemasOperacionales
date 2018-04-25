package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.ArrayList;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@Named("controladorProducto")
@SessionScoped
public class ControladorProducto implements Serializable{

	private String nombre;
	private String descripcion;
	private double peso;
	private double dimension;
	private double valor;
	private ArrayList<Lote> lotes;
	private int loteSeleccionado;
	
	@EJB
	private ProductoEJB productoEJB;
	
	/**
	 * Registra el producto
	 */
	public void registrar(){
		//Fecha
		Producto producto = new Producto();
		producto.setDimension(dimension);
		producto.setFechaIngreso(null);
		producto.setPeso(peso);
		producto.setNombre(nombre);
		producto.setDescripcion(descripcion);
		producto.setValorProducto(valor);
		producto.setLote(null);
		productoEJB.registrarProducto(producto);
	}
	
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
	public ArrayList<Lote> getLotes() {
		return lotes;
	}
	public void setLotes(ArrayList<Lote> lotes) {
		this.lotes = lotes;
	}
	public int getLoteSeleccionado() {
		return loteSeleccionado;
	}
	public void setLoteSeleccionado(int loteSeleccionado) {
		this.loteSeleccionado = loteSeleccionado;
	}
	
	
	
}
