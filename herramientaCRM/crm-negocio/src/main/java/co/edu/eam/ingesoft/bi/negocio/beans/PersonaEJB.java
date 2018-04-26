package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Persona;

@Stateless
public class PersonaEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Busca si una persona esta registrada en la base de datos
	 * @param cedula cédula de la persona que se desea buscar
	 * @return la persona si la encuentra, de lo contrario null
	 */
	public Persona buscar(String cedula){
		return em.find(Persona.class, cedula);
	}
	
	

}
