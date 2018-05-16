package co.edu.eam.ingesoft.bi.negocio.etl;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;

@LocalBean
@Stateless
public class ETLAuditoriaEJB {

	
	@EJB
	private Persistencia em;
	
	
	public void cargarDatosDelDia(){
		
		Date fecha = new Date();
		
		
		
	}
}
