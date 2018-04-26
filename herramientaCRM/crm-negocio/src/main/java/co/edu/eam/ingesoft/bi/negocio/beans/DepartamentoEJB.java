package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;

@Stateless
public class DepartamentoEJB {

	@PersistenceContext
	private EntityManager em;
	
	/**
	 * Lista los departamentos registrados en la BD
	 * @return lista de departamentos registrados
	 */
	public List<Departamento> listarDepartamentos (){
		Query q = em.createNamedQuery(Departamento.LISTAR);
		List<Departamento> lista = q.getResultList();
		return lista;
	}
	
	/**
	 * Lista los municipios de un departamento
	 * @param idDepto c�digo del departamento
	 * @return la lista de municipios del departamento
	 */
	public List<Municipio> listarMunicipiosDepartamento (int idDepto){
		Query q = em.createNamedQuery(Municipio.LISTAR_MUNICIPIO_DEPTO);
		q.setParameter(1, idDepto);
		List<Municipio> lista = q.getResultList();
		return lista;
	}
	
}
