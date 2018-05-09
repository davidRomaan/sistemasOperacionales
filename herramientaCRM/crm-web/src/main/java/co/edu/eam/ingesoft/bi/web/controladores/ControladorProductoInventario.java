package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;

@SessionScoped
@Named("controladorProductoInventario")
public class ControladorProductoInventario implements Serializable {

	private List<Producto> productos;
	private int productoSeleccionado;
	
	private List<Inventario> inventarios;
	private int inventarioSeleccionado;
	
	private List<InventarioProducto> inventariosProducto;
	private int codigo;
	
	private int cantidad;
	
	@EJB
	private ProductoEJB productoEJB;
	
	@PostConstruct
	private void cargarComponentes(){
		inventarios = productoEJB.listarInventarios();
		productos = productoEJB.listarProductos();
		inventariosProducto = productoEJB.inventariosProducto(productos.get(0));
	}
	
	/**
	 * Lista los inventarios en los que se encuentra registrado un producto
	 */
	public void listarInventariosProducto(){
		Producto prod = productoEJB.buscarProducto(productoSeleccionado);
		inventariosProducto = productoEJB.inventariosProducto(prod);
	}
	
	/**
	 * Busca un producto por su código y lo selecciona en el combo
	 */
	public void buscarProductoCodigo(){
		
		Producto prod = productoEJB.buscarProducto(codigo);
		
		if (prod != null){
			
			productoSeleccionado = prod.getId();
			listarInventariosProducto();
			
		} else {
			
			Messages.addFlashGlobalError("No existe un producto con este código");
			
		}
		
	}
	
	/**
	 * Registra en la tabla inventario producto
	 */
	public void agregar(){
		
		Producto producto = productoEJB.buscarProducto(productoSeleccionado);
		Inventario inventario = productoEJB.buscarInventarioId(inventarioSeleccionado);
		
		InventarioProducto ip = new InventarioProducto();
		ip.setCantidad(cantidad);
		ip.setProductoId(producto);
		ip.setInventarioId(inventario);
		
		try{
			
			productoEJB.registrarInventarioProducto(ip);
			listarInventariosProducto();
			
		} catch (ExcepcionNegocio e){
			
			Messages.addFlashGlobalError(e.getMessage());
			
		}
		
	}
	
	public void eliminar(){
		
	}

	public List<Producto> getProductos() {
		return productos;
	}

	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public int getProductoSeleccionado() {
		return productoSeleccionado;
	}

	public void setProductoSeleccionado(int productoSeleccionado) {
		this.productoSeleccionado = productoSeleccionado;
	}

	public List<Inventario> getInventarios() {
		return inventarios;
	}

	public void setInventarios(List<Inventario> inventarios) {
		this.inventarios = inventarios;
	}

	public int getInventarioSeleccionado() {
		return inventarioSeleccionado;
	}

	public void setInventarioSeleccionado(int inventarioSeleccionado) {
		this.inventarioSeleccionado = inventarioSeleccionado;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public List<InventarioProducto> getInventariosProducto() {
		return inventariosProducto;
	}

	public void setInventariosProducto(List<InventarioProducto> inventariosProducto) {
		this.inventariosProducto = inventariosProducto;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}
	
	
	
}
