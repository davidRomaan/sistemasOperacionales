package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;

@Stateless
public class MunicipioEJB {
	
	@PersistenceContext
	private EntityManager em;
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio buscar(int id){
		return em.find(Municipio.class, id);
	}

}
