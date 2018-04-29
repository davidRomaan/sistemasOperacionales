package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;

@LocalBean
@Stateless
public class PersonaEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Busca si una persona esta registrada en la base de datos
	 * @param cedula c�dula de la persona que se desea buscar
	 * @return la persona si la encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Persona buscar(String cedula){
		return em.find(Persona.class, cedula);
	}
	
	/**
	 * Registra un cliente
	 * @param c Cliente que se desea registrar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void crearPersona(Persona p){
		Persona cli = buscar(p.getCedula());
		if (cli == null){
			em.persist(p);
		} else {
			//throw new ExcepcionNegocio("El cliente que desea registrar ya se encuentra registrado");
		}
	}
	

	/**
	 * edita una persona
	 * @param p la persona que se va a editar
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarPersona(Persona p) {
		em.merge(p);
	}
	
	
	/**
	 * 
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Persona> listarPersona(){
		Query q = em.createNamedQuery(Persona.LISTA_PERSONA);
		List<Persona> clientes = q.getResultList();
		return clientes;
	}
	
	/**
	 * 
	 * @return
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listaMunicipios(){
		Query q = em.createNamedQuery(Municipio.LISTAR_MUNICIPIO);
		List<Municipio> municipio = q.getResultList();
		return municipio;
	}
	
	

}