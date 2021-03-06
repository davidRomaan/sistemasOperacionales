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

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.persistencia.DTO.UsuariosDTO;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;

@LocalBean
@Stateless
public class UsuarioEJB {

	@EJB
	private Persistencia em;

	@EJB
	private PersonaEJB personaEJB;

	@EJB
	private UsuarioEJB usuarioEJB;

	public Usuario buscarUsuario(String user) {
		em.setBd(ConexionEJB.getBd());
		List<Usuario> us = (List<Usuario>) (Object) em.listarConParametroString(Usuario.BUSCAR_USUARIO, user);
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
		em.setBd(ConexionEJB.getBd());
		return (List<Usuario>) (Object) em.listar(Usuario.LISTA_USUARIOS);
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
	 *            c�dula del usuario que se desea buscar
	 * @return el usuario si lo encuentra, de lo contrario null
	 */
	public Usuario buscarUsuarioCedula(String cedula) {
		em.setBd(ConexionEJB.getBd());
		return (Usuario) em.buscar(Usuario.class, cedula);
	}

	/**
	 * edita un usuario
	 * 
	 * @param u
	 *            el usuario a editar
	 */
	public void editarUsuario(Usuario u) {
		em.setBd(ConexionEJB.getBd());
		em.editar(u);
	}

	/**
	 * Registra un usuario en la base de datos
	 * 
	 * @param usuario
	 *            usuario que se desea registrar
	 */
	public void registrarUsuario(Usuario usuario) throws ExcepcionNegocio {
		if (buscarCliente(usuario.getCedula()) != null) {
			throw new ExcepcionNegocio("El cliente ya existe");
		} else {
			em.setBd(ConexionEJB.getBd());
			em.crear(usuario);
		}
	}

	public void registrarCliente(Persona p) {
		if (buscarCliente(p.getCedula()) != null) {
			throw new ExcepcionNegocio("El cliente ya existe");
		
		} else if (buscarUsu(p.getCedula()) != null){
			throw new ExcepcionNegocio("Ya existe una persona con el n�mero de identificaci�n ingresado");			
		}		
		else {
			em.setBd(ConexionEJB.getBd());
			em.crear(p);
		}
	}

	public Persona buscarCliente(String cedula) {
		em.setBd(ConexionEJB.getBd());
		return em.buscarCliente(cedula);
	}

	public List<UsuariosDTO> llenarDTO()throws Exception {

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

	public void editarCliente(Persona p) {
		em.setBd(ConexionEJB.getBd());
		em.editar(p);
	}

	public void eliminarCliente(Persona p) {
		em.setBd(ConexionEJB.getBd());
		em.eliminar(p);
	}
	
	public void eliminarUsuario(Usuario u){
		em.setBd(ConexionEJB.getBd());
		em.eliminar(u);
	}

	public void registrarUsu(Usuario u) throws ExcepcionNegocio {
		em.setBd(ConexionEJB.getBd());
		if (buscarUsu(u.getCedula()) != null){
			throw new ExcepcionNegocio("Este usuario ya existe");
		} else {
		em.crear(u);
		}
	}

	public Usuario buscarUsu(String cedula) {
		em.setBd(ConexionEJB.getBd());
		return (Usuario) em.buscar(Usuario.class, cedula);
	}
}
