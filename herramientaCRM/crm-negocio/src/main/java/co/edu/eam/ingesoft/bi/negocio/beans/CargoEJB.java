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
import co.edu.eam.ingesoft.bi.presistencia.entidades.Cargo;

@LocalBean
@Stateless
public class CargoEJB {

	@PersistenceContext
	private EntityManager em;

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void registrarCargo(Cargo c) throws ExcepcionNegocio {
		Cargo buscado = buscarCargo(c.getId());
		if (buscado == null) {
			em.persist(c);
		} else {
			throw new ExcepcionNegocio("este cargo ya se encuentra creado");
		}
	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public Cargo buscarCargo(int codigo) {
		return em.find(Cargo.class, codigo);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void editarCargo(Cargo c) {
		em.merge(c);
	}

	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void eliminarCargo(Cargo c) {
		em.remove(em.merge(c));

	}

	@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
	public List<Cargo> listarCargos() {
		Query q = em.createNamedQuery(Cargo.LISTAR_CARGOS);
		List<Cargo> lista = q.getResultList();
		return lista;
	}

}
