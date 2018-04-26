package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Usuario;


@Stateless
public class UsuarioEJB {

	@PersistenceContext
	private EntityManager em;
	
	
	public Usuario buscarUsuario(String user) {
		List<Usuario>us= em.createNamedQuery("Usuario.buscarUsuario").setParameter(1, user).getResultList();
		if(us.isEmpty()){
			return null;
		}else{
			return us.get(0);
		}
	}
	
	/**
	 * Busca un usuario en la base de datos
	 * @param cedula cédula del usuario que se desea buscar
	 * @return el usuario si lo encuentra, de lo contrario null
	 */
	public Usuario buscarUsuarioCedula (String cedula){
		return em.find(Usuario.class, cedula);
	}
	
	/**
	 * Busca un cliente, verificando su tipo de usuario
	 * @param cedula cédula del cliente que se desea buscar
	 * @return el cliente si lo encuentra, de lo contrario null
	 */
	public Usuario buscarCliente (String cedula){
		Usuario cliente = buscarUsuarioCedula(cedula);
		if (cliente.getTipoUsuario().getNombre().equals("cliente")){
			return cliente;
		}
		return null;
	}
	
	/**
	 * Registra un usuario en la base de datos
	 * @param usuario usuario que se desea registrar
	 */
	public void registrarUsuario (Usuario usuario){
		em.persist(usuario);
	}

}
