package co.edu.eam.ingesoft.bi.negocio.beans;

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
import co.edu.eam.ingesoft.bi.presistencia.entidades.DetalleVentaPK;
import co.edu.eam.ingesoft.bi.presistencia.entidades.FacturaVenta;

@LocalBean
@Stateless
public class DetalleVentaEJB {

	@EJB
	private Persistencia em;
	
	public DetalleVenta buscar(DetalleVentaPK pk) {
		em.setBd(ConexionEJB.getBd());
		return (DetalleVenta) em.buscar(DetalleVenta.class, pk);
	}

	/**
	 * Registra un detalle venta en la base de datos
	 * 
	 * @param dv
	 *            detalle venta que se desea registrar
	 */
	public void registrarDetalleVenta(List<DetalleVenta> detalles, FacturaVenta factura) {
		em.setBd(ConexionEJB.getBd());
		em.registrarDetalleVenta(detalles, factura);
	}

}
