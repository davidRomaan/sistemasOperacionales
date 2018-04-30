package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVenta;
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVentaPK;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;

@LocalBean
@Stateless
public class DetalleVentaEJB {

	@PersistenceContext
	EntityManager em;
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public DetalleVenta buscar(DetalleVentaPK pk){
		return em.find(DetalleVenta.class, pk);
	}
	
	/**
	 * Registra un detalle venta en la base de datos
	 * @param dv detalle venta que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarDetalleVenta (List<DetalleVenta> detalles, FacturaVenta factura){
				
		for (DetalleVenta detalleVenta : detalles) {
			
			DetalleVentaPK primaria = new DetalleVentaPK();
			
			primaria.setFacturaVenta(factura.getId());
			primaria.setProducto(detalleVenta.getProducto().getId());
			
			DetalleVenta detalle = buscar(primaria);
			
			if (detalle == null){
								
				em.persist(detalle);
				
			}
			
		}
		//System.out.println("detalle " + dv.getProducto().getNombre());
		//em.persist(dv);
		/**Query q = em.createNativeQuery("INSERT INTO DETALLE_VENTA (factura_venta_id, "
				+ "producto_id, cantidad, subtotal) VALUES (?1,?2,?3,?4)");
		q.setParameter(1, dv.getFacturaVenta().getId());
		q.setParameter(2, dv.getProducto().getId());
		q.setParameter(3, dv.getCantidad());
		q.setParameter(4, dv.getSubtotal());
		System.out.println("Query " + q.toString());
		q.executeUpdate();**/
	}
	
}
