package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import org.hibernate.jpa.event.spi.jpa.ListenerFactory;

import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.negocio.beans.VentaEJB;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Producto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionFactura;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionMunicipio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionPersona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.DimensionProducto;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@LocalBean
@Stateless
public class ETLVentasEJB {

	@EJB
	private Persistencia em;

	@EJB
	private VentaEJB ventaEJB;

	public List<FacturaVenta> listaVentasPeriodo(Calendar fechaInicio, Calendar fechaFin, int bd) {

		em.setBd(bd);

		String fecha1 = ventaEJB.convertirCalendarAString(fechaInicio);
		String fecha2 = ventaEJB.convertirCalendarAString(fechaFin);

		List<Object> lista = em.listarFacturasIntervaloFecha(fecha1, fecha2);

		System.out.println("Tamaño lista: " + lista.size());

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
	public List<HechoVentas> obtenerDatosHechoVentasAcumulacionSimple(Calendar fechaInicio, Calendar fechaFin,
			int bd, List<HechoVentas> listaHechos) {
		
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
					hecho.setFactura(crearDimensionFactura(facturaVenta));
					hecho.setMunicipio(crearDimensionMunicipio(facturaVenta));
					hecho.setPersona(crearDimensionCliente(facturaVenta));
					hecho.setProducto(crearDimensionProducto(detalleVenta));
					hecho.setSubtotal(detalleVenta.getSubtotal());
					hecho.setUnidades(detalleVenta.getCantidad());
					
					listaHechos.add(hecho);

				}
			}
		}
		
		return listaHechos;
		
	}
	
	public void cargarDatosDWH(List<HechoVentas> hechos){
		
		int fila = 0;
		
		List<Integer> codigosFacturas = new ArrayList<Integer>();
		List<String> cedulasEmpleados = new ArrayList<String>();
		List<String> cedulasClientes = new ArrayList<String>();
		List<Integer> codigosMunicipios = new ArrayList<Integer>();
		List<Integer> codigosProductos = new ArrayList<Integer>();
		
		for (HechoVentas hechoVentas : hechos) {
			
			fila++;
			
			int unidades = hechoVentas.getUnidades();
			double subtotal = hechoVentas.getSubtotal();
			int idFactura = hechoVentas.getFactura().getId();
			String cedulaCliente = hechoVentas.getPersona().getCedula();
			int idMunicipio = hechoVentas.getMunicipio().getId();
			int idProducto = hechoVentas.getProducto().getId();
			String cedulaEmpleado = hechoVentas.getEmpleado().getCedula();
			
			int edadEmpleado = hechoVentas.getEmpleado().getEdad();
			int edadCliente = hechoVentas.getPersona().getEdad();
			
			double precioProducto = hechoVentas.getProducto().getPrecio();
			if (precioProducto <= 0){
				throw new ExcepcionNegocio("Debe cambiar el precio del producto de la fila " + fila);
			}
			
			if (edadCliente > 130 || edadEmpleado > 130){
				throw new ExcepcionNegocio("Debe cambiar la edad de la persona ubicada en la fila " + fila);
			}
			
			if (!em.dimensionExiste(idFactura, "DIMENSION_FACTURA") && !codigosFacturas.contains(idFactura)){
				em.crearDimensionFactura(hechoVentas.getFactura());
				codigosFacturas.add(idFactura);
			}
						
			if (!em.dimensionPersonaExiste(cedulaEmpleado) && !cedulasEmpleados.contains(cedulaEmpleado)){
				//em.eliminarDimensionPersona(cedulaEmpleado);
				em.crearDimensionPersona(hechoVentas.getEmpleado());
				cedulasEmpleados.add(cedulaEmpleado);
			} 			
			//em.crearDimensionPersona(hechoVentas.getEmpleado());
			
			if (!em.dimensionExiste(idProducto, "DIMENSION_PRODUCTO") && !codigosProductos.contains(idProducto)){
				//em.eliminarDimensionProducto(idProducto);
				em.crearDimensionProducto(hechoVentas.getProducto());
				codigosProductos.add(idProducto);
			}
			//em.crearDimensionProducto(hechoVentas.getProducto());
			
			if (!em.dimensionExiste(idMunicipio, "DIMENSION_MUNICIPIO") && !codigosMunicipios.contains(idMunicipio)){
				em.crearDimensionMunicipio(hechoVentas.getMunicipio());
				codigosMunicipios.add(idMunicipio);
			}
			
			if (!em.dimensionPersonaExiste(cedulaCliente) && !cedulasClientes.contains(cedulaCliente)){
				//em.eliminarDimensionPersona(cedulaCliente);
				em.crearDimensionPersona(hechoVentas.getPersona());
				cedulasClientes.add(cedulaCliente);
			}						
//			em.crearDimensionPersona(hechoVentas.getPersona());
			
			em.crearHechoVentas(unidades, subtotal, idFactura, cedulaCliente, 
					idMunicipio, idProducto, cedulaEmpleado);
			
		}
		
	}

	/**
	 * Crea un hecho venta en la bd
	 * 
	 * @param facturaVenta
	 *            factura donde se encuentra la inforamción de la venta
	 * @param detalleVenta
	 *            detalle de venta de la factura
	 */
	private void crearHechoVentas(FacturaVenta facturaVenta, DetalleVenta detalleVenta) {

		int idFactura = facturaVenta.getId();

		Municipio municipio = facturaVenta.getEmpleadoId().getMunicipio();
		int idMunicipio = municipio.getId();

		Persona cliente = facturaVenta.getClienteId();
		String cedulaCliente = cliente.getCedula();

		Persona empleado = facturaVenta.getEmpleadoId();
		String cedulaEmpleado = empleado.getCedula();

		Producto producto = detalleVenta.getProducto();
		int idProducto = producto.getId();

		int unidades = detalleVenta.getCantidad();
		double subtotal = unidades * producto.getValorProducto();

		em.crearHechoVentas(unidades, subtotal, idFactura, cedulaCliente, idMunicipio, idProducto, cedulaEmpleado);

	}

	/**
	 * Crea una dimensión de factura que se agregará al hecho de venta
	 * 
	 * @param facturaVenta
	 *            factura generada
	 */
	private DimensionFactura crearDimensionFactura(FacturaVenta facturaVenta) {

		DimensionFactura dimensionFactura = new DimensionFactura();
		dimensionFactura.setFecha(facturaVenta.getFechaVenta());
		dimensionFactura.setId(facturaVenta.getId());
		dimensionFactura.setTotalVenta(facturaVenta.getTotal());

		return dimensionFactura;

	}

	/**
	 * Crea la dimensión del municipio que se desea asignar al hecho de ventas
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
	 * Crea una dimendion del producto que se agregará al hecho venta
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
		dimensionProducto.setTipoProducto(producto.getTipoProducto().getNombre());

		return dimensionProducto;

	}

	/**
	 * Calcula la edad de una persona
	 * 
	 * @param fechaNacimiento
	 *            fecha de nacimiento de la persona
	 * @return la edad de la persona
	 */
	public int calcularEdad(String fechaNacimiento) {
		
		if (fechaNacimiento.equals("")){
			return 2018;
		}

		String fecha[] = fechaNacimiento.split("/");
		int anio = Integer.parseInt(fecha[2]);

		Calendar fechaActual = new GregorianCalendar();
		int anioActual = fechaActual.get(Calendar.YEAR);

		int edad = anioActual - anio;

		return edad;

	}

}
