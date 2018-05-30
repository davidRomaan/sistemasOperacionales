package co.edu.eam.ingesoft.bi.negocio.dwh;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import co.edu.eam.ingesoft.bi.negocio.persistencia.Persistencia;
import co.edu.eam.ingesoft.bi.presistencia.entidades.datawh.HechoVentas;

@Stateless
@LocalBean
public class HechoVentasEJB {

	@EJB
	private Persistencia em;
	
	public List<HechoVentas> obtenerListaHechos(){		
		return em.listarHechosVenta();		
	}
	
}
