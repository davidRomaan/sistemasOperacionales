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
import co.edu.eam.ingesoft.bi.presistencia.entidades.Cargo;

@LocalBean
@Stateless
public class CargoEJB {

	@EJB
	private Persistencia em;

	public void registrarCargo(Cargo c) throws ExcepcionNegocio {
		Cargo buscado = buscarCargo(c.getId());
		if (buscado == null) {
			em.crearEnTodasBD(c);
		} else {
			throw new ExcepcionNegocio("este cargo ya se encuentra creado");
		}
	}

	public Cargo buscarCargo(int codigo) {
		em.setBd(ConexionEJB.getBd());
		return (Cargo) em.buscar(Cargo.class, codigo);
	}

	public void editarCargo(Cargo c) {
		em.editarEnTodasBD(c);
	}

	public void eliminarCargo(Cargo c) {
		em.eliminarEnTodasBD(c);

	}

	public List<Cargo> listarCargos() {
		em.setBd(ConexionEJB.getBd());
		return (List<Cargo>) (Object) em.listar(Cargo.LISTAR_CARGOS);
	}

}
