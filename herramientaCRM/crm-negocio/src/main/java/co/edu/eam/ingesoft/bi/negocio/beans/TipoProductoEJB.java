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
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoProducto;

@LocalBean
@Stateless
public class TipoProductoEJB {

	@EJB
	private Persistencia em;
	
	
	/**
	 * Busca si un tipo de producto esta registrado en la base de datos
	 * @param codigo codigo por que se desea buscar
	 * @return tipo si lo encuentra, de lo contrario null
	 */
	public TipoProducto buscar(int codigo){
		em.setBd(ConexionEJB.getBd());
		return (TipoProducto) em.buscar(TipoProducto.class, codigo);
	}
	
	
	
	/**
	 * Registra un tipoproducto en la BD
	 * 
	 * @param tipoproducto
	 *             que se desea registrar
	 */
	public void registrarTipoProd(TipoProducto tipoproducto) throws ExcepcionNegocio {
		// TODO Auto-generated method stub
		if (buscar(tipoproducto.getId()) != null) {
			throw new ExcepcionNegocio("Este tipo de producto ya existe");
		} else {
			em.setBd(ConexionEJB.getBd());
			em.crear(tipoproducto);
		}
	}
	
	/**
	 * Edita un tipoproducto en la BD
	 * 
	 * @param tipoproducto
	 *             que se desea editar
	 */
	public void editarTipoProducto(TipoProducto tipoproducto) {
		em.setBd(ConexionEJB.getBd());
		em.editar(tipoproducto);
	}
	
	
	/**
	 * Elimina un tipoproducto de la BD
	 * 
	 * @param tipoproducto
	 *             que se desea eliminar
	 */
	public void eliminarTipoProd(TipoProducto tipoproducto) {
		em.setBd(ConexionEJB.getBd());
		em.eliminar(tipoproducto);
	
	}
	
	
	public List<TipoProducto> listarTipos() {
		em.setBd(ConexionEJB.getBd());
		return (List<TipoProducto>)(Object) em.listar(TipoProducto.LISTAR);
	}
}
