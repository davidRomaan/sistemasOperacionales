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
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;

@LocalBean
@Stateless
public class TipoUsuarioEJB {

	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Registra un tipo de usuario en la bd
	 * @param tu tipo de usuario que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrar(TipoUsuario tu){
		if (buscar(tu.getNombre()) != null){
			throw new ExcepcionNegocio("Ya existe un tipo de usuario con este nombre");
		} else {
			em.persist(tu);
		}
	}
	
	/**
	 * Busca un tipo de usuario por el nombre
	 * @param nombre nombre del tipo de usuario que se desea buscar
	 * @return el tipo de usuario
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public TipoUsuario buscar(String nombre){
		Query q = em.createNamedQuery(TipoUsuario.BUSCAR_NOMBRE);
		q.setParameter(1, nombre);
		List<TipoUsuario> lista = q.getResultList();
		if (lista.size() == 0){
			return null;
		}
		return lista.get(0);
	}
	
	/**
	 * Lsita los tipos de usuario registrados
	 * @return los tipos de usuario registrados
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<TipoUsuario> listar(){
		Query q = em.createNamedQuery(TipoUsuario.LISTAR);
		List<TipoUsuario> lista = q.getResultList();
		return lista;
	}
	
	/**
	 * Elimina un tipo de usuario de la bd
	 * @param tu tipo de usuario que se desea elminar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminar(TipoUsuario tu){
		em.remove(em.merge(tu));
	}
	
	/**
	 * Edita un tipo de usuario
	 * @param tu tipo de usuario a editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editar(TipoUsuario tu){
		em.merge(tu);
	}
	
}
