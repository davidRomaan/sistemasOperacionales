package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;

@LocalBean
@Stateless
public class MunicipioEJB {
	
	@EJB
	private Persistencia em;
	

	public Municipio buscar(int id){
		em.setBd(ConexionEJB.getBd());
		return (Municipio) em.buscar(Municipio.class, id);
	}

}
