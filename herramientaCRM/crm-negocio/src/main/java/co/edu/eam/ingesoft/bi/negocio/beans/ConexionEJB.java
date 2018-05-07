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
	
	private static int bd = 0;
	
	/**
	 * Obtiene la conexi�n que se guard� la �ltima vez
	 */
	public void ultimaBD () {
		em.setBd(2);
		Conexion con  = (Conexion) em.buscar(Conexion.class, 1);
		bd = con.getCodigo();
	}
	
	public void cambiarBD (Conexion conexion){
		em.setBd(2);
		em.editar(conexion);
	}

	public static int getBd() {
		return bd;
	}

	public static void setBd(int bd) {
		ConexionEJB.bd = bd;
	}
	
}
