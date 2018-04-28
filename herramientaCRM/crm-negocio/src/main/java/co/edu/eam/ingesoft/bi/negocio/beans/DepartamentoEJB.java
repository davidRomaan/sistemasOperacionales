package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;

@LocalBean
@Stateless
public class DepartamentoEJB {

	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Lista los departamentos registrados en la BD
	 * @return lista de departamentos registrados
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Departamento> listarDepartamentos (){
		Query q = em.createNamedQuery(Departamento.LISTAR);
		List<Departamento> lista = q.getResultList();
		return lista;
	}
	
	/**
	 * Busca un departamento en la bd
	 * @param id código del depto
	 * @return el deto si lo encuentra, de lo contrario null
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Departamento buscarDepto (int id){
		return em.find(Departamento.class, id);
	}
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Municipio buscarMunicipio (int id){
		return em.find(Municipio.class, id);
	}
	
	/**
	 * Lista los municipios de un departamento
	 * @param idDepto código del departamento
	 * @return la lista de municipios del departamento
	 */
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Municipio> listarMunicipiosDepartamento (int idDepto){
		Query q = em.createNamedQuery(Municipio.LISTAR_MUNICIPIO_DEPTO);
		q.setParameter(1, idDepto);
		List<Municipio> lista = q.getResultList();
		return lista;
	}
	
}
