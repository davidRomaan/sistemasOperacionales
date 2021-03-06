package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
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
	private String accion;

	@EJB
	private ProductoEJB productoEJB;

	@EJB
	private AuditoriaEJB auditoriaEJB;

	@Inject
	private ControladorSesion sesion;

	@PostConstruct
	private void cargarComponentes() {
		inventarios = productoEJB.listarInventarios();
		productos = productoEJB.listarProductos();
		if (productos.size() > 0) {
			inventariosProducto = productoEJB.inventariosProducto(productos.get(0));
		}
	}

	/**
	 * Lista los inventarios en los que se encuentra registrado un producto
	 */
	public void listarInventariosProducto() {
		Producto prod = productoEJB.buscarProducto(productoSeleccionado);
		inventariosProducto = productoEJB.inventariosProducto(prod);
	}

	/**
	 * Busca un producto por su c�digo y lo selecciona en el combo
	 */
	public void buscarProductoCodigo() {

		Producto prod = productoEJB.buscarProducto(codigo);

		if (prod != null) {

			productoSeleccionado = prod.getId();

			accion = "Buscar Producto";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaProducto", accion, "producto buscado: " + prod.getNombre(),
					sesion.getUser().getCedula(), browserDetail);

			listarInventariosProducto();

		} else {

			Messages.addFlashGlobalError("No existe un producto con este c�digo");

		}

	}

	/**
	 * Registra en la tabla inventario producto
	 */
	public void agregar() {

		Producto producto = productoEJB.buscarProducto(productoSeleccionado);
		Inventario inventario = productoEJB.buscarInventarioId(inventarioSeleccionado);

		InventarioProducto ip = new InventarioProducto();
		ip.setCantidad(cantidad);
		ip.setProductoId(producto);
		ip.setInventarioId(inventario);

		try {

			productoEJB.registrarInventarioProducto(ip);

			accion = "Crear InventarioProducto";
			String browserDetail2 = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaInventarioProducto", accion, "IP creado: " + ip.getProductoId(),
					sesion.getUser().getCedula(), browserDetail2);

			listarInventariosProducto();

		} catch (ExcepcionNegocio e) {

			Messages.addFlashGlobalError(e.getMessage());

		}

	}

	/**
	 * Elimina un invnetario de un producto
	 * 
	 * @param ip
	 *            invnetario del producto que se desea eliminar
	 */
	public void eliminar(InventarioProducto ip) {
		inventariosProducto.remove(ip);
		productoEJB.eliminarInventarioProducto(ip);
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
