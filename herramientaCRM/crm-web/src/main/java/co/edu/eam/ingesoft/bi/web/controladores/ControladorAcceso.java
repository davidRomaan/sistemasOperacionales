package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.persistencia.enumeraciones.BaseDatos;

@Named("controladoAcceso")
@SessionScoped
public class ControladorAcceso implements Serializable {

	private BaseDatos base;

	private List<BaseDatos> lista;

	@PostConstruct
	private void cargarCampos() {

		lista = Arrays.asList(BaseDatos.values());
	}

	public List<BaseDatos> getLista() {
		return lista;
	}

	public void setLista(List<BaseDatos> lista) {
		this.lista = lista;
	}

	public BaseDatos getBase() {
		return base;
	}

	public void setBase(BaseDatos base) {
		this.base = base;
	}

}
