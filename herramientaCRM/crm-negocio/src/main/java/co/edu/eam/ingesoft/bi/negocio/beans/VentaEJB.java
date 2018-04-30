package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
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
	public void registrarVenta (FacturaVenta v){
		em.persist(v);
	}
	
	/**
	 * Registra un detalle venta en la base de datos
	 * @param dv detalle venta que se desea registrar
	 */
	public void registrarDetalleVenta (DetalleVenta dv){
		em.merge(dv);
	}
	
	/**
	 * Obtiene el �ltimo c�digo de la factura generada a un cliente
	 * @param cedulaCliente c�dula del clienteal cual se le gener� la factura
	 * @return el id de la �ltima factura
	 */
	public int codigoUltimaFacturaCliente (String cedulaCliente){
		Query q = em.createNamedQuery(FacturaVenta.OBTENER_ULTIMA_REGISTRADA);
		q.setParameter(1, cedulaCliente);
		return (int) ((Integer)q.getSingleResult());
	}
	
}
