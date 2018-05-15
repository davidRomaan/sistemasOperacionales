package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;

@LocalBean
@Stateless
public class VentaEJB {

	@EJB
	private Persistencia em;

	/**
	 * Se registra la venta en la base de datos
	 * 
	 * @param v
	 *            venta que se desea registrar
	 */
	public void registrarVenta(FacturaVenta v) {
		em.setBd(ConexionEJB.getBd());
		em.crear(v);
	}

	/**
	 * Obtiene el último código de la factura generada a un cliente
	 * 
	 * @param cedulaCliente
	 *            cédula del clienteal cual se le generó la factura
	 * @return el id de la última factura
	 */
	public int codigoUltimaFacturaCliente(String cedulaCliente) {
		em.setBd(ConexionEJB.getBd());
		return em.codigoUltimaFacturaCliente(cedulaCliente);
	}

	/**
	 * Lista las facturas registradas en una fecha determinada
	 * 
	 * @param fecha
	 *            la fecha ingresada
	 * @return la lista de facturas registradas en esa fecha
	 */
	public List<FacturaVenta> listarFacturasPorFecha(String fecha) {
		em.setBd(ConexionEJB.getBd());
		int dia = obtenerDia(fecha);
		int mes = obtenerMes(fecha);
		int anio = obtenerAnio(fecha);
		return em.listarFacturasPorFecha(FacturaVenta.LISTAR_POR_FECHA, dia, mes, anio);
	}

	/**
	 * Convierte un formato de fecha Calendar a string
	 * 
	 * @param fecha
	 *            fecha que se desea convertir
	 * @return la fecha en formato String
	 */
	public String convertirCalendarAString(Calendar fecha) {
		int anio = fecha.get(Calendar.YEAR);
		int mes = fecha.get(Calendar.MONTH);
		int dia = fecha.get(Calendar.DAY_OF_MONTH);
		return anio + "-" + mes + "-" + dia;
	}

	/**
	 * Busca una factura por su código
	 * 
	 * @param codigo
	 *            codigo de la factura
	 * @return la factura si la encuentra, de lo contrario null
	 */
	public FacturaVenta buscarFactura(int codigo) {
		em.setBd(ConexionEJB.getBd());
		return (FacturaVenta) em.buscar(FacturaVenta.class, codigo);
	}

	/**
	 * Convierte una fecha de tipo String a Date
	 * 
	 * @param fecha
	 *            fecha que se desea convertir
	 * @return la fecha en tipo Date
	 */
	public Calendar convertirFechaStrintADate(String fecha) {
		String[] datos = fecha.split("/");
		int dia = Integer.parseInt(datos[0]);
		int mes = Integer.parseInt(datos[1]);
		int anio = Integer.parseInt(datos[2]);

		Calendar c = new GregorianCalendar();
		c.set(anio, mes, dia);

		System.out.println(c.get(Calendar.DATE) + "-" + c.get(Calendar.MONTH) + "-" + c.get(Calendar.YEAR));

		return c;

	}

	/**
	 * Obtiene el día de una fecha ingresada
	 * 
	 * @param fecha
	 *            fecha ingresada por el usuario
	 * @return el día de la fecha ingresada
	 */
	public int obtenerDia(String fecha) {
		String datos[] = fecha.split("/");
		return Integer.parseInt(datos[0]);
	}

	/**
	 * Obtiene el mes de una fecha ingresada
	 * 
	 * @param fecha
	 *            fecha ingresada por el usuario
	 * @return el mes de la fecha ingresada
	 */
	public int obtenerMes(String fecha) {
		String datos[] = fecha.split("/");
		return Integer.parseInt(datos[1]);
	}

	/**
	 * Obtiene el anio de una fecha ingresada
	 * 
	 * @param fecha
	 *            fecha ingresada por el usuario
	 * @return el anio de la fecha ingresada
	 */
	public int obtenerAnio(String fecha) {
		String datos[] = fecha.split("/");
		return Integer.parseInt(datos[2]);
	}

	public List<DetalleVenta> listarDetallesVentaFactura(FacturaVenta factura) {
		em.setBd(ConexionEJB.getBd());
		return (List<DetalleVenta>) (Object) em.listarConParametroObjeto(DetalleVenta.LISTAR_DETALLES_FACTURA, factura);
	}

	public void eliminarDetalleVenta(DetalleVenta dv) {
		em.setBd(ConexionEJB.getBd());
		em.eliminarDetalleVenta(dv);
	}

	public void eliminarVenta(FacturaVenta fv) {
		em.setBd(ConexionEJB.getBd());
		em.eliminar(fv);
	}

	public String obtenerFechaActual() {

		Calendar fechaActual = new GregorianCalendar();
		int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
		int mes = fechaActual.get(Calendar.MONTH) + 1;
		int anio = fechaActual.get(Calendar.YEAR);

		String diaActual = String.valueOf(dia);
		String mesActual = String.valueOf(mes);

		if (dia <= 9) {
			diaActual = "0" + dia;
		}

		if (mes <= 9) {
			mesActual = "0" + mes;
		}

		String nuevaFecha = diaActual + "/" + mesActual + "/" + anio;

		return nuevaFecha;
	}

}
