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
	

	public List<FacturaVenta> listaVentasPeriodo(Calendar fechaInicio, Calendar fechaFin, int bd) {
		em.setBd(bd);
		List<FacturaVenta> listaFacturas = (List<FacturaVenta>) (Object) em
				.listarConDosParametrosObjeto(FacturaVenta.FACTURAS_FECHA_INICIO_FIN, fechaInicio, fechaFin);
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
	public void obtenerDatosHechoVentasAcumulacionSimple(Calendar fechaInicio, Calendar fechaFin) {

//		// Lista de facturas registradas en la bd mysql
//		List<FacturaVenta> listaFacturas = listaVentasPeriodo(fechaInicio, fechaFin, 1);
//		// Lista de facturas registradas en la bd postgres
//		
//
//		if (listaFacturas.size() == 0) {
//
//			//throw new ExcepcionNegocio("No hay ventas registradas en el periodo ingresado");
//			System.out.println("No hay ventas MYSQL");
//
//		} else {
//
//			//Se recorre la primera lista obtenida de mysql
//			for (FacturaVenta facturaVenta : listaFacturas) {
//
//				List<DetalleVenta> detalles = (List<DetalleVenta>) (Object) em
//						.listarConParametroObjeto(DetalleVenta.LISTAR_DETALLES_FACTURA, facturaVenta);
//
//				for (DetalleVenta detalleVenta : detalles) {
//					
//					crearDimensionesVenta(facturaVenta, detalleVenta);
//					crearHechoVentas(facturaVenta, detalleVenta);
//
//				}
//			}
//
//		}
		
		List<FacturaVenta> listaFacturasP = listaVentasPeriodo(fechaInicio, fechaFin, 2);
		
		if (listaFacturasP.size() == 0){
			
			System.out.println("No hay datos");
			
		}
		
		//Se recorre la segunda lista obtenida de postgres
		for (FacturaVenta facturaVenta : listaFacturasP) {

			List<DetalleVenta> detalles = (List<DetalleVenta>) (Object) em
					.listarConParametroObjeto(DetalleVenta.LISTAR_DETALLES_FACTURA, facturaVenta);

			for (DetalleVenta detalleVenta : detalles) {
				
				System.out.println("Detalle " + detalleVenta.getSubtotal());
				
				crearDimensionesVenta(facturaVenta, detalleVenta);
				crearHechoVentas(facturaVenta, detalleVenta);

			}
		}

	}

	/**
	 * Crea un hecho venta en la bd
	 * @param facturaVenta factura donde se encuentra la inforamción de la venta
	 * @param detalleVenta detalle de venta de la factura
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
	 * Crea las dimensiones asocioadas al hecho venta
	 * @param facturaVenta factura donde se encuentra la información de la venta
	 * @param detalleVenta detalles de venta de la factura
	 */
	private void crearDimensionesVenta(FacturaVenta facturaVenta, DetalleVenta detalleVenta) {

		crearDimensionCliente(facturaVenta);
		crearDimensionEmpleado(facturaVenta);
		crearDimensionesVenta(facturaVenta, detalleVenta);
		crearDimensionFactura(facturaVenta);
		crearDimensionMunicipio(facturaVenta);
		crearDimensionProducto(detalleVenta);

	}

	/**
	 * Crea una dimensión de factura que se agregará al hecho de venta
	 * 
	 * @param facturaVenta
	 *            factura generada
	 */
	private void crearDimensionFactura(FacturaVenta facturaVenta) {

		DimensionFactura dimensionFactura = new DimensionFactura();
		dimensionFactura.setFecha(facturaVenta.getFechaVenta());
		dimensionFactura.setId(facturaVenta.getId());
		dimensionFactura.setTotalVenta(facturaVenta.getTotal());
		
		em.crearDimensionFactura(dimensionFactura);

	}

	/**
	 * Crea la dimensión del municipio que se desea asignar al hecho de ventas
	 * 
	 * @param facturaVenta
	 *            factura de la venta en la que se encuetra el municipio
	 */
	private void crearDimensionMunicipio(FacturaVenta facturaVenta) {

		DimensionMunicipio dimensionMunicipio = new DimensionMunicipio();
		Municipio municipio = facturaVenta.getEmpleadoId().getMunicipio();
		dimensionMunicipio.setDepartamento(municipio.getDepartamento().getNombre());
		dimensionMunicipio.setId(municipio.getId());
		dimensionMunicipio.setNombre(municipio.getNombre());

		em.crearDimensionMunicipio(dimensionMunicipio);
		
	}

	/**
	 * crea la deimension de tipo empleado
	 * 
	 * @param facturaVenta
	 *            factura en la cual esta el empleado registrado
	 */
	private void crearDimensionEmpleado(FacturaVenta facturaVenta) {

		DimensionPersona dimensionEmpleado = new DimensionPersona();
		Persona empleado = facturaVenta.getEmpleadoId();
		dimensionEmpleado.setNombre(empleado.getNombre());
		dimensionEmpleado.setApellido(empleado.getApellido());
		dimensionEmpleado.setCedula(empleado.getCedula());
		dimensionEmpleado.setEdad((short) calcularEdad(empleado.getFechaNacimiento()));
		dimensionEmpleado.setGenero(String.valueOf(empleado.getGenero()));
		dimensionEmpleado.setTipoPersona("empleado");
		
		em.crearDimensionPersona(dimensionEmpleado);

	}

	/**
	 * crea la deimension de tipo cliente
	 * 
	 * @param facturaVenta
	 *            factura en la cual esta el cliente registrado
	 */
	private void crearDimensionCliente(FacturaVenta facturaVenta) {

		DimensionPersona dimensionCliente = new DimensionPersona();
		Persona cliente = facturaVenta.getEmpleadoId();
		dimensionCliente.setNombre(cliente.getNombre());
		dimensionCliente.setApellido(cliente.getApellido());
		dimensionCliente.setCedula(cliente.getCedula());
		dimensionCliente.setEdad((short) calcularEdad(cliente.getFechaNacimiento()));
		dimensionCliente.setGenero(String.valueOf(cliente.getGenero()));
		dimensionCliente.setTipoPersona("cliente");
		
		em.crearDimensionPersona(dimensionCliente);

	}

	/**
	 * Crea una dimendion del producto que se agregará al hecho venta
	 * 
	 * @param detalleVenta
	 *            detalle de venta en la que se encuentra el producto
	 */
	private void crearDimensionProducto(DetalleVenta detalleVenta) {

		DimensionProducto dimensionProducto = new DimensionProducto();
		Producto producto = detalleVenta.getProducto();
		dimensionProducto.setPrecio(producto.getValorProducto());
		dimensionProducto.setId(producto.getId());
		dimensionProducto.setNombre(producto.getNombre());
		dimensionProducto.setTipoProducto(producto.getTipoProducto().getNombre());
		
		em.crearDimensionProducto(dimensionProducto);

	}

	/**
	 * Calcula la edad de una persona
	 * 
	 * @param fechaNacimiento
	 *            fecha de nacimiento de la persona
	 * @return la edad de la persona
	 */
	public int calcularEdad(String fechaNacimiento) {

		String fecha[] = fechaNacimiento.split("/");
		int anio = Integer.parseInt(fecha[2]);

		Calendar fechaActual = new GregorianCalendar();
		int anioActual = fechaActual.get(Calendar.YEAR);

		int edad = anioActual - anio;

		return edad;

	}

}
