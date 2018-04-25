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

}
