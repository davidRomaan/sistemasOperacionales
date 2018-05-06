package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Area;

@LocalBean
@Stateless
public class AreasEmpresaEJB {

	@EJB
	private Persistencia em;

	public void registrarAreas(Area a) throws ExcepcionNegocio {
		Area buscado = buscarArea(a.getId());
		if (buscado == null) {
			em.crearEnTodasBD(a);
		} else {
			throw new ExcepcionNegocio("esta area ya se encuentra creada");
		}
	}

	public Area buscarArea(int codigo) {
		em.setBd(ConexionEJB.getBd());
		return (Area) em.buscar(Area.class, codigo);
	}

	public void editarArea(Area a) {
		em.editarEnTodasBD(a);
	}

	public void eliminarArea(Area a) {
		em.eliminarEnTodasBD(a);

	}

	public List<Area> listarAreas() {
		em.setBd(ConexionEJB.getBd());
		return (List<Area>) (Object) em.listar(Area.LISTAR_AREAS);
	}

}
