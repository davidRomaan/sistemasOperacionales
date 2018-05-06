package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import co.edu.eam.ingesoft.bi.negocio.beans.ConexionEJB;
import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.BaseDatos;
import co.edu.eam.ingesoft.bi.presistencia.entidades.Conexion;

@Named("controladoAcceso")
@SessionScoped
public class ControladorAcceso implements Serializable {

	private String base;

	private List<BaseDatos> lista;

	@EJB
	private ConexionEJB conexionEJB;

	@PostConstruct
	private void cargarCampos() {

		lista = Arrays.asList(BaseDatos.values());

	}

	public void cambiarBD() {

		int bd = 0;

		if (base.equalsIgnoreCase("MYSQL")) {
			bd = 1;
		} else {
			bd = 2;
		}

		if (ConexionEJB.getBd() == bd) {

			Messages.addFlashGlobalWarn("El sistema ya se encuentra en esta base de datos");

		} else {

			Conexion c = new Conexion();
			c.setBaseDatos(base);
			c.setId(1);
			c.setCodigo(bd);

			conexionEJB.cambiarBD(c);

			Messages.addFlashGlobalInfo("Base de datos cambiada exitosamente");

		}

	}

	public List<BaseDatos> getLista() {
		return lista;
	}

	public void setLista(List<BaseDatos> lista) {
		this.lista = lista;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

}
