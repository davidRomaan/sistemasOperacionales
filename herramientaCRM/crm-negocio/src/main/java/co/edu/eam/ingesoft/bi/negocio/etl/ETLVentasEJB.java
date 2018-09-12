package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Auditoria;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionMunicipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.Foto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@LocalBean
@Stateless
public class ETLVentasEJB {

	@EJB
	private Persistencia em;

	@EJB
	private VentaEJB ventaEJB;

	public void registrarFoto(Foto f) {
		em.setBd(ConexionEJB.getBd());
		em.crear(f);
	}

	public List<FacturaVenta> listaVentasPeriodo(String fecha1, String fecha2, int bd) {

		em.setBd(bd);

		List<Object> lista = em.listarFacturasIntervaloFecha(fecha1, fecha2);

		List<FacturaVenta> listaFacturas = new ArrayList<FacturaVenta>();

		for (int i = 0; i < lista.size(); i++) {

			int cod = (int) (Integer) lista.get(i);

			FacturaVenta factura = (FacturaVenta) em.buscar(FacturaVenta.class, cod);

			listaFacturas.add(factura);

		}

		return listaFacturas;
	}

	/**
	 * Obtiene todos los datos asociados a un hecho venta
	 * 
	 * @param fechaInicio
	 *            fehca de inicio desde la cual se desea cargar los datos
	 * @param fechaFin
	 *            fecha final hasta la cual se desea cargr los datos
	 */
	public List<HechoVentas> obtenerDatosHechoVentasAcumulacionSimple(String fechaInicio, String fechaFin, int bd,
			List<HechoVentas> listaHechos) {

		// Lista de facturas registradas en la bd
		List<FacturaVenta> listaFacturasP = listaVentasPeriodo(fechaInicio, fechaFin, bd);

		if (listaFacturasP.size() == 0) {

			throw new ExcepcionNegocio("No hay facturas registradas en el periodo ingresado");

		} else {

			em.setBd(bd);

			// Se recorre la lista obtenida de la bd
			for (FacturaVenta facturaVenta : listaFacturasP) {

				List<DetalleVenta> detalles = (List<DetalleVenta>) (Object) em
						.listarConParametroObjeto(DetalleVenta.LISTAR_DETALLES_FACTURA, facturaVenta);

				for (DetalleVenta detalleVenta : detalles) {

					HechoVentas hecho = new HechoVentas();
					hecho.setEmpleado(crearDimensionEmpleado(facturaVenta));
					hecho.setMunicipio(crearDimensionMunicipio(facturaVenta));
					hecho.setPersona(crearDimensionCliente(facturaVenta));
					hecho.setProducto(crearDimensionProducto(detalleVenta));

					hecho.setSubtotal(detalleVenta.getSubtotal());
					hecho.setUnidades(detalleVenta.getCantidad());
					hecho.setFecha(facturaVenta.getFechaVenta());

					listaHechos.add(hecho);

				}
			}
		}

		return listaHechos;

	}

	/**
	 * vacía las tablas relacionados con las ventas en la base de datos de
	 * oracle
	 */
	public void limpiarDWH() {

		em.limpiarDWH("HECHO_VENTAS");
		em.limpiarDWH("DIMENSION_MUNICIPIO");
		em.limpiarDWH("DIMENSION_PERSONA");
		em.limpiarDWH("DIMENSION_PRODUCTO");

	}

	/**
	 * Carga los datos transformados en la base de datos de oralce
	 * 
	 * @param hechos
	 *            lista que se desea cargar en la bd
	 */
	public void cargarDatosDWH(List<HechoVentas> hechos) {

		int fila = 0;

		List<String> cedulasEmpleados = new ArrayList<String>();
		List<String> cedulasClientes = new ArrayList<String>();
		List<String> nombresMunicipios = new ArrayList<String>();
		List<String> nombreProductos = new ArrayList<String>();

		List<DimensionPersona> empleadosRegistrados = new ArrayList<DimensionPersona>();
		List<DimensionProducto> productosRegistrados = new ArrayList<DimensionProducto>();
		List<DimensionMunicipio> municipiosRegistrados = new ArrayList<DimensionMunicipio>();
		List<DimensionPersona> clientesRegistrados = new ArrayList<DimensionPersona>();

		DimensionPersona empleadoBuscado = null;
		DimensionProducto productoBuscado = null;
		DimensionMunicipio municipioBuscado = null;
		DimensionPersona clienteBuscado = null;

		for (HechoVentas hechoVentas : hechos) {

			fila++;

			DimensionProducto productoHecho = hechoVentas.getProducto();
			DimensionMunicipio municipioHecho = hechoVentas.getMunicipio();

			String cedulaCliente = hechoVentas.getPersona().getCedula();
			String cedulaEmpleado = hechoVentas.getEmpleado().getCedula();

			int edadEmpleado = hechoVentas.getEmpleado().getEdad();
			int edadCliente = hechoVentas.getPersona().getEdad();

			double precioProducto = hechoVentas.getProducto().getPrecio();
			if (precioProducto <= 0) {
				throw new ExcepcionNegocio("Debe cambiar el precio del producto de la fila " + fila);
			}

			if (edadCliente > 130 || edadCliente < 10) {
				edadEmpleado = 35;
			}
			
			if (edadEmpleado > 130 || edadEmpleado < 10) {
				edadEmpleado = 35;
			}

			empleadoBuscado = em.dimensionPersonaExiste(cedulaEmpleado);

			boolean empleadoRegistrado = false;

			if (empleadoBuscado == null && !cedulasEmpleados.contains(cedulaEmpleado)) {

				empleadoBuscado = new DimensionPersona();

				hechoVentas.getEmpleado().setId(empleadoBuscado.getId());

				empleadoBuscado = hechoVentas.getEmpleado();

				em.crearDimension(empleadoBuscado);

				empleadosRegistrados.add(empleadoBuscado);
				cedulasEmpleados.add(cedulaEmpleado);
			}
			
			if (!empleadoRegistrado && empleadoBuscado == null){
				
				for (DimensionPersona dimensionPersona : empleadosRegistrados) {
					
					if (dimensionPersona.getCedula().equals(cedulaEmpleado)){
						
						hechoVentas.setEmpleado(dimensionPersona);
						break;
						
					}
					
				}
				
			} else {
				
				hechoVentas.setEmpleado(empleadoBuscado);
				
			}

			productoBuscado = (DimensionProducto) em.dimensionExiste(DimensionProducto.BUSCAR_NOMBRE,
					productoHecho.getNombre());

			boolean productoRegistrado = false;

			if (productoBuscado == null && !nombreProductos.contains(productoHecho.getNombre())) {

				productoRegistrado = true;

				productoBuscado = new DimensionProducto();

				productoHecho.setId(productoBuscado.getId());

				productoBuscado = productoHecho;

				em.crearDimension(productoBuscado);

				nombreProductos.add(productoBuscado.getNombre());
				productosRegistrados.add(productoBuscado);

			}
			
			if (!productoRegistrado && productoBuscado == null){
				
				for (DimensionProducto dimensionProducto : productosRegistrados) {
					
					if (dimensionProducto.getNombre().equals(productoHecho.getNombre())){
						
						hechoVentas.setProducto(dimensionProducto);
						break;
						
					}
					
				}
				
			} else {
				
				hechoVentas.setProducto(productoBuscado);
				
			}

			municipioBuscado = (DimensionMunicipio) em.dimensionExiste(DimensionMunicipio.BUSCAR_NOMBRE,
					municipioHecho.getNombre());

			boolean municipioRegistrado = false;

			if (municipioBuscado == null && !nombresMunicipios.contains(municipioHecho.getNombre())) {

				municipioRegistrado = true;

				municipioBuscado = new DimensionMunicipio();

				municipioHecho.setId(municipioBuscado.getId());

				municipioBuscado = municipioHecho;

				em.crearDimension(municipioBuscado);

				nombresMunicipios.add(municipioBuscado.getNombre());
				municipiosRegistrados.add(municipioBuscado);

			}
			
			if (!municipioRegistrado && municipioBuscado == null){
				
				for (DimensionMunicipio dimensionMunicipio : municipiosRegistrados) {
					
					if (dimensionMunicipio.getNombre().equals(municipioHecho.getNombre())){
						
						hechoVentas.setMunicipio(dimensionMunicipio);
						break;
						
					}
					
				}
				
			} else {
				
				hechoVentas.setMunicipio(municipioBuscado);
				
			}

			clienteBuscado = em.dimensionPersonaExiste(cedulaCliente);
			
			boolean clienteRegistrado = false;

			if (clienteBuscado == null && !cedulasClientes.contains(cedulaCliente)) {
				
				clienteRegistrado = true;
				
				clienteBuscado = new DimensionPersona();
				
				hechoVentas.getPersona().setId(clienteBuscado.getId());
				
				clienteBuscado = hechoVentas.getPersona();
				
				em.crearDimension(clienteBuscado);
				
				cedulasClientes.add(cedulaCliente);
				clientesRegistrados.add(clienteBuscado);
			}
			
			if (!clienteRegistrado && clienteBuscado == null){
				
				for (DimensionPersona dimensionPersona : clientesRegistrados) {
					
					if (dimensionPersona.getCedula().equals(cedulaCliente)){
						
						hechoVentas.setPersona(dimensionPersona);
						break;
						
					}
					
				}
				
			} else {
				
				hechoVentas.setPersona(clienteBuscado);
				
			}

			em.crearHechoVentas(hechoVentas);

		}

	}

	/**
	 * Crea la dimensiï¿½n del municipio que se desea asignar al hecho de ventas
	 * 
	 * @param facturaVenta
	 *            factura de la venta en la que se encuetra el municipio
	 */
	private DimensionMunicipio crearDimensionMunicipio(FacturaVenta facturaVenta) {

		DimensionMunicipio dimensionMunicipio = new DimensionMunicipio();
		Municipio municipio = facturaVenta.getEmpleadoId().getMunicipio();
		dimensionMunicipio.setDepartamento(municipio.getDepartamento().getNombre());
		dimensionMunicipio.setId(municipio.getId());
		dimensionMunicipio.setNombre(municipio.getNombre());

		return dimensionMunicipio;

	}

	/**
	 * crea la dimension de tipo empleado
	 * 
	 * @param facturaVenta
	 *            factura en la cual esta el empleado registrado
	 */
	private DimensionPersona crearDimensionEmpleado(FacturaVenta facturaVenta) {

		DimensionPersona dimensionEmpleado = new DimensionPersona();
		Persona empleado = facturaVenta.getEmpleadoId();
		dimensionEmpleado.setNombre(empleado.getNombre());
		dimensionEmpleado.setApellido(empleado.getApellido());
		dimensionEmpleado.setCedula(empleado.getCedula());
		dimensionEmpleado.setEdad((short) calcularEdad(empleado.getFechaNacimiento()));
		dimensionEmpleado.setGenero(String.valueOf(empleado.getGenero()));
		dimensionEmpleado.setTipoPersona("empleado");

		return dimensionEmpleado;

	}

	/**
	 * crea la deimension de tipo cliente
	 * 
	 * @param facturaVenta
	 *            factura en la cual esta el cliente registrado
	 */
	private DimensionPersona crearDimensionCliente(FacturaVenta facturaVenta) {

		DimensionPersona dimensionCliente = new DimensionPersona();
		Persona cliente = facturaVenta.getClienteId();
		dimensionCliente.setNombre(cliente.getNombre());
		dimensionCliente.setApellido(cliente.getApellido());
		dimensionCliente.setCedula(cliente.getCedula());
		dimensionCliente.setEdad((short) calcularEdad(cliente.getFechaNacimiento()));
		dimensionCliente.setGenero(String.valueOf(cliente.getGenero()));
		dimensionCliente.setTipoPersona("cliente");

		return dimensionCliente;

	}

	/**
	 * Crea una dimendion del producto que se agregarï¿½ al hecho venta
	 * 
	 * @param detalleVenta
	 *            detalle de venta en la que se encuentra el producto
	 */
	private DimensionProducto crearDimensionProducto(DetalleVenta detalleVenta) {

		DimensionProducto dimensionProducto = new DimensionProducto();
		Producto producto = detalleVenta.getProducto();
		dimensionProducto.setPrecio(producto.getValorProducto());
		dimensionProducto.setId(producto.getId());
		dimensionProducto.setNombre(producto.getNombre());

		String tipoProducto = producto.getTipoProducto().getNombre();

		dimensionProducto.setTipoProducto(tipoProducto.toUpperCase());

		return dimensionProducto;

	}

	private List<HechoVentas> crearHechosVenta(List<FacturaVenta> listaFacturas, List<HechoVentas> listaHechos) {

		// Se recorre la lista obtenida de la bd
		for (FacturaVenta facturaVenta : listaFacturas) {

			List<DetalleVenta> detalles = (List<DetalleVenta>) (Object) em
					.listarConParametroObjeto(DetalleVenta.LISTAR_DETALLES_FACTURA, facturaVenta);

			for (DetalleVenta detalleVenta : detalles) {

				HechoVentas hecho = new HechoVentas();
				hecho.setEmpleado(crearDimensionEmpleado(facturaVenta));
				hecho.setMunicipio(crearDimensionMunicipio(facturaVenta));
				hecho.setPersona(crearDimensionCliente(facturaVenta));
				hecho.setProducto(crearDimensionProducto(detalleVenta));

				hecho.setSubtotal(detalleVenta.getSubtotal());
				hecho.setUnidades(detalleVenta.getCantidad());
				hecho.setFecha(facturaVenta.getFechaVenta());
				listaHechos.add(hecho);

			}
		}

		return listaHechos;

	}

	// ------------------------ Rolling ------------------------------

	public List<HechoVentas> obtenerHechoVentasRollingDia(String fecha, int bd, List<HechoVentas> listaHechos) {

		List<FacturaVenta> listaFacturas = listaVentasPeriodo(fecha, fecha, bd);

		if (listaFacturas.size() == 0) {

			throw new ExcepcionNegocio("No hay facturas registradas en el día ingresado");

		} else {

			em.setBd(bd);

			listaHechos = crearHechosVenta(listaFacturas, listaHechos);

		}

		return listaHechos;

	}

	private List<FacturaVenta> obtenerFacturasPorMes(String fecha, int bd) {

		String datos[] = fecha.split("-");

		int mes = Integer.parseInt(datos[1]);
		int anio = Integer.parseInt(datos[0]);

		List<FacturaVenta> facturas = new ArrayList<FacturaVenta>();

		List<Object> codigos = em.listaFacturasMes(mes, anio, bd);

		for (Object object : codigos) {

			int cod = (Integer) object;

			em.setBd(bd);

			FacturaVenta factura = (FacturaVenta) em.buscar(FacturaVenta.class, cod);

			facturas.add(factura);

		}

		return facturas;

	}

	private List<FacturaVenta> obtenerFacturasPorAnio(String fecha, int bd) {

		String datos[] = fecha.split("-");

		int anio = Integer.parseInt(datos[0]);

		List<FacturaVenta> facturas = new ArrayList<FacturaVenta>();

		List<Object> codigos = em.listaFacturasAnio(anio, bd);

		for (Object object : codigos) {

			int cod = (Integer) object;

			em.setBd(bd);

			FacturaVenta factura = (FacturaVenta) em.buscar(FacturaVenta.class, cod);

			facturas.add(factura);

		}

		return facturas;

	}

	public List<HechoVentas> obtnerHechoVentasRollingMes(String fecha, int bd, List<HechoVentas> listaHechos) {

		List<FacturaVenta> facturas = obtenerFacturasPorMes(fecha, bd);

		if (facturas.size() == 0) {

			throw new ExcepcionNegocio("No hay facturas registradas en el mes ingresado");

		} else {

			em.setBd(bd);

			listaHechos = crearHechosVenta(facturas, listaHechos);

		}

		return listaHechos;

	}

	public List<HechoVentas> obtnerHechoVentasRollingAnio(String fecha, int bd, List<HechoVentas> listaHechos) {

		List<FacturaVenta> facturas = obtenerFacturasPorAnio(fecha, bd);

		if (facturas.size() == 0) {

			throw new ExcepcionNegocio("No hay facturas registradas en el año ingresado");

		} else {

			em.setBd(bd);

			listaHechos = crearHechosVenta(facturas, listaHechos);

		}

		return listaHechos;

	}

	public List<HechoVentas> obtenerHechoVentasRollingSemana(String fecha, int bd, List<HechoVentas> listaHechos) {

		return null;

	}

	/**
	 * Calcula la edad de una persona
	 * 
	 * @param fechaNacimiento
	 *            fecha de nacimiento de la persona
	 * @return la edad de la persona
	 */
	public int calcularEdad(String fechaNacimiento) {

		if (fechaNacimiento.equals("")) {
			return 35;
		}

		String fecha[] = fechaNacimiento.split("/");
		int anio = Integer.parseInt(fecha[2]);

		Calendar fechaActual = new GregorianCalendar();
		int anioActual = fechaActual.get(Calendar.YEAR);

		int edad = anioActual - anio;

		return edad;

	}

	// ------------------------- Transformación --------------------

	/**
	 * Transforma los datos que se desea cargar en la bd de oracle
	 * 
	 * @param hechos
	 *            lista de hecho de ventas que se desea tranformar
	 * @return la lista de hecho de ventas transformada
	 */
	public List<HechoVentas> transformarDatos(List<HechoVentas> hechos) {

		for (HechoVentas hecho : hechos) {

			DimensionProducto producto = hecho.getProducto();

			if (producto.getPrecio() <= 0) {

				double promedio = calcularPromedioProductos(hechos);
				producto.setPrecio(promedio);

				hecho.setSubtotal(promedio * hecho.getUnidades());

			}

			DimensionPersona cliente = hecho.getPersona();

			if (cliente.getEdad() < 10 || cliente.getEdad() > 130) {

				cliente.setEdad((short) calcularPromedioEdades(hechos));

			}

		}

		return hechos;

	}

	/**
	 * Calcula el promedio de la edades de las personas de la lista
	 * 
	 * @param hechos
	 *            lista de hechos de venta en la cual se encuetra la información
	 *            de las personas
	 * @return el promedio de las edades de las personas
	 */
	public int calcularPromedioEdades(List<HechoVentas> hechos) {

		int sumaEdades = 0;
		int cantidad = 0;

		for (HechoVentas hechoVentas : hechos) {

			short edad = hechoVentas.getPersona().getEdad();

			if (edad > 10 && edad <= 130) {

				cantidad++;
				sumaEdades += edad;

			}

		}

		if (cantidad == 0) {
			return sumaEdades;
		}

		return sumaEdades / cantidad;

	}

	/**
	 * Calcula el promedio de los precios de los productos de la lista
	 * 
	 * @param hechos
	 *            lista de hechos de ventas en la cual están registrados los
	 *            productos
	 * @return el promedio
	 */
	public double calcularPromedioProductos(List<HechoVentas> hechos) {

		double precios = 0;
		int cantidad = 0;

		for (HechoVentas hecho : hechos) {

			double precio = hecho.getProducto().getPrecio();

			if (precio > 0) {

				precios += precio;
				cantidad++;

			}

		}

		if (cantidad == 0) {
			return precios;
		}

		double promedio = precios / cantidad;

		return promedio;

	}

}
