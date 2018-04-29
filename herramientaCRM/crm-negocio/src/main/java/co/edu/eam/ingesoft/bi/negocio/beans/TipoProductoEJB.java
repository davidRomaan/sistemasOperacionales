package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoProducto;

@LocalBean
@Stateless
public class TipoProductoEJB {

	@PersistenceContext
	private EntityManager em;
	
	
	/**
	 * Busca si un tipo de producto esta registrado en la base de datos
	 * @param codigo codigo por que se desea buscar
	 * @return tipo si lo encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoProducto buscar(int codigo){
		return em.find(TipoProducto.class, codigo);
	}
	
	
	
	/**
	 * Registra un tipoproducto en la BD
	 * 
	 * @param tipoproducto
	 *             que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarTipoProd(TipoProducto tipoproducto) throws ExcepcionNegocio {
		// TODO Auto-generated method stub
		if (buscar(tipoproducto.getId()) != null) {
			throw new ExcepcionNegocio("Este tipo de producto ya existe");
		} else {
			em.persist(tipoproducto);
		}
	}
	
	/**
	 * Edita un tipoproducto en la BD
	 * 
	 * @param tipoproducto
	 *             que se desea editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarTipoProducto(TipoProducto tipoproducto) {
		em.merge(tipoproducto);
	}
	
	
	/**
	 * Elimina un tipoproducto de la BD
	 * 
	 * @param tipoproducto
	 *             que se desea eliminar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarTipoProd(TipoProducto tipoproducto) {
		em.remove(em.merge(tipoproducto));
	
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoProducto> listarTipos() {
		Query q = em.createNamedQuery(TipoProducto.LISTAR);
		List<TipoProducto> lista = q.getResultList();
		return lista;
	}
}
