package co.edu.eam.ingesoft.bi.negocio.beans;

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

	public List<FacturaVenta> listarFacturasPorFecha(String fecha) {
		em.setBd(ConexionEJB.getBd());
		return (List<FacturaVenta>) (Object) em.listarConParametroString(FacturaVenta.LISTAR_POR_FECHA, fecha);
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
