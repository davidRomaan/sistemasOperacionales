package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;

@LocalBean
@Stateless
public class PersonaEJB {

	@EJB
	private Persistencia em;

	/**
	 * Busca si una persona esta registrada en la base de datos
	 * 
	 * @param cedula
	 *            c�dula de la persona que se desea buscar
	 * @return la persona si la encuentra, de lo contrario null
	 */
	public Persona buscar(String cedula) {
		em.setBd(ConexionEJB.getBd());
		return (Persona) em.buscar(Persona.class, cedula);
	}

	/**
	 * Registra un cliente
	 * 
	 * @param c
	 *            Cliente que se desea registrar
	 */
	public void crearPersona(Persona p) {
		Persona cli = buscar(p.getCedula());
		if (cli == null) {
			em.setBd(ConexionEJB.getBd());
			em.crear(p);
		} else {
			// throw new ExcepcionNegocio("El cliente que desea registrar ya se
			// encuentra registrado");
		}
	}

	/**
	 * edita una persona
	 * 
	 * @param p
	 *            la persona que se va a editar
	 */
	public void editarPersona(Persona p) {
		em.setBd(ConexionEJB.getBd());
		em.editar(p);
	}

	/**
	 * 
	 * @return
	 */
	public List<Persona> listarPersona() {
		em.setBd(ConexionEJB.getBd());
		return (List<Persona>) (Object) em.listar(Persona.LISTA_PERSONA);
	}

	public List<Persona> listarClientes() {
		em.setBd(ConexionEJB.getBd());
		return em.listarClientes();
	}

}
