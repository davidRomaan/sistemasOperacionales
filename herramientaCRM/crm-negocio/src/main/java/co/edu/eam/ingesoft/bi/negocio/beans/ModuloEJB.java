package co.edu.eam.ingesoft.bi.negocio.beans;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.negocios.exception.ExcepcionNegocio;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Modulo;
import co.edu.eam.ingesoft.bi.presistencia.entidades.ModulosUsuario;
import co.edu.eam.ingesoft.bi.presistencia.entidades.ModulosUsuarioPK;
import co.edu.eam.ingesoft.bi.presistencia.entidades.TipoUsuario;

@LocalBean
@Stateless
public class ModuloEJB {

	@EJB
	private Persistencia em;

	/**
	 * Registra un módulo en la bd
	 * 
	 * @param m
	 *            módulo que se desea registrar
	 */
	public void registrarModuloUsuario(ModulosUsuario m) {
		if (buscarModuloUsuario(m.getModulo_id().getId(), m.getTipoUsiario_id().getId()) != null) {
			throw new ExcepcionNegocio("Ya se asignó este módulo a este tipo de usuario");
		} else {
			em.setBd(ConexionEJB.getBd());
			em.registrarModuloUsuario(m);
		}
	}

	/**
	 * Busca un modulo de usuario en la base de datos
	 * 
	 * @param idModulo
	 *            código del módulo
	 * @param idTipoUsuario
	 *            tipo de usuario al que se le agregó el módulo
	 * @return el modulo del usuario si lo encuentra, de lo contraio null
	 */
	public ModulosUsuario buscarModuloUsuario(int idModulo, int idTipoUsuario) {
		ModulosUsuarioPK pk = new ModulosUsuarioPK();
		pk.setModulo_id(idModulo);
		pk.setTipoUsiario_id(idTipoUsuario);
		em.setBd(ConexionEJB.getBd());
		return (ModulosUsuario) em.buscar(ModulosUsuario.class, pk);
	}
	
	/**
	 * Lista los modulos registrados en la base de datos
	 * @return la lista de modulos registrados
	 */
	public List<Modulo> listar (){
		em.setBd(ConexionEJB.getBd());
		return (List<Modulo>)(Object) em.listar(Modulo.LISTAR);
	}
	
	/**
	 * Busca un módulo en la bd
	 * @param pk identificador de módulo
	 * @return el módulo si lo encuetra, de lo contrario null
	 */
	public Modulo buscar (int pk){
		em.setBd(ConexionEJB.getBd());
		return (Modulo) em.buscar(Modulo.class, pk);
	}
	
	/**
	 * Lista los modulos disponibles para un tipo de usuario
	 * @return la lista de los modulos
	 */
	public List<Modulo> listarModuloPorTipoUsuario (int tipoUser){
		List<Modulo> modulos = new ArrayList<Modulo>();
		List<ModulosUsuario> modulosUsuario = (List<ModulosUsuario>)(Object) 
				em.listarConParametroInt(ModulosUsuario.LISTAR, tipoUser);
		if (modulosUsuario.size() != 0){
			for (ModulosUsuario moduloUsuario : modulosUsuario) {
				modulos.add(moduloUsuario.getModulo_id());
			}
		}
		return modulos;
	}

}
