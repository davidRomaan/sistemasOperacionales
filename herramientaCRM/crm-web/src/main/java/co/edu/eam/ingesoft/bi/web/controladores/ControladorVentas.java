package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaDetalleVentaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaPersonaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.DepartamentoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.DetalleVentaEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Inventario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorVentas")
@SessionScoped
public class ControladorVentas implements Serializable {

	// Empleado que inci� sesi�n
	@Inject
	private ControladorSesion sesion;

	// Datos cliente
	private String cedula;
	private String nombre;
	private String apellido;
	private String correo;
	private String telefono;
	private Genero tipoGenero;
	private int deptoSeleccionado;
	private int municipioSeleccionado;
	private List<Genero> generos;
	private String accion;

	// Cliente que va a reliazar la compra
	Usuario cliente;
	private boolean mostrarDatosCliente;

	private List<Departamento> departamentos;
	private List<Municipio> municipios;

	// Datos venta
	private int cantidad;
	private List<DetalleVenta> productosCompra;
	private List<InventarioProducto> productos;
	private InventarioProducto inventarioProductoComprar;
	
	private List<InventarioProducto> inventariosEditar;

	DetalleVenta detalleAgregar;

	FacturaVenta factura;
	double totalVenta = 0;

	// EJB
	@EJB
	private ProductoEJB productoEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

	@EJB
	private VentaEJB ventaEJB;
	
	@EJB
	private DetalleVentaEJB detalleEJB;

	@EJB
	private DepartamentoEJB departamentoEJB;
	
	@EJB
	private AuditoriaDetalleVentaEJB auditoriaDetalleVentasEJB;

	/**
	 * Carga los elementos al iniciar la p�gina
	 */
	@PostConstruct
	private void cargarElementos() {
		cargarProductos();
		listarDepartamentos();
		productosCompra = new ArrayList<DetalleVenta>();
		generos = Arrays.asList(Genero.values());
		municipios = departamentoEJB.listarMunicipiosDepartamento(departamentos.get(0).getId());
		inventariosEditar = new ArrayList<InventarioProducto>();
	}

	/**
	 * Carga la lista de productos
	 */
	private void cargarProductos() {
		productos = productoEJB.listarInventariosProductos();
	}

	/**
	 * Lista los departamentos registrados
	 */
	private void listarDepartamentos() {
		departamentos = departamentoEJB.listarDepartamentos();
	}

	/**
	 * Lista los muncipios del departamento seleccionado
	 */
	public void listarMunicipios() {
		municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado);
	}
	
	private void reload() throws IOException {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
	}

	/**
	 * Se asigna la fatura a cada uno de los detalles de venta
	 */
	private void registrarDetallesVenta() {
		detalleEJB.registrarDetalleVenta(productosCompra, factura);
		for (DetalleVenta detalleVenta : productosCompra) {
			detalleVenta.setFacturaVenta(factura);
			try {
				accion = "Registrar Detalle Venta";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaDetalleVentasEJB.crearAuditoriaDetalleVenta(detalleVenta, accion, browserDetail);
			
			}catch (Exception e) {
				e.printStackTrace();
			}
			// Registramos cada uno de los detalles venta
			detalleEJB.registrarDetalleVenta(productosCompra, factura);
		}
	}

	/**
	 * Se crea un detalle venta que ser� agregado al carrito
	 * 
	 * @param p
	 *            InventarioProducto que se desea comprar
	 */
	public void crearDetalleVenta(InventarioProducto p) {
		detalleAgregar = new DetalleVenta();
		detalleAgregar.setProducto(p.getProductoId());
		inventarioProductoComprar = p;
		try{
			reload();
		}catch (IOException e){
			
		}
	}

	/**
	 * Se agrega la cantidad del producto que se desea vender
	 */
	public void agregarCantidad() {
		if (cantidad > 0) {
			if (cantidad <= inventarioProductoComprar.getCantidad()) {
				detalleAgregar.setCantidad(cantidad);
				detalleAgregar.setSubtotal(detalleAgregar.getProducto().getValorProducto() * cantidad);
				inventarioProductoComprar.setCantidad(inventarioProductoComprar.getCantidad() - cantidad);
				sumarTotalVenta(cantidad, detalleAgregar.getProducto().getValorProducto());
				productosCompra.add(detalleAgregar);
				inventariosEditar.add(inventarioProductoComprar);
				detalleAgregar = null;
				try{
					reload();
				}catch (IOException e){
					
				}
			} else {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"No existe esta cantidad en el inventario", null));
			}
		} else {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "La cantidad debe ser mayor a 0", null));
		}
	}

	/**
	 * Para verificar si se seleccion� el bot�n agregar
	 * 
	 * @return true si se seleccion�, de lo contrario false
	 */
	public boolean isBtnAgregar() {
		return detalleAgregar != null;
	}

	/**
	 * Elimina un detalle venta de la lista
	 * 
	 * @param dv
	 *            detalle venta a eliminar
	 */
	public void eliminarDetalleVenta(DetalleVenta dv) {
		restarTotalVenta(dv);
		inventariosEditar.remove(inventarioProductoComprar);
		inventarioProductoComprar.setCantidad(inventarioProductoComprar.getCantidad() + dv.getCantidad());
		productosCompra.remove(dv);
		try{
			reload();
		}catch (IOException e){
			
		}
	}

	/**
	 * Le resta al total de la venta el detalle venta que se desea eliminar
	 * 
	 * @param dv
	 *            detalle de venta que se desea eliminar
	 */
	private void restarTotalVenta(DetalleVenta dv) {
		int cantidad = dv.getCantidad();
		double precioProducto = dv.getProducto().getValorProducto();
		totalVenta -= cantidad * precioProducto;
	}

	/**
	 * Se suma al total de la venta el nuevo producto que se desea agregar
	 * 
	 * @param cantidad
	 *            cantidad del mismo producto que se desea agregar
	 * @param precioProducto
	 *            precio del producto
	 */
	private void sumarTotalVenta(int cantidad, double precioProducto) {
		double valorProductos = cantidad * precioProducto;
		totalVenta += valorProductos;
	}

	private void limpiarCampos() {

		productosCompra = new ArrayList<DetalleVenta>();
		nombre = "";
		apellido = "";
		correo = "";
		telefono = "";
		cedula = "";
		totalVenta = 0;
		cliente = null;
		mostrarDatosCliente = false;

	}
	
	/**
	 * Actualiza las cantidades de los inventarios
	 */
	private void actualizarInventarios(){
		for (InventarioProducto inventarioProducto : inventariosEditar) {
			productoEJB.editarInventarioProducto(inventarioProducto);
		}
	}

	/**
	 * Crea la factura que se asiganar� a los detalle venta
	 */
	public void vender() {

		if (productosCompra.size() == 0) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "No hay productos en el carrito de compras", null));

		} else {

			factura = new FacturaVenta();

			if (cliente != null) {
				factura.setClienteId(cliente);
				Date fecha = new Date();
				String nuevaFecha = fecha.getDate()+"/"+fecha.getMonth()+1+"/"+fecha.getYear();
				factura.setFechaVenta(nuevaFecha);
				factura.setTotal(totalVenta);
				factura.setEmpleadoId(sesion.getUser());
				ventaEJB.registrarVenta(factura);
				
				factura.setId(ventaEJB.codigoUltimaFacturaCliente(cliente.getCedula()));
				registrarDetallesVenta();
				actualizarInventarios();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "La venta se ha registrado exitosamente", null));

				limpiarCampos();

			} else {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Debe buscar o registrar un cliente previamente", null));

			}
		}

	}

	/**
	 * Registra un cliente
	 */
	public void registrarCliente() {

		cliente = new Usuario();
		cliente.setApellido(apellido);
		cliente.setNombre(nombre);
		cliente.setCedula(cedula);
		cliente.setCorreo(correo);
		cliente.setTelefono(telefono);
		cliente.setGenero(tipoGenero);

		Municipio municipio = departamentoEJB.buscarMunicipio(municipioSeleccionado);
		cliente.setMunicipio(municipio);

		// Genero
		TipoUsuario tipoUsuario = new TipoUsuario();
		tipoUsuario.setId(3);
		cliente.setTipoUsuario(tipoUsuario);

		try {

			usuarioEJB.registrarUsuario(cliente);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Cliente Registrado Exitosamente", null));

		} catch (ExcepcionNegocio e) {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));

		}

	}

	/**
	 * Busca un cliente
	 */
	public void buscarCliente() {

		cliente = usuarioEJB.buscarCliente(cedula);

		mostrarDatosCliente = true;

		if (cliente != null) {

			nombre = cliente.getNombre();
			apellido = cliente.getApellido();
			correo = cliente.getCorreo();
			telefono = cliente.getTelefono();
			deptoSeleccionado = cliente.getMunicipio().getDepartamento().getId();
			municipioSeleccionado = cliente.getMunicipio().getId();
			tipoGenero = cliente.getGenero();

		} else {

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"El cliente no se encuentra registrado, este debe ser registrado para continuar con la venta",
							null));

		}
		try{
			reload();
		}catch (IOException e){
			
		}
	}

	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Genero getTipoGenero() {
		return tipoGenero;
	}

	public void setTipoGenero(Genero tipoGenero) {
		this.tipoGenero = tipoGenero;
	}

	public List<Genero> getGeneros() {
		return generos;
	}

	public void setGeneros(List<Genero> generos) {
		this.generos = generos;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public List<DetalleVenta> getProductosCompra() {
		return productosCompra;
	}

	public void setProductosCompra(List<DetalleVenta> productosCompra) {
		this.productosCompra = productosCompra;
	}

	public List<InventarioProducto> getProductos() {
		return productos;
	}

	public void setProductos(List<InventarioProducto> productos) {
		this.productos = productos;
	}

	public int getDeptoSeleccionado() {
		return deptoSeleccionado;
	}

	public void setDeptoSeleccionado(int deptoSeleccionado) {
		this.deptoSeleccionado = deptoSeleccionado;
	}

	public int getMunicipioSeleccionado() {
		return municipioSeleccionado;
	}

	public void setMunicipioSeleccionado(int municipioSeleccionado) {
		this.municipioSeleccionado = municipioSeleccionado;
	}

	public List<Departamento> getDepartamentos() {
		return departamentos;
	}

	public void setDepartamentos(List<Departamento> departamentos) {
		this.departamentos = departamentos;
	}

	public List<Municipio> getMunicipios() {
		return municipios;
	}

	public void setMunicipios(List<Municipio> municipios) {
		this.municipios = municipios;
	}

	public double getTotalVenta() {
		return totalVenta;
	}

	public void setTotalVenta(double totalVenta) {
		this.totalVenta = totalVenta;
	}

	public boolean isMostrarDatosCliente() {
		return mostrarDatosCliente;
	}

	public void setMostrarDatosCliente(boolean mostrarDatosCliente) {
		this.mostrarDatosCliente = mostrarDatosCliente;
	}

}
