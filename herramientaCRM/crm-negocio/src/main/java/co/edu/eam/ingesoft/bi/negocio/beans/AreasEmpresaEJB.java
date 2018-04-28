package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;


@Stateless
public class AreasEmpresaEJB {

	
	@PersistenceContext
	private EntityManager em;
	
	
	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarAreas(Area a) throws ExcepcionNegocio {
		Area buscado = buscarArea(a.getId());
		if (buscado == null) {
			em.persist(a);
		} else {
			throw new ExcepcionNegocio("esta area ya se encuentra creada");
		}
	}

	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Area buscarArea(int codigo) {
		return em.find(Area.class, codigo);
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarArea(Area a) {
		em.merge(a);
	}

	
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarArea(Area a) {
		em.remove(em.merge(a));
	
	}
	
	
	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Area> listarAreas() {
		Query q = em.createNamedQuery(Area.LISTAR_AREAS);
		List<Area> lista = q.getResultList();
		return lista;
	}

}
