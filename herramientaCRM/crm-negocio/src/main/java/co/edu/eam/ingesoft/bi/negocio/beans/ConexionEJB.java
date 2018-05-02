package co.edu.eam.ingesoft.bi.negocio.beans;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Conexion;

@LocalBean
@Stateless
public class ConexionEJB {

	@EJB
	private Persistencia em;
	
	/**
	 * Obtiene la conexión que se guardó la última vez
	 * @return la base de datos a la cual se estaba apuntando la última vez
	 */
	public Conexion obtenerBD (){
		return (Conexion) em.buscar(Conexion.class, 1);
	}
	
}
