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
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;

@LocalBean
@Stateless
public class TipoUsuarioEJB {

	@EJB
	private Persistencia em;
	
	/**
	 * Registra un tipo de usuario en la bd
	 * @param tu tipo de usuario que se desea registrar
	 */
	public void registrar(TipoUsuario tu){
		if (buscar(tu.getNombre()) != null){
			throw new ExcepcionNegocio("Ya existe un tipo de usuario con este nombre");
		} else {
			em.setBd(ConexionEJB.getBd());
			em.crear(tu);
		}
	}
	
	/**
	 * Busca un tipo de usuario por el nombre
	 * @param nombre nombre del tipo de usuario que se desea buscar
	 * @return el tipo de usuario
	 */
	public TipoUsuario buscar(String nombre){
		em.setBd(ConexionEJB.getBd());
		List<TipoUsuario> lista = (List<TipoUsuario>)(Object) 
				em.listarConParametroString(TipoUsuario.BUSCAR_NOMBRE, nombre);
		if (lista.size() == 0){
			return null;
		}
		return lista.get(0);
	}
	
	/**
	 * Busca un tipo de usuario por su id
	 * @param id id del tipo de usuario
	 * @return el tipo de usuario si lo encuentra, de lo contrario null
	 */
	public TipoUsuario buscarID (int id){
		em.setBd(ConexionEJB.getBd());
		return (TipoUsuario) em.buscar(TipoUsuario.class, id);
	}
	
	/**
	 * Lsita los tipos de usuario registrados
	 * @return los tipos de usuario registrados
	 */
	public List<TipoUsuario> listar(){
		em.setBd(ConexionEJB.getBd());
		return (List<TipoUsuario>)(Object) em.listar(TipoUsuario.LISTAR);
	}
	
	/**
	 * Elimina un tipo de usuario de la bd
	 * @param tu tipo de usuario que se desea elminar
	 */
	public void eliminar(TipoUsuario tu){
		em.setBd(ConexionEJB.getBd());
		em.eliminar(tu);
	}
	
	/**
	 * Edita un tipo de usuario
	 * @param tu tipo de usuario a editar
	 */
	public void editar(TipoUsuario tu){
		em.setBd(ConexionEJB.getBd());
		em.editar(tu);
	}
	
}
