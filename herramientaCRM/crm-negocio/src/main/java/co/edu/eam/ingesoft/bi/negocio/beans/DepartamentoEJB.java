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
import co.edu.eam.ingesoft.bi.presistencia.entidades.Departamento;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Municipio;

@LocalBean
@Stateless
public class DepartamentoEJB {

	@EJB
	private Persistencia em;

	/**
	 * Lista los departamentos registrados en la BD
	 * 
	 * @return lista de departamentos registrados
	 */
	public List<Departamento> listarDepartamentos() {
		em.setBd(ConexionEJB.getBd());
		return (List<Departamento>) (Object) em.listar(Departamento.LISTAR_DEPARTAMENTOS);
	}

	/**
	 * Busca un departamento en la bd
	 * 
	 * @param id
	 *            c�digo del depto
	 * @return el deto si lo encuentra, de lo contrario null
	 */
	public Departamento buscarDepto(int id) {
		em.setBd(ConexionEJB.getBd());
		return (Departamento) em.buscar(Departamento.class, id);
	}

	public Municipio buscarMunicipio(int id) {
		em.setBd(ConexionEJB.getBd());
		return (Municipio) em.buscar(Municipio.class, id);
	}

	/**
	 * Lista los municipios de un departamento
	 * 
	 * @param idDepto
	 *            c�digo del departamento
	 * @return la lista de municipios del departamento
	 */
	public List<Municipio> listarMunicipiosDepartamento(int idDepto) {
		em.setBd(ConexionEJB.getBd());
		return (List<Municipio>) (Object) em.listarConParametroInt
				(Municipio.LISTAR_MUNICIPIO_DEPTO, idDepto);

	}

}
