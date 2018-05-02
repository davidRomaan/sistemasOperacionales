package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;

@LocalBean
@Stateless
public class VentaEJB {

	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Se registra la venta en la base de datos
	 * @param v venta que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarVenta (FacturaVenta v){
		em.persist(v);
	}
	
	/**
	 * Obtiene el último código de la factura generada a un cliente
	 * @param cedulaCliente cédula del clienteal cual se le generó la factura
	 * @return el id de la última factura
	 */
	public int codigoUltimaFacturaCliente (String cedulaCliente){
		Query q = em.createNamedQuery(FacturaVenta.OBTENER_ULTIMA_REGISTRADA);
		q.setParameter(1, cedulaCliente);
		return (int) ((Integer)q.getSingleResult());
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<FacturaVenta> listarFacturasPorFecha(String fecha){
		Query q = em.createNamedQuery(FacturaVenta.LISTAR_POR_FECHA);
		q.setParameter(1, fecha);
		List<FacturaVenta> facturas = q.getResultList();
		return facturas;
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<DetalleVenta> listarDetallesVentaFactura(FacturaVenta factura){
		Query q = em.createNamedQuery(DetalleVenta.LISTAR_DETALLES_FACTURA);
		q.setParameter(1, factura);
		List<DetalleVenta> lista = q.getResultList();
		return lista;
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarDetalleVenta (DetalleVenta dv){
		
		Query q = em.createNativeQuery("DELETE FROM DETALLE_VENTA WHERE "
				+ "factura_venta_id = ?1 AND producto_id = ?2");
		q.setParameter(1, dv.getFacturaVenta().getId());
		q.setParameter(2, dv.getProducto().getId());
		q.executeUpdate();
		
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarVenta (FacturaVenta fv){
		em.remove(em.contains(fv) ? fv : em.merge(fv));
	}
	
	public String obtenerFechaActual(){
		
		Calendar fechaActual = new GregorianCalendar();
		int dia = fechaActual.get(Calendar.DAY_OF_MONTH);
		int mes = fechaActual.get(Calendar.MONTH)+1;
		int anio = fechaActual.get(Calendar.YEAR);
		
		String diaActual = String.valueOf(dia);
		String mesActual = String.valueOf(mes);
		
		if (dia <= 9){
			diaActual = "0" + dia;
		}
		
		if (mes <= 9){
			mesActual = "0" + mes;
		}

		String nuevaFecha = diaActual + "/" + mesActual + "/" + anio;
		
		return nuevaFecha;
	}
	
}
