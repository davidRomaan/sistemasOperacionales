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

import co.edu.eam.ingesoft.bi.negocio.beans.DepartamentoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.ProductoEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.UsuarioEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.Genero;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@Named("controladorVentas")
@SessionScoped
public class ControladorVentas implements Serializable{

	//Datos cliente
	private String cedula;
	private String nombre;
	private String apellido;
	private String correo;
	private String telefono;
	private Genero genero;
	private Departamento deptoSeleccionado;
	private Municipio municipioSeleccionado;
	
	//Cliente que va a reliazar la compra
	Usuario cliente;
	private boolean clienteExiste;
	
	private List<Departamento> departamentos;
	private List<Municipio> municipios;
	
	//Datos venta
	private int cantidad;
	private List<DetalleVenta> productosCompra;
	private List<Producto> productos;
	
	DetalleVenta detalleAgregar;
	
	FacturaVenta factura;
	double totalVenta = 0;
	
	private boolean btnAgregarCarritoSeleccionado;
	
	//EJB
	@EJB
	private ProductoEJB productoEJB;
	
	@EJB
	private UsuarioEJB usuarioEJB;
	
	@EJB
	private VentaEJB ventaEJB;
	
	@EJB
	private DepartamentoEJB departamentoEJB;
	
	/**
	 * Carga los elementos al iniciar la página
	 */
	@PostConstruct
	private void cargarElementos(){
		cargarProductos();
		listarDepartamentos();
	}
	
	/**
	 * Carga la lista de productos
	 */
	private void cargarProductos(){
		productos = productoEJB.listarProductos();
	}
	
	/**
	 * Lista los departamentos registrados
	 */
	private void listarDepartamentos (){
		departamentos = departamentoEJB.listarDepartamentos();
	}
	
	/**
	 * Lista los muncipios del departamento seleccionado
	 */
	public void listarMunicipios(){
		municipios = departamentoEJB.listarMunicipiosDepartamento(deptoSeleccionado.getId());
	}
	
	/**
	 * Se asigna la fatura a cada uno de los detalles de venta
	 */
	private void registrarDetallesVenta(){
		for (DetalleVenta detalleVenta : productosCompra) {
			detalleVenta.setFacturaVentaId(factura);
			//Registramos cada uno de los detalles venta
			ventaEJB.registrarDetalleVenta(detalleVenta);
		}
	}
	
	/**
	 * Se crea un detalle venta que será agregado al carrito
	 * @param p Producto que se desea comprar
	 */
	public void crearDetalleVenta(Producto p){
		btnAgregarCarritoSeleccionado = true;
		detalleAgregar = new DetalleVenta();
		detalleAgregar.setProductoId(p);
	}
	
	/**
	 * Se agrega la cantidad del producto que se desea vender
	 */
	public void agregarCantidad(){
		detalleAgregar.setCantidad(cantidad);
		sumarTotalVenta(cantidad, detalleAgregar.getProductoId().getValorProducto());
		productosCompra.add(detalleAgregar);
		detalleAgregar = null;
	}
	
	public void eliminarDetalleVenta (DetalleVenta dv){
		restarTotalVenta(dv);
		productosCompra.remove(dv);
	}
	
	/**
	 * Le resta al total de la venta el detalle venta que se desea eliminar
	 * @param dv detalle de venta que se desea eliminar
	 */
	private void restarTotalVenta (DetalleVenta dv){
		int cantidad = dv.getCantidad();
		double precioProducto = dv.getProductoId().getValorProducto();
		totalVenta -= cantidad * precioProducto;
	}
	
	/**
	 * Se suma al total de la venta el nuevo producto que se desea agregar
	 * @param cantidad cantidad del mismo producto que se desea agregar
	 * @param precioProducto precio del producto
	 */
	private void sumarTotalVenta (int cantidad, double precioProducto){
		double valorProductos = cantidad * precioProducto;
		totalVenta += valorProductos;
	}
	
	private void limpiarCampos(){
		
		productosCompra = new ArrayList<DetalleVenta>();
		nombre = "";
		apellido = "";
		correo = "";
		telefono = "";
		cedula = "";
		totalVenta = 0;
		cliente = null;
		
	}
	
	/**
	 * Crea la factura que se asiganará a los detalle venta
	 */
	public void vender(){
		
		factura = new FacturaVenta();
		
		if (cliente != null){
			factura.setClienteId(cliente);
			factura.setFechaVenta(new Date());
			factura.setTotal(totalVenta);
			
			ventaEJB.registrarVenta(factura);
			
			registrarDetallesVenta();
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, 
							"La venta se ha registrado exitosamente", null));
			
			limpiarCampos();
			
		} else {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, 
							"Debe buscar o registrar un cliente previamente", null));
			
		}
		
	}
	
	/**
	 * Registra un cliente
	 */
	public void registrarCliente(){
		
		cliente = new Usuario();
		cliente.setApellido(apellido);
		cliente.setNombre(nombre);
		cliente.setCedula(cedula);
		cliente.setCorreo(correo);
		cliente.setTelefono(telefono);
		cliente.setMunicipio(municipioSeleccionado);
		
		//Genero
		TipoUsuario tipoUsuario = new TipoUsuario();
		tipoUsuario.setId(3);
		cliente.setTipoUsuario(tipoUsuario);
		
		try{
		usuarioEJB.registrarUsuario(cliente);
		clienteExiste = true;
		
		} catch (ExcepcionNegocio e){
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), null));
			
		}
		
	}
	
	/**
	 * Busca un cliente
	 */
	public void buscarCliente(){
		cliente = usuarioEJB.buscarCliente(cedula);
		if (cliente != null){
			
			clienteExiste = true;
			
			nombre = cliente.getNombre();
			apellido = cliente.getApellido();
			correo = cliente.getCorreo();
			telefono = cliente.getTelefono();
			municipioSeleccionado = cliente.getMunicipio();
			
		} else {
			
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, 
							"El cliente no se encuentra registrado, "
							+ "este debe ser registrado para continuar con la venta", null));
			
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
	public Genero getGenero() {
		return genero;
	}
	public void setGenero(Genero genero) {
		this.genero = genero;
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
	public List<Producto> getProductos() {
		return productos;
	}
	public void setProductos(List<Producto> productos) {
		this.productos = productos;
	}

	public Departamento getDeptoSeleccionado() {
		return deptoSeleccionado;
	}

	public void setDeptoSeleccionado(Departamento deptoSeleccionado) {
		this.deptoSeleccionado = deptoSeleccionado;
	}

	public Municipio getMunicipioSeleccionado() {
		return municipioSeleccionado;
	}

	public void setMunicipioSeleccionado(Municipio municipioSeleccionado) {
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

	public boolean isClienteExiste() {
		return clienteExiste;
	}

	public void setClienteExiste(boolean clienteExiste) {
		this.clienteExiste = clienteExiste;
	}

	public boolean isBtnAgregarCarritoSeleccionado() {
		return btnAgregarCarritoSeleccionado;
	}

	public void setBtnAgregarCarritoSeleccionado(boolean btnAgregarCarritoSeleccionado) {
		this.btnAgregarCarritoSeleccionado = btnAgregarCarritoSeleccionado;
	}
	
	
	
}
