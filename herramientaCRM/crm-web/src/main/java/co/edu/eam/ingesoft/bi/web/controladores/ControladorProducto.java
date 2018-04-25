package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@Named("controladorProducto")
@SessionScoped
public class ControladorProducto implements Serializable{

	private int codigo;
	private String nombre;
	private String descripcion;
	private double peso;
	private double dimension;
	private double valor;
	private List<Lote> lotes;
	private Lote loteSeleccionado;
	private List<Producto> productos;
	
	private Producto productoBuscado;
	
	@EJB
	private ProductoEJB productoEJB;
	
	@PostConstruct
	private void listarLotes(){
		lotes = productoEJB.lotes();
	}
	
	/**
	 * Busca un producto por su código
	 */
	public void buscar(){
		
		productoBuscado = productoEJB.buscarProducto(codigo);
		
		if (productoBuscado != null){
			
			nombre = productoBuscado.getNombre();
			descripcion = productoBuscado.getDescripcion();
			peso = productoBuscado.getPeso();
			dimension = productoBuscado.getDimension();
			valor = productoBuscado.getValorProducto();
			loteSeleccionado = productoBuscado.getLote();
			
		} else {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe", null));
			
		}
		
	}
	
	/**
	 * Edita un producto previamente buscado
	 */
	public void editar(){
		
		if (productoBuscado == null){
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe buscar un producto previamente", null));
		} else {
			
			productoBuscado.setDescripcion(descripcion);
			productoBuscado.setNombre(nombre);
			productoBuscado.setDimension(dimension);
			productoBuscado.setLote(loteSeleccionado);
			productoBuscado.setValorProducto(valor);
			productoBuscado.setPeso(peso);
			
			productoEJB.editarProducto(productoBuscado);
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Operación exitosa", null));
			
		}
		
	}
	
	/**
	 * Registra el producto
	 */
	public void registrar(){	
		
		buscar();
		
		if (productoBuscado == null){
		
		Producto producto = new Producto();
		producto.setId(codigo);
		producto.setDimension(dimension);
		producto.setFechaIngreso(new Date());
		producto.setPeso(peso);
		producto.setNombre(nombre);
		producto.setDescripcion(descripcion);
		producto.setValorProducto(valor);
		producto.setLote(loteSeleccionado);
		productoEJB.registrarProducto(producto);
		
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(FacesMessage.SEVERITY_INFO, 
						"El producto ha sido registrado exitosamente", null));
		
		} else {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Ya existe un producto con el código ingresado", null));
			
		}
		
	}
	
	/**
	 * Lista los productos registrados en la base de datos
	 */
	public void listar(){
		
		productos = productoEJB.listarProductos();
		
	}
	
	public void eliminar(){
		eliminarProducto(productoBuscado);
	}
	
	/**
	 * Elimina un producto
	 * @param p producto que se desea eliminar
	 */
	public void eliminarProducto(Producto p){
		productoEJB.eliminarProducto(p);
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
	public List<Lote> getLotes() {
		return lotes;
	}
	public void setLotes(ArrayList<Lote> lotes) {
		this.lotes = lotes;
	}

	public Lote getLoteSeleccionado() {
		return loteSeleccionado;
	}

	public void setLoteSeleccionado(Lote loteSeleccionado) {
		this.loteSeleccionado = loteSeleccionado;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}
	
	
	
}
