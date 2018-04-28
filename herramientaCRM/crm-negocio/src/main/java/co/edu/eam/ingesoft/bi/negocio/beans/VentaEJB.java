package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
		em.persist(dv);
	}
	
}
