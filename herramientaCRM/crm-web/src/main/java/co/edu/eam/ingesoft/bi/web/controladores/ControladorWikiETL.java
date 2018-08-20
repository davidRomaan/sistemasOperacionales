package co.edu.eam.ingesoft.bi.web.controladores;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import co.edu.eam.ingesoft.bi.negocio.etl.ETLWikiEJB;

@SessionScoped
@Named("controladorWikiETL")
public class ControladorWikiETL implements Serializable {
	
	@EJB
	private ETLWikiEJB wikiEJB;
	
	public void obtenerDatosWiki(){
		wikiEJB.obtenerDatosWikiAcumulacionSimple("2018-07-15", "2018-09-01");
		wikiEJB.cargarDatosDWH(wikiEJB.obtenerDatosWikiRolling());
	}

}
