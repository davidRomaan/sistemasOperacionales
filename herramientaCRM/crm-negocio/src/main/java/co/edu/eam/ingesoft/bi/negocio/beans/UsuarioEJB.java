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
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@LocalBean
@Stateless
public class UsuarioEJB {

	@PersistenceContext
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario buscarUsuario(String user) {
		List<Usuario> us = em.createNamedQuery("Usuario.buscarUsuario").setParameter(1, user).getResultList();
		if (us.isEmpty()) {
			return null;
		} else {
			return us.get(0);
		}
	}

	/**
	 * metodo que busca las personas que se encuentren inactivos
	 * 
	 * @return lista con las personas inactivas
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Persona> listarActivosInactivos() {
		Query q = em.createNamedQuery(Persona.LISTA_PERSONA);
		List<Persona> per = q.getResultList();
		return per;
	}

	/**
	 * Busca un usuario en la base de datos
	 * 
	 * @param cedula
	 *            cédula del usuario que se desea buscar
	 * @return el usuario si lo encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario buscarUsuarioCedula(String cedula) {
		return em.find(Usuario.class, cedula);
	}

	/**
	 * Busca un cliente, verificando su tipo de usuario
	 * 
	 * @param cedula
	 *            cédula del cliente que se desea buscar
	 * @return el cliente si lo encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario buscarCliente(String cedula) {
		Usuario cliente = buscarUsuarioCedula(cedula);
		if (cliente != null) {
			if (cliente.getTipoUsuario().getNombre().equalsIgnoreCase("Cliente")) {
				return cliente;
			}
		}
		return null;
	}

	/**
	 * Registra un usuario en la base de datos
	 * 
	 * @param usuario
	 *            usuario que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarUsuario(Usuario usuario) throws ExcepcionNegocio {
		if (buscarCliente(usuario.getCedula()) != null) {
			throw new ExcepcionNegocio("El cliente ya existe");
		} else {
			em.persist(usuario);
		}
	}

}
