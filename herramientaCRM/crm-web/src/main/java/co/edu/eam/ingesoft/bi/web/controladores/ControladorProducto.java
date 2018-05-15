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
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.TipoProductoEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Lote;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorProducto")
@SessionScoped
public class ControladorProducto implements Serializable {

	@Inject
	private ControladorSesion sesion;

	private int codigo;
	private String nombre;
	private String descripcion;
	private double peso;
	private double dimension;
	private int inventarioSeleccionado;
	private List<InventarioProducto> inventariosProducto;
	private double valor;
	private List<Lote> lotes;
	private int loteSeleccionado;
	private List<Producto> productos;
	private List<TipoProducto> tiposProducto;
	private Producto productoBuscado;
	private int tipoProductoSeleccionado;
	private String accion;
	private Usuario usuario;

	@EJB
	private AuditoriaEJB auditoriaEJB;

	@EJB
	private ProductoEJB productoEJB;

	@EJB
	private TipoProductoEJB tipoProductoEJB;

	@PostConstruct
	private void cargarDatos() {

		usuario = Faces.getApplicationAttribute("user");
		lotes = productoEJB.lotes();
		listarTiposProducto();
		listar();
	}

	/**
	 * Lista los tipos de producto registrados
	 */
	private void listarTiposProducto() {
		tiposProducto = productoEJB.listarTiposProducto();
	}

	/**
	 * Busca un producto por su c�digo
	 */
	public void buscar() {

		productoBuscado = productoEJB.buscarProducto(codigo);

		if (productoBuscado != null) {

			try {

				accion = "Buscar Producto";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaProducto", accion,
						"producto buscado: " + productoBuscado.getNombre(), sesion.getUser().getCedula(),
						browserDetail);

			} catch (Exception e) {
				e.printStackTrace();
			}

			codigo = productoBuscado.getId();
			nombre = productoBuscado.getNombre();
			descripcion = productoBuscado.getDescripcion();
			peso = productoBuscado.getPeso();
			dimension = productoBuscado.getDimension();
			valor = productoBuscado.getValorProducto();
			loteSeleccionado = productoBuscado.getLote().getId();

			inventariosProducto = productoEJB.inventariosProducto(productoBuscado);

		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No existe", null));

		}

	}

	/**
	 * Edita un producto previamente buscado
	 */
	public void editar() {

		if (productoBuscado == null) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe buscar un producto previamente", null));
		} else {

			if (validarCampos()) {

				productoBuscado.setDescripcion(descripcion);
				productoBuscado.setNombre(nombre);
				productoBuscado.setDimension(dimension);
				productoBuscado.setLote(productoEJB.buscarloteProducto(loteSeleccionado));
				productoBuscado.setValorProducto(valor);
				productoBuscado.setPeso(peso);

				productoEJB.editarProducto(productoBuscado);

				try {

					accion = "Editar Producto";
					String browserDetail = Faces.getRequest().getHeader("User-Agent");
					auditoriaEJB.crearAuditoria("AuditoriaProducto", accion,
							"producto editado: " + productoBuscado.getNombre(), sesion.getUser().getCedula(),
							browserDetail);

				} catch (Exception e) {
					e.printStackTrace();
				}

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Operaci�n exitosa", null));

				limpiarCampos();
				listar();

			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe ingresar todos los campos", null));
			}
		}

	}

	/**
	 * Limpia los campos del producto
	 */
	private void limpiarCampos() {
		productoBuscado = null;
		dimension = 0;
		codigo = 0;
		nombre = "";
		descripcion = "";
		valor = 0;
		peso = 0;
	}

	private boolean validarCampos() {
		if (codigo <= 0 || dimension <= 0 || peso <= 0 || nombre.equals("") || valor <= 0) {
			return false;
		}
		return true;
	}

	/**
	 * Registra el producto
	 */
	public void registrar() {

		if (validarCampos()) {

			Producto producto = new Producto();
			producto.setId(codigo);
			producto.setDimension(dimension);
			producto.setFechaIngreso(new Date());
			producto.setPeso(peso);
			producto.setNombre(nombre);
			producto.setDescripcion(descripcion);
			producto.setValorProducto(valor);
			producto.setLote(productoEJB.buscarloteProducto(loteSeleccionado));
			producto.setUsuairoPersonaId(sesion.getUser());

			TipoProducto tipoProducto = tipoProductoEJB.buscar(tipoProductoSeleccionado);

			producto.setTipoProducto(tipoProducto);

			try {

				productoEJB.registrarProducto(producto);

				accion = "Crear Producto";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaProducto", accion, "producto creado: " + producto.getNombre(),
						sesion.getUser().getCedula(), browserDetail);

			} catch (ExcepcionNegocio e) {
				// TODO: handle exception
				Messages.addFlashGlobalError(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
			}

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "El producto ha sido registrado exitosamente", null));
			limpiarCampos();
			listar();

		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Debe ingresar todos los campos", null));
		}

	}

	/**
	 * Lista los productos registrados en la base de datos
	 */
	public void listar() {
		productos = productoEJB.listarProductos();
	}

	/**
	 * Elimina un producto
	 * 
	 * @param p
	 *            producto que se desea eliminar
	 */
	public void eliminarProducto(Producto p) {
		productos.remove(p);
		productoEJB.eliminarProducto(p);

		try {

			accion = "Eliminar Producto";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaProducto", accion, "producto eliminado: " + p.getNombre(),
					sesion.getUser().getCedula(), browserDetail);

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	public int getLoteSeleccionado() {
		return loteSeleccionado;
	}

	public void setLoteSeleccionado(int loteSeleccionado) {
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

	public List<TipoProducto> getTiposProducto() {
		return tiposProducto;
	}

	public void setTiposProducto(List<TipoProducto> tiposProducto) {
		this.tiposProducto = tiposProducto;
	}

	public int getTipoProductoSeleccionado() {
		return tipoProductoSeleccionado;
	}

	public void setTipoProductoSeleccionado(int tipoProductoSeleccionado) {
		this.tipoProductoSeleccionado = tipoProductoSeleccionado;
	}

	public int getInventarioSeleccionado() {
		return inventarioSeleccionado;
	}

	public void setInventarioSeleccionado(int inventarioSeleccionado) {
		this.inventarioSeleccionado = inventarioSeleccionado;
	}

	public List<InventarioProducto> getInventariosProducto() {
		return inventariosProducto;
	}

	public void setInventariosProducto(List<InventarioProducto> inventariosProducto) {
		this.inventariosProducto = inventariosProducto;
	}

}
