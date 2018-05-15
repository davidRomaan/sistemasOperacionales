package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.AuditoriaEJB;
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
import co.edu.eam.ingesoft.bi.presistencia.entidades.InventarioProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
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
	private String fechaNacimiento;
	private Usuario usuario;

	// Cliente que va a reliazar la compra
	Persona cliente;
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
	private AuditoriaEJB auditoriaEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

	@EJB
	private VentaEJB ventaEJB;

	@EJB
	private DetalleVentaEJB detalleEJB;

	@EJB
	private DepartamentoEJB departamentoEJB;

	/**
	 * Carga los elementos al iniciar la p�gina
	 */
	@PostConstruct
	private void cargarElementos() {
		usuario = Faces.getApplicationAttribute("user");
		cargarProductos();
		listarDepartamentos();
		productosCompra = new ArrayList<DetalleVenta>();
		generos = Arrays.asList(Genero.values());
		municipios = departamentoEJB.listarMunicipiosDepartamento(departamentos.get(0).getId());
		inventariosEditar = new ArrayList<InventarioProducto>();
	}

	/**
	 * Valida que se hayan ingresado todos los campos
	 * 
	 * @return true si se llenaron los campos, de lo contrario false
	 */
	private boolean validarCampos() {
		if (cedula.equals("") || nombre.equals("") || apellido.equals("") || telefono.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * Carga la lista de productos
	 */
	private void cargarProductos() {
		productos = productoEJB.listarInventariosProductos();

		if (productos.size() == 0) {

			List<Producto> listaProductos = productoEJB.listarProductos();

			if (listaProductos.size() > 0) {

				Messages.addFlashGlobalWarn("Para realizar una venta debe agregar los productos a un inventario");

			}

		}

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

	private void reload() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Se asigna la fatura a cada uno de los detalles de venta
	 */
	private void registrarDetallesVenta() {
		detalleEJB.registrarDetalleVenta(productosCompra, factura);

		try {

			accion = "Crear DetalleVenta";
			String browserDetail = Faces.getRequest().getHeader("User-Agent");
			auditoriaEJB.crearAuditoria("AuditoriaDetalleVenta", accion, "DT creada: " + factura.getId(),
					sesion.getUser().getCedula(), browserDetail);

		} catch (Exception e) {
			e.printStackTrace();
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
		reload();
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
				reload();

			} else {
				Messages.addFlashGlobalError("No existe esta cantidad en el inventario");
			}
		} else {
			Messages.addFlashGlobalError("La cantidad debe ser mayor a 0");
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
		
		accion = "Eliminar DetalleVenta";
		String browserDetail = Faces.getRequest().getHeader("User-Agent");
		auditoriaEJB.crearAuditoria("AuditoriaDetalleVenta", accion, "DV eliminada: " + factura.getId(),
				sesion.getUser().getCedula(), browserDetail);
		
		reload();
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

		nombre = "";
		apellido = "";
		correo = "";
		telefono = "";
		totalVenta = 0;
		cliente = null;

	}

	/**
	 * Actualiza las cantidades de los inventarios
	 */
	private void actualizarInventarios() {
		for (InventarioProducto inventarioProducto : inventariosEditar) {
			productoEJB.editarInventarioProducto(inventarioProducto);
		}
	}

	public boolean isClienteExiste() {
		return cliente == null;
	}

	/**
	 * Crea la factura que se asiganar� a los detalle venta
	 */
	public void vender() {

		if (cliente != null) {

			if (productosCompra.size() == 0) {

				Messages.addFlashGlobalError("No hay productos en el carrito de compras");

			} else {

				factura = new FacturaVenta();

				factura.setClienteId(cliente);
				
				Calendar fechaActual = new GregorianCalendar();
				int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
				int mes = fechaActual.get(Calendar.MONTH) + 1;
				int anio = fechaActual.get(Calendar.YEAR);

				factura.setDia(dia);
				factura.setMes(mes);
				factura.setAnio(anio);
				factura.setFechaVenta(new GregorianCalendar());
				
				factura.setTotal(totalVenta);
				factura.setEmpleadoId(sesion.getUser());
				ventaEJB.registrarVenta(factura);

				accion = "Registar FacturaVenta";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaFacturaVenta", accion, "FV creada: " + factura.getId(),
						sesion.getUser().getCedula(), browserDetail);

				factura.setId(ventaEJB.codigoUltimaFacturaCliente(cliente.getCedula()));
				registrarDetallesVenta();
				actualizarInventarios();

				Messages.addFlashGlobalInfo("La venta se ha registrado exitosamente");

				limpiarCampos();
				mostrarDatosCliente = false;
				productosCompra = new ArrayList<DetalleVenta>();

				reload();

			}

		} else {
			Messages.addFlashGlobalError("Debe buscar o registrar un cliente previamente");
		}

	}

	/**
	 * Registra un cliente
	 */
	public void registrarCliente() {

		if (validarCampos()) {

			cliente = new Persona();
			cliente.setApellido(apellido);
			cliente.setNombre(nombre);
			cliente.setCedula(cedula);
			cliente.setCorreo(correo);
			cliente.setTelefono(telefono);
			cliente.setGenero(tipoGenero);
			cliente.setFechaNacimiento(fechaNacimiento);

			Municipio municipio = departamentoEJB.buscarMunicipio(municipioSeleccionado);
			cliente.setMunicipio(municipio);

			try {

				usuarioEJB.registrarCliente(cliente);

				accion = "Crear Persona";
				String browserDetail2 = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona creada: " + cliente.getNombre(),
						sesion.getUser().getCedula(), browserDetail2);

				Messages.addFlashGlobalInfo("Cliente Registrado Exitosamente");

				limpiarCampos();
				mostrarDatosCliente = false;

				reload();

			} catch (ExcepcionNegocio e) {

				Messages.addFlashGlobalError(e.getMessage());

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {

			Messages.addFlashGlobalError("Debe ingresar todos los campos");

		}

	}

	/**
	 * Busca un cliente
	 */
	public void buscarCliente() {

		cliente = usuarioEJB.buscarCliente(cedula);

		mostrarDatosCliente = true;

		reload();

		if (cliente != null) {

			try {

				accion = "Buscar Persona";
				String browserDetail = Faces.getRequest().getHeader("User-Agent");
				auditoriaEJB.crearAuditoria("AuditoriaPersona", accion, "persona buscada: " + cliente.getNombre(),
						sesion.getUser().getCedula(), browserDetail);

			} catch (Exception e) {
				e.printStackTrace();
			}

			nombre = cliente.getNombre();
			apellido = cliente.getApellido();
			correo = cliente.getCorreo();
			telefono = cliente.getTelefono();
			deptoSeleccionado = cliente.getMunicipio().getDepartamento().getId();

			municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado);

			municipioSeleccionado = cliente.getMunicipio().getId();
			tipoGenero = cliente.getGenero();
			fechaNacimiento = cliente.getFechaNacimiento();

		} else {

			limpiarCampos();

			Messages.addFlashGlobalError("El cliente no se encuentra registrado,"
					+ " este debe ser registrado para continuar con la venta");

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

	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

}
