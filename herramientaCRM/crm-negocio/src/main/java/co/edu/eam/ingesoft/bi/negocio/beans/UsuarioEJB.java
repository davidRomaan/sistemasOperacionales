package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.DTO.UsuariosDTO;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@LocalBean
@Stateless
public class UsuarioEJB {

	@PersistenceContext
	private EntityManager em;

	@EJB
	private PersonaEJB personaEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

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
	public List<Usuario> listarActivosInactivos() {
		Query q = em.createNamedQuery(Usuario.LISTA_USUARIOS);
		List<Usuario> lista = q.getResultList();
		return lista;
	}

	/**
	 * metodo que genera una clave aleatoria de 8 digitos
	 * 
	 * @return la clave
	 */
	public String generarClave() {
		String clave = UUID.randomUUID().toString().toUpperCase().substring(0, 8);
		return clave;
	}

	/**
	 * Busca un usuario en la base de datos
	 * 
	 * @param cedula
	 *            cédula del usuario que se desea buscar
	 * @return el usuario si lo encuentra, de lo contrario null
	 */
	public Usuario buscarUsuarioCedula(String cedula) {
		return em.find(Usuario.class, cedula);
	}

	/**
	 * edita un usuario
	 * 
	 * @param u
	 *            el usuario a editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarUsuario(Usuario u) {
		em.merge(u);
	}

	/**
	 * Busca un cliente, verificando su tipo de usuario
	 * 
	 * @param cedula
	 *            cédula del cliente que se desea buscar
	 * @return el cliente si lo encuentra, de lo contrario null
	 
	public Usuario buscarCliente(String cedula) {
		Usuario cliente = buscarUsuarioCedula(cedula);
		if (cliente.getTipoUsuario().getNombre().equalsIgnoreCase("cliente")) {
			return cliente;
		}
		return null;
	}*/

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
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarCliente (Persona p){
		if (buscarCliente(p.getCedula()) != null){
			throw new ExcepcionNegocio("El cliente ya existe");
		} else {
			em.persist(p);
		}
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Persona buscarCliente (String cedula){
		Query q = em.createNativeQuery("SELECT * FROM USUARIO WHERE cedula = ?1");
		q.setParameter(1, cedula);
		List<Persona> lista = q.getResultList();
		if (lista.size() == 0){
			return em.find(Persona.class, cedula);
		}
		return null;
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<UsuariosDTO> llenarDTO() {

		List<Persona> personas = personaEJB.listarPersona();
		List<Usuario> usuarios = usuarioEJB.listarActivosInactivos();
		if (personas != null) {

			List<UsuariosDTO> lista = new ArrayList<UsuariosDTO>();
			for (int j = 0; j < usuarios.size(); j++) {

				String nombre = usuarios.get(j).getNombre();
				String apellido = usuarios.get(j).getApellido();
				String cedula = usuarios.get(j).getCedula();
				boolean estado = usuarios.get(j).isActivo();

				UsuariosDTO objeto = new UsuariosDTO(nombre, apellido, cedula, estado);
				lista.add(objeto);
			}
			return lista;
		} else {
			throw new ExcepcionNegocio("no hay datos para mostrar");
		}

	}
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarCliente(Persona p){
		em.merge(p);
	}
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarCliente(Persona p){
		em.remove(em.merge(p));
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarUsu(Usuario u) throws ExcepcionNegocio {
		em.persist(u);
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Usuario buscarUsu(String cedula) {
		return em.find(Usuario.class, cedula);
	}
}
